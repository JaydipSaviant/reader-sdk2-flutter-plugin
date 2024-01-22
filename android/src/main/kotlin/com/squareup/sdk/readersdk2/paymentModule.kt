package com.squareup.sdk.readersdk2

import android.content.Context
import android.util.Log
import com.squareup.sdk.reader2.payment.CurrencyCode
import com.squareup.sdk.reader2.payment.Money
import com.squareup.sdk.reader2.payment.PaymentManager
import com.squareup.sdk.reader2.payment.PaymentParameters
import com.squareup.sdk.readersdk2.payment.ChargeViewModel
import io.flutter.plugin.common.MethodChannel
import java.util.UUID

class PaymentModule {


    //private var chargeFragment: ChargeFragment = ChargeFragment()

//    private var paymentManager : PaymentManager? = null

//    init {
//        Log.d("TAG", ": wqb dews dhw fhasdjkf has rhbahjsb rdhsb")
//        paymentManager = ReaderSdk.paymentManager()
//        Log.d("TAG", ": wqb dews dhw fhasdjkf has rhbahjsb rdhsb   26")
//    }

    fun startCheckout(
        checkoutParameters: HashMap<String, Any>?,
        result: MethodChannel.Result,
        paymentManager: PaymentManager,
        viewModel: ChargeViewModel,
        contextReader: Context,
    ) {
        if (checkoutParameters != null) {
            //MyFlutterActivity()
            //chargeFragment.startPayment()
            //paymentManager.startPaymentActivity(parameters)
            Log.d("Tageee", "checkoutParameters::$checkoutParameters , $result")
            startPayment(checkoutParameters, paymentManager!!, viewModel,contextReader)
            //Log.d("Tageee", "checkoutParameters::$checkoutParameters , $result")
        } else {
            result.error("INVALID_ARGUMENT", "Invalid arguments", null)
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

fun startPayment(
    checkoutParameters: HashMap<String, Any>?,
    paymentManager: PaymentManager,
    viewModel: ChargeViewModel,
    contextReader: Context,
) {
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
//    var tipAmount = 10
//        if (tipAmount != null && tipAmount > 0) {
//            builder.tipMoney(Money(tipAmount.toLong(), CurrencyCode.USD))
//        }
//        val feeAmount = checkoutParams.amountMoney.amount
//        if (feeAmount != null && feeAmount > 0) {
//            builder.appFeeMoney(Money(feeAmount.toLong(), CurrencyCode.USD))
//        }
//        val delayDuration = arguments?.getLong(KeypadFragment.PARAM_DELAY_DURATION)
//        if (delayDuration != null && delayDuration > 0) {
//            builder.delayDuration(delayDuration)
//        }
//    val orderId = UUID.randomUUID().toString()
//    if (!orderId.isNullOrEmpty()) {
//        builder.orderId(orderId)
//    }
//        val customerId = UUID.randomUUID().toString()
//        if (!customerId.isNullOrEmpty()) {
//            builder.customerId(customerId)
//        }
//        val teamMemberId = UUID.randomUUID().toString()
//        if (!teamMemberId.isNullOrEmpty()) {
//            builder.teamMemberId(teamMemberId)
//        }
//        val locationId = "LWTCANRWNHMF0"
//        if (!locationId.isNullOrEmpty()) {
//            builder.locationId(locationId)
//        }
//        val referenceId = UUID.randomUUID().toString()
//        if (!referenceId.isNullOrEmpty()) {
//            builder.referenceId(referenceId)
//        }
//    val note = "This is testing payment"/*arguments?.getString(KeypadFragment.PARAM_NOTE)*/
//    if (!note.isNullOrEmpty()) {
//        builder.note(note)
//    }
//        val statementDescription = "check"
//        if (!statementDescription.isNullOrEmpty()) {
//            builder.statementDescription(statementDescription)
//        }

    val parameters = builder.build()
    Log.d("TAG", "startPayment:  116 9090  $parameters")
    viewModel.startPayment(parameters,contextReader)
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
