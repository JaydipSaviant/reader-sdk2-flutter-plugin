package com.squareup.sdk.readersdk2.readers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squareup.sdk.reader2.ReaderSdk
import com.squareup.sdk.reader2.cardreader.PairingHandle
import com.squareup.sdk.reader2.core.CallbackReference
import com.squareup.sdk.reader2.extensions.PairingResult

class PairingViewModel : ViewModel() {
    private var pairingCallbackReference: CallbackReference? = null
    private val readerManager = ReaderSdk.readerManager()

    private lateinit var pairingHandle: PairingHandle

    private val pairingResult = MutableLiveData<PairingResult>()

    fun getPairingResult(): LiveData<PairingResult> = pairingResult

    fun startPairing() {
        if (readerManager.isPairingInProgress) {
            // Pairing has already started
            Log.d(TAG, "Pairing is already in progress")
            return
        }

        pairingCallbackReference = readerManager.addPairingCallback {
            Log.d(TAG, "Pairing result received: $it. Propagating to observer...")
            pairingResult.postValue(it)
        }

        Log.d(TAG, "Starting pairing...")
        pairingHandle = readerManager.pairReader()
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared() called")

        if (readerManager.isPairingInProgress) {
            val stopResult = pairingHandle.stop()
            Log.d(
                TAG,
                "onCleared(): Pairing was in progress, attempted to cancel with result: $stopResult"
            )
        }

        pairingCallbackReference?.clear()
        pairingCallbackReference = null
    }

    companion object {
        const val TAG = "PairingViewModel"
    }
}
