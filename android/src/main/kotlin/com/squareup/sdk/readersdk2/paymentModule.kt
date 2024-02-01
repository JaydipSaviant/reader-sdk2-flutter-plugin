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
import com.squareup.sdk.reader2.payment.CurrencyCode
import com.squareup.sdk.reader2.payment.Money
import com.squareup.sdk.reader2.payment.PaymentManager
import com.squareup.sdk.reader2.payment.PaymentParameters
import com.squareup.sdk.readersdk2.payment.ChargeViewModel
import io.flutter.plugin.common.MethodChannel
import java.util.UUID
import com.squareup.sdk.reader2.payment.Payment
import com.squareup.sdk.readersdk2.payment.toHtml

class PaymentModule {

    private lateinit var context: Context
    fun startCheckout(
        checkoutParameters: HashMap<String, Any>?,
        result: MethodChannel.Result,
        paymentManager: PaymentManager,
        viewModel: ChargeViewModel,
        contextReader: Context,
        //resultInvoke :((String)->Unit)
    ) {
        if (checkoutParameters != null) {
            Log.d("Tageee", "checkoutParameters::$checkoutParameters , $result")

            startPayment(
                checkoutParameters,
                paymentManager,
                viewModel,
                contextReader,
//                paymentRes = {
//                    resultInvoke.invoke(it)
//                    Log.d("TAG", "startCheckout: it result = $it")
//                }
            )
        } else {
            return
            //"INVALID_ARGUMENT"
        }
    }

    private fun startPayment(
        checkoutParameters: HashMap<String, Any>?,
        paymentManager: PaymentManager,
        viewModel: ChargeViewModel,
        contextReader: Context,
       // paymentRes: ((PaymentResult) -> Unit),
    ) {
        context = contextReader
        // var paymentHandler: PaymentHandle? = null
        val checkoutParams: CheckoutParams? = checkoutParameters.toCheckoutParams()


        Log.d("TAG", "Actual payment start   62 $checkoutParams")
        val builder = PaymentParameters.Builder(
            amount = Money(checkoutParams?.amountMoney?.amount!!.toLong(), CurrencyCode.USD),
            idempotencyKey = UUID.randomUUID().toString(),
        ).autocomplete(true)
        Log.d("TAG", "startPayment: builder = $builder")
        Log.d("TAG", "startPayment: builder 11 = ${checkoutParams.amountMoney.amount}")
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
        Log.d("TAG", "startPayment:  116 9090  $parameters")
        viewModel.startPayment(parameters)
        var paymentResults: String = ""

        paymentManager.addPaymentCallback { result: PaymentResult ->
            when (result) {
                is Result.Success -> {
                    //showChargeSuccessDialog(result.value)
                    //paymentRes.invoke(result.value)
                    paymentResults = result.value.toString()
                    Log.d("TAG", "startPayment: 71 -- ${result.value.toString()}")
                    Log.d("TAG", "startPayment: 711 -- $paymentResults")
                    paymentResult(paymentResults)
                }

                is Result.Failure -> {
                   // paymentRes.invoke(result)
                    paymentResults = result.errorMessage
                    Log.d("TAG", "startPayment: 74 -- ${result.errorMessage}")
                    Log.d("TAG", "startPayment: 744 -- $paymentResults")
                    paymentResult(paymentResults)
                }
            }

            Log.d("TAG", "startPayment: -- ${result}")
        }
        Log.d("TAG", "startPayment: -- 123 ${paymentResults}")
    }

    fun paymentResult (result :String) : String{
        var paymentRes: String =result
        return paymentRes
    }

    private fun showChargeSuccessDialog(paymentResult: Payment) {
        Log.d("TAG", "showChargeSuccessDialog: payment = $paymentResult")
        showSimpleMessageDialog(
            title = "Success", message = Html.fromHtml(
                (paymentResult as Payment.OnlinePayment).toHtml(),
            )
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
            )
        )

    private fun showSimpleMessageDialog(
        title: String,
        message: CharSequence,
    ) {
        val dialog = AlertDialog.Builder(context).setTitle(title).setMessage(message)
            .setPositiveButton(android.R.string.ok, null).show()

        // Make the text inside the dialog selectable so one can copy it
        (dialog?.findViewById<View>(android.R.id.message) as TextView).apply {
            setTextIsSelectable(true)
        }
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
