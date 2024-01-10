package com.squareup.sdk.readersdk2

import android.os.Bundle
import android.util.Log
import com.squareup.sdk.reader2.ReaderSdk
import com.squareup.sdk.reader2.payment.CurrencyCode
import com.squareup.sdk.reader2.payment.Money
import com.squareup.sdk.reader2.payment.PaymentParameters
import com.squareup.sdk.readersdk2.payment.ChargeFragment
import com.squareup.sdk.readersdk2.payment.KeypadFragment
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.plugin.common.MethodChannel
import java.util.UUID

class PaymentModule {

    //private var chargeFragment: ChargeFragment = ChargeFragment()

    // private val paymentManager = ReaderSdk.paymentManager()

    fun startCheckout(checkoutParameters: HashMap<String, Any>?, result: MethodChannel.Result) {
        if (checkoutParameters != null) {
            //MyFlutterActivity()
            //chargeFragment.startPayment()
            //paymentManager.startPaymentActivity(parameters)
            Log.d("Tageee", "checkoutParameters::$checkoutParameters , $result")
            startPayment()
            //Log.d("Tageee", "checkoutParameters::$checkoutParameters , $result")
        } else {
            result.error("INVALID_ARGUMENT", "Invalid arguments", null)
        }
    }

    fun startPayment() {
        Log.d("Tageee", "checkoutParameters 666:: ,")
//
//        if (!isAdded) {
//            Log.w("Tageee", "Fragment is not added to its activity. Cannot start payment.")
//            return
//        }
//
//        Log.d("Tageee", "checkoutParameters 1:: ,")
//
//        if (viewModel.paymentInProgress) return
//        Log.d("Tageee", "checkoutParameters 222:: ,")
//        val autoComplete: Boolean =
//            arguments?.getBoolean(KeypadFragment.PARAM_AUTOCOMPLETE) ?: false
//        Log.d("Tageee", "checkoutParameters start payment:: , $autoComplete")
//
//        val idempotencyKey = arguments?.getString(KeypadFragment.IDEMPOTENCY_KEY)
//        val builder = PaymentParameters.Builder(
//            amount = Money(baseAmount, CurrencyCode.USD),
//            /* An idempotency key can be optionally provided by the user through manual entry. In cases
//            where the idempotency key is not provided by the user, we will generate random IdempotencyKey.
//            Manually entered IdempotencyKey is used for internal tests only!
//             */
//            idempotencyKey = if (idempotencyKey.isNullOrEmpty()) {
//                UUID.randomUUID().toString()
//            } else {
//                idempotencyKey
//            },
//        ).autocomplete(autoComplete)
//
//        if (!autoComplete) {
//            builder.acceptPartialAuthorization(
//                arguments?.getBoolean(KeypadFragment.PARAM_ACCEPT_PARTIAL) ?: false
//            )
//        }
//        val tipAmount = arguments?.getLong(KeypadFragment.PARAM_TIP_AMOUNT)
//        if (tipAmount != null && tipAmount > 0) {
//            builder.tipMoney(Money(tipAmount, CurrencyCode.USD))
//        }
//        val feeAmount = arguments?.getLong(KeypadFragment.PARAM_FEE_AMOUNT)
//        if (feeAmount != null && feeAmount > 0) {
//            builder.appFeeMoney(Money(feeAmount, CurrencyCode.USD))
//        }
//        val delayDuration = arguments?.getLong(KeypadFragment.PARAM_DELAY_DURATION)
//        if (delayDuration != null && delayDuration > 0) {
//            builder.delayDuration(delayDuration)
//        }
//        val orderId = arguments?.getString(KeypadFragment.PARAM_ORDER_ID)
//        if (!orderId.isNullOrEmpty()) {
//            builder.orderId(orderId)
//        }
//        val customerId = arguments?.getString(KeypadFragment.PARAM_CUSTOMER_ID)
//        if (!customerId.isNullOrEmpty()) {
//            builder.customerId(customerId)
//        }
//        val teamMemberId = arguments?.getString(KeypadFragment.PARAM_TEAM_MEMBER_ID)
//        if (!teamMemberId.isNullOrEmpty()) {
//            builder.teamMemberId(teamMemberId)
//        }
//        val locationId = arguments?.getString(KeypadFragment.PARAM_LOCATION_ID)
//        if (!locationId.isNullOrEmpty()) {
//            builder.locationId(locationId)
//        }
//        val referenceId = arguments?.getString(KeypadFragment.PARAM_REFERENCE_ID)
//        if (!referenceId.isNullOrEmpty()) {
//            builder.referenceId(referenceId)
//        }
//        val note = arguments?.getString(KeypadFragment.PARAM_NOTE)
//        if (!note.isNullOrEmpty()) {
//            builder.note(note)
//        }
//        val statementDescription = arguments?.getString(KeypadFragment.PARAM_STATEMENT_DESCRIPTION)
//        if (!statementDescription.isNullOrEmpty()) {
//            builder.statementDescription(statementDescription)
//        }
//
//        val parameters = builder.build()
//        Log.i("demo-app", "Starting payment with parameters=$parameters")
//
//        viewModel.startPayment(parameters)
    }
}
//    fun startPayment() {
//        Log.d("TAG", "Actual payment start")
//        val builder = PaymentParameters.Builder(
//            amount = Money(20L, CurrencyCode.USD),
//            /* An idempotency key can be optionally provided by the user through manual entry. In cases
//            where the idempotency key is not provided by the user, we will generate random IdempotencyKey.
//            Manually entered IdempotencyKey is used for internal tests only!
//             */
//            idempotencyKey = UUID.randomUUID().toString(),
//        ).autocomplete(true)
//
//        /*if (!autoComplete) {
//            builder.acceptPartialAuthorization(
//                arguments?.getBoolean(KeypadFragment.PARAM_ACCEPT_PARTIAL) ?: false
//            )
//        }*/
//        //val tipAmount = arguments?.getLong(KeypadFragment.PARAM_TIP_AMOUNT)
////        if (tipAmount != null && tipAmount > 0) {
////            builder.tipMoney(Money(tipAmount, CurrencyCode.USD))
////        }
////        val feeAmount = arguments?.getLong(KeypadFragment.PARAM_FEE_AMOUNT)
////        if (feeAmount != null && feeAmount > 0) {
////            builder.appFeeMoney(Money(feeAmount, CurrencyCode.USD))
////        }
////        val delayDuration = arguments?.getLong(KeypadFragment.PARAM_DELAY_DURATION)
////        if (delayDuration != null && delayDuration > 0) {
////            builder.delayDuration(delayDuration)
////        }
//        val orderId = UUID.randomUUID().toString()
//        if (!orderId.isNullOrEmpty()) {
//            builder.orderId(orderId)
//        }
////        val customerId = arguments?.getString(KeypadFragment.PARAM_CUSTOMER_ID)
////        if (!customerId.isNullOrEmpty()) {
////            builder.customerId(customerId)
////        }
////        val teamMemberId = arguments?.getString(KeypadFragment.PARAM_TEAM_MEMBER_ID)
////        if (!teamMemberId.isNullOrEmpty()) {
////            builder.teamMemberId(teamMemberId)
////        }
////        val locationId = arguments?.getString(KeypadFragment.PARAM_LOCATION_ID)
////        if (!locationId.isNullOrEmpty()) {
////            builder.locationId(locationId)
////        }
////        val referenceId = arguments?.getString(KeypadFragment.PARAM_REFERENCE_ID)
////        if (!referenceId.isNullOrEmpty()) {
////            builder.referenceId(referenceId)
////        }
//        val note = "This is testing payment"/*arguments?.getString(KeypadFragment.PARAM_NOTE)*/
//        if (!note.isNullOrEmpty()) {
//            builder.note(note)
//        }
////        val statementDescription = arguments?.getString(KeypadFragment.PARAM_STATEMENT_DESCRIPTION)
////        if (!statementDescription.isNullOrEmpty()) {
////            builder.statementDescription(statementDescription)
////        }
//
//        val parameters = builder.build()
//      //  paymentManager.startPaymentActivity(parameters)
//    }
//}
//// MyFlutterFragment.kt
//class MyFlutterFragment : FlutterFragment() {
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        Log.d("TAG", "onCreate: my flutter fragment")
//        val fragmentManager = childFragmentManager
//        val transaction = fragmentManager.beginTransaction()
//        transaction.replace(R.id.fragment_container_view_tag, ChargeFragment())
//        transaction.commit()
//    }
//}
//
//// MyFlutterActivity.kt
//class MyFlutterActivity : FlutterFragmentActivity() {
//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//        Log.d("TAG", "onCreate:41 my flutter activity")
//        supportFragmentManager.beginTransaction()
//            .replace(android.R.id.content, MyFlutterFragment())
//            .commit()
//    }
//}
//class MyFlutterActivity : FlutterFragmentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        Log.d("TAG", "onCreate: my flutter fragment")
//        super.onCreate(savedInstanceState)
//        if (supportFragmentManager.findFragmentById(R.id.content) == null) {
//            Log.d("TAG", "onCreate: my flutter fragment 1")
//            supportFragmentManager.beginTransaction()
//                .add(R.id.content, ChargeFragment())
//                .commit()
//        }
//    }
//
//}

