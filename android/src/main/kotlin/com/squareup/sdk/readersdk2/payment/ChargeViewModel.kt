package com.squareup.sdk.readersdk2.payment

import android.content.Context
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squareup.sdk.reader2.ReaderSdk
import com.squareup.sdk.reader2.cardreader.CardEntryMethod
import com.squareup.sdk.reader2.core.CallbackReference
import com.squareup.sdk.reader2.core.ErrorDetails
import com.squareup.sdk.reader2.core.Result
import com.squareup.sdk.reader2.extensions.PaymentResult
import com.squareup.sdk.reader2.payment.AlternatePaymentMethod
import com.squareup.sdk.reader2.payment.Payment
import com.squareup.sdk.reader2.payment.PaymentHandle
import com.squareup.sdk.reader2.payment.PaymentParameters

class ChargeViewModel : ViewModel() {
    private val paymentResult = MutableLiveData<PaymentResult>()
    private val cardEntryMethods = MutableLiveData<Set<CardEntryMethod>>()
    private var paymentHandler: PaymentHandle? = null
    var paymentInProgress = false
    private val paymentManager = ReaderSdk.paymentManager()
    private var callbacks = mutableListOf<CallbackReference>()
    private var alertDialog: AlertDialog? = null
    private lateinit var context: Context

    fun getPaymentResult(): LiveData<PaymentResult> = paymentResult

    fun getCardEntryMethods(): LiveData<Set<CardEntryMethod>> = cardEntryMethods

    init {
        // Set initial value to a snapshot of available input methods
        cardEntryMethods.value = paymentManager.getAvailableCardEntryMethods()

        callbacks.add(paymentManager.addAvailableCardEntryMethodChangedCallback {
            cardEntryMethods.postValue(
                it
            )
        })

        callbacks.add(paymentManager.addPaymentCallback {
            paymentInProgress = false
            paymentResult.postValue(it)
        })
    }

    fun getPaymentMethods(): List<AlternatePaymentMethod> {
        check(paymentInProgress)
        return paymentHandler!!.alternateMethods
    }

    fun startPayment(parameters: PaymentParameters) {
        if (paymentInProgress) return
        paymentInProgress = true
        Log.d("TAG", "startPayment:  51 $paymentInProgress")
        paymentInProgress = true
        Log.d("TAG", "startPayment:  52 $paymentInProgress")
        paymentHandler = paymentManager.startPaymentActivity(parameters)
        Log.d("TAG", "startPayment:  53 $paymentHandler")
//        paymentManager.addPaymentCallback { result: PaymentResult ->
//            when (result) {
//                is Result.Success -> {
//                    //showChargeSuccessDialog(result.value)
//                    paymentResults = result.value.toString()
//                    Log.d("TAG", "startPayment: 71 -- ${result.value}")
//                    Log.d("TAG", "startPayment: 711 -- $paymentResults")
//
//                }
//                is Result.Failure -> {
//                    paymentResults = result.errorMessage
//                    Log.d("TAG", "startPayment: 74 -- ${result.errorMessage}")
//                    Log.d("TAG", "startPayment: 744 -- $paymentResults")
//                }
//            }
//
//            Log.d("TAG", "startPayment: -- ${result}")
//        }
        Log.d("TAG", "startPayment: 103 - ")
        // return paymentResults
    }

    fun cancel() {
        if (!paymentInProgress) return
        paymentHandler?.cancel() // triggers callbacks, which close the fragment
    }

    override fun onCleared() {
        callbacks.forEach { it.clear() }
        paymentInProgress = false
        paymentHandler?.cancel()
        paymentHandler = null
    }

}
