package com.squareup.sdk.readersdk2.readers

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.sdk.reader2.cardreader.CardEntryMethod
import com.squareup.sdk.reader2.cardreader.ReaderChangedEvent.Change.REMOVED
import com.squareup.sdk.reader2.cardreader.ReaderInfo
import com.squareup.sdk.reader2.cardreader.ReaderInfo.ConnectionError.BluetoothBondingFailure
import com.squareup.sdk.reader2.cardreader.ReaderInfo.ConnectionError.ConnectedButNotReady
import com.squareup.sdk.reader2.cardreader.ReaderInfo.Model
import com.squareup.sdk.reader2.cardreader.ReaderInfo.Model.CONTACTLESS_AND_CHIP
import com.squareup.sdk.reader2.cardreader.ReaderInfo.Model.MAGSTRIPE
import com.squareup.sdk.reader2.cardreader.ReaderInfo.State
import com.squareup.sdk.readersdk2.R

class ReaderDetailsFragment : Fragment(R.layout.reader_details_fragment) {
    private lateinit var viewModel: ReadersViewModel
    private var readerId: String? = null

    override fun onViewCreated(
      view: View,
      savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReadersViewModel::class.java)

        // Extract reader ID from the arguments and find the corresponding reader
        readerId = arguments?.getString(EXTRA_KEY)
        val reader = if (readerId.isNullOrEmpty()) null else viewModel.getReader(readerId!!)

        // If reader is found - update the UI to display its details
        reader?.let { updateReaderDetails(it) }
    }

    override fun onResume() {
        super.onResume()
        // Start observing reader changes to update the UI when the current reader's state changes
        viewModel.observeReaderChanges {
            if (it.reader.id == readerId) {
                if (it.change == REMOVED) {
                    findNavController().popBackStack()
                } else {
                    updateReaderDetails(it.reader)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopObserving()
    }

    private fun updateReaderDetails(reader: ReaderInfo) {
        activity?.title = reader.model.asString()

        with(requireView()) {
            refreshStatus(reader)
            refreshConnection(reader)
            refreshAccepts(reader)
            refreshBattery(reader)
            refreshFirmware(reader)
            refreshSerial(reader)
            refreshButtons(reader)
        }
    }

    private fun View.refreshStatus(reader: ReaderInfo) {
        findViewById<TextView>(R.id.status).text = reader.state.asString(resources)

        findViewById<TextView>(R.id.denied_message).apply {
            val state = reader.state
            text = when (state) {
                is State.FailedToConnect -> "${state.asString(resources)}. " +
                        if (state.reason?.localizedTitle != null &&
                            state.reason!!.localizedTitle.isNotEmpty()
                        ) {
                            "${state.reason?.localizedTitle}: ${state.reason?.localizedDescription}"
                        } else {
                            ""
                        }

                is State.Disconnected -> {
                    val error = state.error
                    "${state.asString(resources)}. " +
                            when (error) {
                                is BluetoothBondingFailure ->
                                    when (error?.bondError) {
                                        UNBOND_REASON_AUTH_CANCELED -> resources.getString(R.string.bluetooth_auth_canceled)
                                        UNBOND_REASON_AUTH_DISCOVERY_IN_PROGRESS ->
                                            resources.getString(R.string.bluetooth_auth_discovery_in_progress)

                                        UNBOND_REASON_AUTH_FAILED -> resources.getString(R.string.bluetooth_auth_failed)
                                        UNBOND_REASON_AUTH_REJECTED -> resources.getString(R.string.bluetooth_auth_rejected)
                                        UNBOND_REASON_AUTH_REPEATED_ATTEMPTS ->
                                            resources.getString(R.string.bluetooth_repeated_attempts)

                                        UNBOND_REASON_AUTH_TIMEOUT -> resources.getString(R.string.bluetooth_timeout)
                                        UNBOND_REASON_REMOTE_AUTH_CANCELED ->
                                            resources.getString(R.string.bluetooth_remote_auth_canceled)

                                        UNBOND_REASON_REMOTE_DEVICE_DOWN ->
                                            resources.getString(R.string.bluetooth_remote_device_down)

                                        UNBOND_REASON_REMOVED -> resources.getString(R.string.bluetooth_removed)
                                        null -> resources.getString(R.string.no_bluetooth_code)
                                        else -> resources.getString(
                                            R.string.unknown_bluetooth_code,
                                            error.bondError
                                        )
                                    }

                                ConnectedButNotReady -> resources.getString(R.string.unknown_disconnected)
                                null -> ""
                            }
                }

                else -> {
                    ""
                }
            }
            isVisible = (text != "")
        }
    }

    private fun View.refreshConnection(reader: ReaderInfo) {
        findViewById<TextView>(R.id.connection).text =
            resources.getString(
                if (reader.model == MAGSTRIPE) {
                    R.string.connection_audio_label
                } else {
                    R.string.connection_bluetooth_label
                }
            )
    }

    private fun View.refreshAccepts(reader: ReaderInfo) {
        findViewById<TextView>(R.id.accepts).text =
            reader.supportedCardEntryMethods.joinToString { it.asString() }
    }

    private fun View.refreshBattery(reader: ReaderInfo) {
        val battery = reader.batteryStatus
        if (battery != null) {
            val charging = if (battery.isCharging) getString(R.string.charging_text) else ""

            findViewById<TextView>(R.id.battery).text =
                resources.getString(R.string.battery_percent_info, charging, battery.percent)
            showChildren(R.id.battery, R.id.battery_label, R.id.battery_divider)
        } else {
            hideChildren(R.id.battery, R.id.battery_label, R.id.battery_divider)
        }
    }

    private fun View.refreshFirmware(reader: ReaderInfo) {
        if (reader.firmwareVersion != null) {
            findViewById<TextView>(R.id.firmware).text = reader.firmwareVersion
            showChildren(R.id.firmware, R.id.firmware_label, R.id.firmware_divider)
        } else {
            hideChildren(R.id.firmware, R.id.firmware_label, R.id.firmware_divider)
        }
    }

    private fun View.refreshSerial(reader: ReaderInfo) {
        if (reader.serialNumber != null) {
            findViewById<TextView>(R.id.serial_number).text = reader.serialNumber
            showChildren(R.id.serial_number, R.id.serial_number_label, R.id.serial_number_divider)
        } else {
            hideChildren(R.id.serial_number, R.id.serial_number_label, R.id.serial_number_divider)
        }
    }

    private fun View.refreshButtons(reader: ReaderInfo) {
        findViewById<Button>(R.id.identify_button).apply {
            isVisible = reader.canBlink
            setOnClickListener { viewModel.blink(reader) }
        }

        findViewById<Button>(R.id.forget_button).apply {
            isVisible = reader.canBeForgotten
            setOnClickListener { viewModel.forget(reader) }
        }
    }

    private fun View.showChildren(vararg ids: Int) =
        ids.forEach { findViewById<View>(it).isVisible = true }

    private fun View.hideChildren(vararg ids: Int) =
        ids.forEach { findViewById<View>(it).isVisible = false }

    // Mappings from Card Entry Method / Model to a readable string representation
    private val cardEntryMethods = mapOf(
        CardEntryMethod.SWIPED to R.string.card_entry_method_swipe,
        CardEntryMethod.CONTACTLESS to R.string.card_entry_method_contactless,
        CardEntryMethod.EMV to R.string.card_entry_method_chip
    )

    private val models = mapOf(
        MAGSTRIPE to R.string.magstripe,
        CONTACTLESS_AND_CHIP to R.string.contactless_chip
    )

    private fun CardEntryMethod.asString() = resources.getString(cardEntryMethods[this]!!)
    private fun Model.asString() = resources.getString(models[this]!!)

    companion object {
        const val EXTRA_KEY = "cardReaderId"

        /**
         * These UNBOND_REASON_* codes are cribbed from BluetoothDevice or from the deprecated
         * androidthings, but seem not to be actually exposed anywhere official.
         */
        const val UNBOND_REASON_AUTH_CANCELED = 3
        const val UNBOND_REASON_AUTH_DISCOVERY_IN_PROGRESS = 5
        const val UNBOND_REASON_AUTH_FAILED = 1
        const val UNBOND_REASON_AUTH_REJECTED = 2
        const val UNBOND_REASON_AUTH_REPEATED_ATTEMPTS = 7
        const val UNBOND_REASON_AUTH_TIMEOUT = 6
        const val UNBOND_REASON_REMOTE_DEVICE_DOWN = 4
        const val UNBOND_REASON_REMOTE_AUTH_CANCELED = 8
        const val UNBOND_REASON_REMOVED = 9
    }
}
