package com.squareup.sdk.readersdk2.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squareup.sdk.reader2.ReaderSdk
import com.squareup.sdk.reader2.cardreader.ReaderChangedEvent
import com.squareup.sdk.reader2.cardreader.ReaderInfo
import com.squareup.sdk.reader2.core.CallbackReference
import kotlin.math.min

class KeypadViewModel : ViewModel() {


    private var amount = MutableLiveData<Long>()
    private var tip = MutableLiveData<Long>()
    private var updateStatus = MutableLiveData<UpdateStatus>()
    private val readerChangedCallbackRef: CallbackReference


    sealed class UpdateStatus {
        object NotUpdating : UpdateStatus()
        data class BlockingUpdate(val percent: Int) : UpdateStatus()
    }

    fun getAmount(): LiveData<Long> = amount
    fun getTip(): LiveData<Long> = tip
    fun getUpdateStatus(): LiveData<UpdateStatus> = updateStatus

    init {
        amount.value = 100L
        tip.value = 0L
        val readerManager = ReaderSdk.readerManager()
        readerChangedCallbackRef = readerManager.addReaderChangedCallback { readerChanged(it) }
        updateStatus.value = getInitialUpdateStatus(readerManager.getReaders())
    }

    fun clear() {
        amount.value = 0
    }

    fun appendDigit(digit: Int) {
        amount.value = min(amount.value!! * 10 + digit, 999_999_999)
    }

    fun setTip(newTip: Long) {
        tip.value = newTip
    }

    override fun onCleared() {
        readerChangedCallbackRef.clear()
    }

    private fun readerChanged(changeEvent: ReaderChangedEvent) {
        // Hide the banner if we aren't updating at all, or if we aren't doing a blocking update
        val percent = changeEvent.reader.firmwarePercent
        if (percent == null || changeEvent.reader.state != ReaderInfo.State.UpdatingFirmware) {
            updateStatus.value = UpdateStatus.NotUpdating
            return
        }
        // Show and update the firmware banner if we are updating
        if (changeEvent.change == ReaderChangedEvent.Change.FIRMWARE_PROGRESS) {
            updateStatus.value = UpdateStatus.BlockingUpdate(percent)
        }
    }

    private fun getInitialUpdateStatus(readers: List<ReaderInfo>): UpdateStatus {
        var result: Int? = null
        for (reader in readers) {
            val percent = reader.firmwarePercent
            if (percent != null) {
                if (result == null || percent < result) {
                    result = reader.firmwarePercent
                }
            }
        }
        return if (result == null) UpdateStatus.NotUpdating else UpdateStatus.BlockingUpdate(result)
    }

}
