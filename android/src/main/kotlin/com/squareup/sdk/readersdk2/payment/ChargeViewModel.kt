package com.squareup.sdk.readersdk2.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squareup.sdk.reader2.ReaderSdk
import com.squareup.sdk.reader2.cardreader.CardEntryMethod
import com.squareup.sdk.reader2.core.CallbackReference
import com.squareup.sdk.reader2.extensions.PaymentResult
import com.squareup.sdk.reader2.payment.AlternatePaymentMethod
import com.squareup.sdk.reader2.payment.PaymentHandle
import com.squareup.sdk.reader2.payment.PaymentParameters

class ChargeViewModel : ViewModel() {
    private val paymentResult = MutableLiveData<PaymentResult>()
    private val cardEntryMethods = MutableLiveData<Set<CardEntryMethod>>()
    private var paymentHandler: PaymentHandle? = null
    var paymentInProgress = false
    private val paymentManager = ReaderSdk.paymentManager()
    private var callbacks = mutableListOf<CallbackReference>()


    fun getPaymentResult(): LiveData<PaymentResult> = paymentResult

    fun getCardEntryMethods(): LiveData<Set<CardEntryMethod>> = cardEntryMethods

    init {
        // Set initial value to a snapshot of available input methods
        cardEntryMethods.value = paymentManager.getAvailableCardEntryMethods()

        callbacks.add(
            paymentManager.addAvailableCardEntryMethodChangedCallback {
                cardEntryMethods.postValue(
                    it
                )
            }
        )

        callbacks.add(
            paymentManager.addPaymentCallback {
                paymentInProgress = false
                paymentResult.postValue(it)
            }
        )
    }


    fun getPaymentMethods(): List<AlternatePaymentMethod> {
        check(paymentInProgress)
        return paymentHandler!!.alternateMethods
    }

    fun startPayment(parameters: PaymentParameters) {
        if (paymentInProgress) return
        paymentInProgress = true
        paymentHandler = paymentManager.startPaymentActivity(parameters)
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
