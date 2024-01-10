package com.squareup.sdk.readersdk2.readers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.sdk.reader2.cardreader.ReaderChangedEvent
import com.squareup.sdk.reader2.cardreader.ReaderChangedEvent.Change.ADDED
import com.squareup.sdk.reader2.cardreader.ReaderChangedEvent.Change.BATTERY_CHARGING
import com.squareup.sdk.reader2.cardreader.ReaderChangedEvent.Change.BATTERY_THRESHOLD
import com.squareup.sdk.reader2.cardreader.ReaderChangedEvent.Change.CHANGED_STATE
import com.squareup.sdk.reader2.cardreader.ReaderChangedEvent.Change.FIRMWARE_PROGRESS
import com.squareup.sdk.reader2.cardreader.ReaderChangedEvent.Change.REMOVED
import com.squareup.sdk.reader2.cardreader.ReaderInfo
import com.squareup.sdk.readersdk2.R

/**
 * The "Readers" screen is used for reader pairing and viewing status of existing readers
 */
class ReadersFragment : Fragment(R.layout.readers_fragment) {

    private var listAdapter: ReaderListAdapter? = null
    private lateinit var viewModel: ReadersViewModel

    override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?,
    ): View? {
        viewModel = ViewModelProvider(this)[ReadersViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(
      view: View,
      savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = ReaderListAdapter(::onCardReaderClicked)

        // Init Recycler view
        view.findViewById<RecyclerView>(R.id.reader_list).apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
            adapter = listAdapter
            // Initialize list with latest readers snapshot
            listAdapter?.data = viewModel.getReaders()
        }

        // Init 'Connect reader' button
        view.findViewById<View>(R.id.connect_button).setOnClickListener {
            findNavController().navigate(R.id.pairing_dest)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.observeReaderChanges(::updateList)
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopObserving()
    }

    override fun onDestroyView() {
        // Clear references to the layout to prevent leaking it
        listAdapter = null
        super.onDestroyView()
    }

    /**
     * Updates data inside the reader list and dispatches proper update event
     */
    private fun updateList(change: ReaderChangedEvent) {
        val adapter = listAdapter ?: error("Update list called after adapter was reset!")

        // Note: normally, one would use RecyclerView's DiffUtil to calculate the difference between lists
        // and dispatch a proper update to recycler view. But here we want to show how the result
        // of a ReaderChanged callback can be used

        // First, find at which positions reader was in the list before and after the change
        // In case of add/remove events, one of these positions would be -1, and it's fine
        val oldPosition = adapter.data.indexOfReader(change.reader)
        val newPosition = viewModel.getReaders().indexOfReader(change.reader)

        // Update data in the recycler view's adapter with the latest snapshot
        adapter.data = viewModel.getReaders()

        // Dispatch an update based on a type of change that happened
        when (change.change) {
            ADDED ->
                adapter.notifyItemInserted(newPosition)

            CHANGED_STATE, BATTERY_THRESHOLD, BATTERY_CHARGING, FIRMWARE_PROGRESS ->
                adapter.notifyItemChanged(oldPosition)

            REMOVED ->
                adapter.notifyItemRemoved(oldPosition)
        }
    }

    /**
     * Looks up a particular reader inside a list of readers and returns its index
     */
    private fun List<ReaderInfo>.indexOfReader(reader: ReaderInfo) =
        indexOfFirst { it.id == reader.id }

    /**
     * Called when the user taps on a specific card reader, to request details on that one.
     */
    private fun onCardReaderClicked(reader: ReaderInfo) {
        findNavController().navigate(
            R.id.action_readers_to_details,
            Bundle().apply { putString(ReaderDetailsFragment.EXTRA_KEY, reader.id) }
        )
    }
}
