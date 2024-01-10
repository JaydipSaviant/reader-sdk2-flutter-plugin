package com.squareup.sdk.readersdk2.readers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.sdk.reader2.cardreader.ReaderInfo
import com.squareup.sdk.reader2.cardreader.ReaderInfo.Model.MAGSTRIPE
import com.squareup.sdk.reader2.cardreader.ReaderInfo.State.Disconnected
import com.squareup.sdk.reader2.cardreader.ReaderInfo.State.FailedToConnect
import com.squareup.sdk.readersdk2.R
import com.squareup.sdk.readersdk2.readers.ReaderListAdapter.ViewHolder


class ReaderListAdapter(
  private val clickHandler: (ReaderInfo) -> Unit,
) : RecyclerView.Adapter<ViewHolder>() {

    var data: List<ReaderInfo> = emptyList()

    /** A view holder, which "holds" a single reader_in_list LinearLayout view  */
    inner class ViewHolder constructor(v: View) : RecyclerView.ViewHolder(v) {

        fun bind(reader: ReaderInfo) {
            with(itemView) {
                findViewById<TextView>(R.id.reader_name).setReaderName(reader)
                findViewById<TextView>(R.id.state).setState(reader)
                setOnClickListener { clickHandler(reader) }
            }
        }
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(
      parent: ViewGroup,
      viewType: Int,
    ) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.reader_in_list, parent, false)
    )

    override fun onBindViewHolder(viewHolder: ViewHolder, index: Int) {
        viewHolder.bind(data[index])
    }
}

private fun TextView.setReaderName(reader: ReaderInfo) {
    text = resources.getString(
        if (reader.model == MAGSTRIPE) R.string.magstripe else R.string.contactless_chip
    )
}

/**
 * Sets state text to be State + Charge percent/update percent
 */
private fun TextView.setState(reader: ReaderInfo) {
    val state = reader.state.asString(resources)
    when {
        reader.firmwarePercent != null -> {
            text = resources.getString(R.string.state_updating, reader.firmwarePercent)
        }

        reader.batteryStatus != null -> {
            val batteryChargeState = resources.getString(
                when {
                    reader.batteryStatus!!.isCharging -> R.string.battery_plugged_in
                    else -> R.string.battery_not_plugged_in
                }
            )
            text = resources.getString(
                R.string.battery_percent_info_with_state,
                state,
                batteryChargeState,
                reader.batteryStatus?.percent
            )
        }

        else -> {
            text = state
        }
    }

    // If error occurred - change state color to red
    val color = ContextCompat.getColor(
        context,
        if (reader.state is FailedToConnect) R.color.red_error else R.color.black_55p
    )
    setTextColor(color)
}

/**
 * Helper method to get a battery icon based on charging state and percentage
 */
@VisibleForTesting
fun ReaderInfo.getBatteryDrawable(): Int {
    val battery = batteryStatus
    return when {
        battery == null || state is Disconnected -> R.drawable.battery_none
        battery.isCharging -> when {
            battery.percent <= 25 -> R.drawable.battery_low_charging
            battery.percent < 50 -> R.drawable.battery_half_charging
            battery.percent < 75 -> R.drawable.battery_high_charging
            battery.percent <= 100 -> R.drawable.battery_full_charging
            else -> R.drawable.battery_none
        }

        !battery.isCharging -> when {
            battery.percent <= 10 -> R.drawable.battery_critical
            battery.percent <= 25 -> R.drawable.battery_very_low
            battery.percent < 50 -> R.drawable.battery_low
            battery.percent < 75 -> R.drawable.battery_half
            battery.percent < 100 -> R.drawable.battery_high
            battery.percent == 100 -> R.drawable.battery_full
            else -> R.drawable.battery_none
        }

        else -> R.drawable.battery_none
    }
}
