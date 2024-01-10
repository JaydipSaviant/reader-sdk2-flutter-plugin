package com.squareup.sdk.readersdk2.payment

import android.text.Editable
import android.text.TextWatcher

class AmountFormatter(private val onNewAmount: ((Long) -> Unit)? = null) : TextWatcher {

    private var programmaticChange = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(sequence: Editable?) {
        if (programmaticChange) {
            return // don't mess with the programmatically recomputed string value
        }
        try {
            programmaticChange = true
            val asLong = parseAmount(sequence?.toString())
            sequence?.replace(0, sequence.length, formatAmount(asLong))
            onNewAmount?.invoke(asLong)
        } finally {
            programmaticChange = false
        }
    }
}