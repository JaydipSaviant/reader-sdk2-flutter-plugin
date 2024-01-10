package com.squareup.sdk.readersdk2.readers

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.sdk.reader2.core.Result.Failure
import com.squareup.sdk.reader2.core.Result.Success
import com.squareup.sdk.readersdk2.R

class PairingFragment : DialogFragment() {
    private lateinit var viewModel: PairingViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewModel = ViewModelProvider(this)[PairingViewModel::class.java]
        getReaderPairingResult()
        viewModel.startPairing()
        return AlertDialog.Builder(context)
            .setView(R.layout.pairing_view)
            .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
            .create()
    }

    override fun onResume() {
        super.onResume()
        val animImage = dialog?.findViewById<View>(R.id.pairing_screen_reader_r12)
        animImage?.background?.let {
            (it as AnimationDrawable).start()
        }
    }

    private fun getReaderPairingResult() {
        viewModel.getPairingResult().observe(
            this,
            Observer { result ->
                Log.d(TAG, "LiveData: observed a new Result object")
                val toastText = when (result) {
                    is Failure -> getString(
                        R.string.pairing_fragment_failed_message,
                        result.errorMessage
                    )

                    is Success -> getString(
                        if (result.value) {
                            R.string.pairing_fragment_success_message
                        } else {
                            R.string.pairing_fragment_canceled_message
                        }
                    )
                }
                Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
                dialog?.dismiss()
            }
        )
    }

    companion object {
        const val TAG = "PairingFragment"
    }
}
