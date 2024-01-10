package com.squareup.sdk.readersdk2.readers

import android.content.res.Resources
import com.squareup.sdk.reader2.cardreader.ReaderInfo.DeniedUxHint
import com.squareup.sdk.reader2.cardreader.ReaderInfo.DeniedUxHint.COUNT
import com.squareup.sdk.reader2.cardreader.ReaderInfo.DeniedUxHint.NO_SUGGESTED_ACTION
import com.squareup.sdk.reader2.cardreader.ReaderInfo.DeniedUxHint.SUGGEST_ACTIVATION
import com.squareup.sdk.reader2.cardreader.ReaderInfo.DeniedUxHint.SUGGEST_CONTACT_SUPPORT
import com.squareup.sdk.reader2.cardreader.ReaderInfo.DeniedUxHint.SUGGEST_RETRY
import com.squareup.sdk.reader2.cardreader.ReaderInfo.State
import com.squareup.sdk.readersdk2.R

// Mapping from Reader states to readable string description
private fun getDescriptionFromState(state: State) = when (state) {
    State.Ready -> R.string.state_ready
    State.Connecting -> R.string.state_connecting
    State.Disabled -> R.string.state_disabled
    is State.FailedToConnect -> R.string.state_failed
    State.UpdatingFirmware -> R.string.state_updating
    is State.Disconnected -> R.string.state_disconnected
}

fun State.asString(resources: Resources) = resources.getString(getDescriptionFromState(this))

private fun getActionFromUxHint(uxHint: DeniedUxHint, resources: Resources) = when (uxHint) {
    SUGGEST_RETRY -> resources.getString(R.string.state_failed_try_again)
    SUGGEST_ACTIVATION -> resources.getString(R.string.state_failed_activate)
    SUGGEST_CONTACT_SUPPORT -> resources.getString(R.string.state_failed_contact)
    NO_SUGGESTED_ACTION, COUNT -> ""
}

fun DeniedUxHint.asString(resources: Resources) = getActionFromUxHint(this, resources)
