package com.squareup.sdk.readersdk2.readers

import androidx.lifecycle.ViewModel
import com.squareup.sdk.reader2.ReaderSdk
import com.squareup.sdk.reader2.cardreader.ReaderChangedEvent
import com.squareup.sdk.reader2.cardreader.ReaderInfo
import com.squareup.sdk.reader2.core.CallbackReference

class ReadersViewModel : ViewModel() {
    private val readerManager = ReaderSdk.readerManager()
    private var callbackReference: CallbackReference? = null

    fun observeReaderChanges(callback: (ReaderChangedEvent) -> Unit) {
        callbackReference =
            readerManager.addReaderChangedCallback { callback(it) }
    }

    fun stopObserving() {
        callbackReference?.clear()
        callbackReference = null
    }

    fun getReaders(): List<ReaderInfo> = readerManager.getReaders()

    fun getReader(id: String) = readerManager.getReader(id)

    fun blink(reader: ReaderInfo) = readerManager.blink(reader)

    fun forget(reader: ReaderInfo) = readerManager.forget(reader)

    override fun onCleared() {
        super.onCleared()
        callbackReference?.clear()
        callbackReference = null
    }
}
