package com.squareup.sdk.readersdk2

import android.content.Context
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.squareup.sdk.reader2.core.ErrorDetails
import com.squareup.sdk.reader2.core.Result
import com.squareup.sdk.reader2.extensions.PaymentResult
import com.squareup.sdk.reader2.payment.Card
import com.squareup.sdk.reader2.payment.CardPaymentDetails
import com.squareup.sdk.reader2.payment.CashDetails
import com.squareup.sdk.reader2.payment.CurrencyCode
import com.squareup.sdk.reader2.payment.Money
import com.squareup.sdk.reader2.payment.PaymentManager
import com.squareup.sdk.reader2.payment.PaymentParameters
import com.squareup.sdk.readersdk2.payment.ChargeViewModel
import io.flutter.plugin.common.MethodChannel
import java.util.UUID
import com.squareup.sdk.reader2.payment.Payment
import com.squareup.sdk.readersdk2.payment.toHtml
import io.flutter.embedding.engine.FlutterEngine

class PaymentModule {

    private lateinit var flutterEngine: FlutterEngine
    private lateinit var context: Context

    private val CHANNELFLUTTER = "readerSDKFlutter"
    private lateinit var methodChannelFlutter: MethodChannel
    fun startCheckout(
        checkoutParameters: HashMap<String, Any>?,
        result: MethodChannel.Result,
        paymentManager: PaymentManager,
        viewModel: ChargeViewModel,
        contextReader: Context,
        channel: MethodChannel
    ) : String {
        if (checkoutParameters != null) {
            return startPayment(checkoutParameters, paymentManager, viewModel, contextReader, channel)
        } else {
            return "INVALID_ARGUMENT"
        }
    }

    fun setFlutterEngine(flutterEngine: FlutterEngine) {
        this.flutterEngine = flutterEngine
    }
    private fun startPayment(
        checkoutParameters: HashMap<String, Any>?,
        paymentManager: PaymentManager,
        viewModel: ChargeViewModel,
        contextReader: Context,
        channel: MethodChannel
    ):String {
        context = contextReader
        val checkoutParams: CheckoutParams? = checkoutParameters.toCheckoutParams()


        val builder = PaymentParameters.Builder(
            amount = Money(checkoutParams?.amountMoney?.amount!!.toLong(), CurrencyCode.USD),
            idempotencyKey = UUID.randomUUID().toString(),
        ).autocomplete(true)
        /*if (!autoComplete) {
            builder.acceptPartialAuthorization(
                arguments?.getBoolean(KeypadFragment.PARAM_ACCEPT_PARTIAL) ?: false
            )
        }*/
        /*  var tipAmount = 10
              if (tipAmount != null && tipAmount > 0) {
                  builder.tipMoney(Money(tipAmount.toLong(), CurrencyCode.USD))
              }
              val feeAmount = checkoutParams.amountMoney.amount
              if (feeAmount != null && feeAmount > 0) {
                  builder.appFeeMoney(Money(feeAmount.toLong(), CurrencyCode.USD))
              }
              val delayDuration = arguments?.getLong(KeypadFragment.PARAM_DELAY_DURATION)
              if (delayDuration != null && delayDuration > 0) {
                  builder.delayDuration(delayDuration)
              }
          val orderId = UUID.randomUUID().toString()
          if (!orderId.isNullOrEmpty()) {
              builder.orderId(orderId)
          }
              val customerId = UUID.randomUUID().toString()
              if (!customerId.isNullOrEmpty()) {
                  builder.customerId(customerId)
              }
              val teamMemberId = UUID.randomUUID().toString()
              if (!teamMemberId.isNullOrEmpty()) {
                  builder.teamMemberId(teamMemberId)
              }
              val locationId = "LWTCANRWNHMF0"
              if (!locationId.isNullOrEmpty()) {
                  builder.locationId(locationId)
              }
              val referenceId = UUID.randomUUID().toString()
              if (!referenceId.isNullOrEmpty()) {
                  builder.referenceId(referenceId)
              }
          val note = "This is testing payment"*//*arguments?.getString(KeypadFragment.PARAM_NOTE)*//*
    if (!note.isNullOrEmpty()) {
        builder.note(note)
    }
        val statementDescription = "check"
        if (!statementDescription.isNullOrEmpty()) {
            builder.statementDescription(statementDescription)
        }*/

        val parameters = builder.build()
        viewModel.startPayment(parameters)
        var paymentResults: String = ""

         flutterEngine =  FlutterEngine(context);

         setFlutterEngine(flutterEngine)

        methodChannelFlutter = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "readersdk2")


        paymentManager.addPaymentCallback { paymentResult: PaymentResult ->
            when (paymentResult) {
                is Result.Success -> {
                    paymentResults = paymentResult.value.toString()
                    GlobleSingleTon.paymentResult = paymentResult.value
                    channel.invokeMethod("sendDataToFlutter", paymentResults)
                }
                is Result.Failure -> {
                    paymentResults = paymentResult.errorMessage
                    GlobleSingleTon.paymentFailure = paymentResult.errorMessage
                }
            }
        }
        return  paymentResults
    }

        private fun showChargeSuccessDialog(paymentResult: Payment) {
            showSimpleMessageDialog(
                title = "Success", message = Html.fromHtml(
                    (paymentResult as Payment.OnlinePayment).toHtml(),
                ), context
            )
        }


    private fun showChargeFailureDialog(errorMessage: String, details: List<ErrorDetails>) =
        showSimpleMessageDialog(
            title = "Error",
            message = Html.fromHtml(
                details.joinToString(prefix = "<b>$errorMessage</b><p>",
                    separator = "<p>",
                    limit = 5,
                    transform = {
                        it.detail + if (it.field.isNullOrBlank()) {
                            ""
                        } else {
                            "(${it.field}"
                        }
                    })
            ),
            context
        )

    private fun showSimpleMessageDialog(
        title: String,
        message: CharSequence,
        contextReader: Context
    ) {
        val dialog = AlertDialog.Builder(contextReader).setTitle(title).setMessage(message)
            .setPositiveButton(android.R.string.ok, null).show()

        // Make the text inside the dialog selectable so one can copy it
        (dialog?.findViewById<View>(android.R.id.message) as TextView).apply {
            setTextIsSelectable(true)
        }
    }
    fun convertOnlinePaymentToMap(onlinePayment: Payment): Map<String, Any> {
        val paymentMap = mutableMapOf<String, Any>()

        paymentMap["id"] = onlinePayment.id
        paymentMap["createdAt"] = onlinePayment.createdAt
        paymentMap["updatedAt"] = onlinePayment.updatedAt
        paymentMap["amountMoney"] = convertMoneyToMap(onlinePayment.amountMoney)
        paymentMap["tipMoney"] = convertMoneyToMap(onlinePayment.tipMoney!!)
        // Add other fields as needed
        // Convert cardDetails to a Map
        paymentMap["cashDetails"] = convertCashDetailsToMap(onlinePayment.cashDetails)
        return paymentMap
    }

    private fun convertMoneyToMap(money: Money): Map<String, Any> {
        val moneyMap = mutableMapOf<String, Any>()

        moneyMap["amount"] = money.amount
        moneyMap["currency"] = money.currencyCode

        return moneyMap
    }

    private fun convertCashDetailsToMap(cashDetails: CashDetails?): Map<String, Any> {
        val cardDetailsMap = mutableMapOf<String, Any>()

        if (cashDetails != null) {
            cardDetailsMap["buyerSuppliedMoney"] = cashDetails.buyerSuppliedMoney
            cardDetailsMap["changeBackMoney"] = cashDetails.changeBackMoney
        }
    //    cardDetailsMap["card"] = convertCardToMap(cashDetails)

        return cardDetailsMap
    }
    fun convertCardDetailsToMap(cardPaymentDetails: CardPaymentDetails.OnlineCardPaymentDetails): Map<String, Any> {
        val cardDetailsMap = mutableMapOf<String, Any>()

        if (cardPaymentDetails != null) {
            cardDetailsMap["status"] = cardPaymentDetails.status
            cardDetailsMap["entryMethod"] = cardPaymentDetails.entryMethod
            cardDetailsMap["accountType"] = cardPaymentDetails.accountType!!
        }
        cardDetailsMap["card"] = convertCardToMap(cardPaymentDetails.card)

        return cardDetailsMap
    }
    fun convertCardToMap(card: Card): Map<String, Any> {
        val cardMap = mutableMapOf<String, Any>()

        cardMap["brand"] = card.brand
        cardMap["lastFourDigits"] = card.lastFourDigits
        cardMap["cardCoBrand"] = card.cardCoBrand
        cardMap["expirationMonth"] = card.expirationMonth
        cardMap["expirationYear"] = card.expirationYear
        cardMap["cardholderName"] = card.cardholderName!!
        cardMap["id"] = card.id!!
        // Add other fields as needed

        return cardMap
    }
}

fun HashMap<String, Any>?.toCheckoutParams(): CheckoutParams? {
    return this?.let {
        CheckoutParams(
            tipSettings = it["tipSettings"] as? TipSettings ?: TipSettings(
                emptyList(),
                false,
                false
            ),
            note = it["note"] as? String,
            skipReceipt = it["skipReceipt"] as? Boolean ?: false,
            amountMoney = (it["amountMoney"] as? HashMap<*, *>)?.let { moneyMap ->
                AmountMoney(
                    amount = moneyMap["amount"] as? Int ?: 0,
                    currencyCode = moneyMap["currencyCode"] as? String ?: ""
                )
            } ?: AmountMoney(0, ""),
            collectSignature = it["collectSignature"] as? Boolean ?: false,
            allowSplitTender = it["allowSplitTender"] as? Boolean ?: false,
            delayCapture = it["delayCapture"] as? Boolean ?: false,
            additionalPaymentTypes = (it["additionalPaymentTypes"] as? List<*>)?.mapNotNull { type ->
                type as? String
            } ?: emptyList()
        )
    }

}


data class CheckoutParams(
    val tipSettings: TipSettings,
    val note: String?,
    val skipReceipt: Boolean,
    val amountMoney: AmountMoney,
    val collectSignature: Boolean,
    val allowSplitTender: Boolean,
    val delayCapture: Boolean,
    val additionalPaymentTypes: List<String>,
)

data class TipSettings(
    val tipPercentages: List<Int>,
    val showSeparateTipScreen: Boolean,
    val showCustomTipField: Boolean,
)

data class AmountMoney(
    val amount: Int,
    val currencyCode: String,
)


