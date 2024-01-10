package com.squareup.sdk.readersdk2.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.sdk.readersdk2.R

class KeypadFragment : Fragment(R.layout.keypad_fragment) {

    companion object {
        const val PARAM_BASE_AMOUNT = "base_amount"
        const val PARAM_TIP_AMOUNT = "tip_amount"
        const val PARAM_FEE_AMOUNT = "app_fee_amount"
        const val PARAM_AUTOCOMPLETE = "autocomplete"
        const val PARAM_ACCEPT_PARTIAL = "accept_partial_authorization"
        const val PARAM_CUSTOMER_ID = "customer_id"
        const val PARAM_DELAY_DURATION = "delay_duration"
        const val PARAM_LOCATION_ID = "location_id"
        const val PARAM_NOTE = "note"
        const val PARAM_ORDER_ID = "order_id"
        const val PARAM_REFERENCE_ID = "reference_id"
        const val PARAM_STATEMENT_DESCRIPTION = "statement_description"
        const val PARAM_TEAM_MEMBER_ID = "team_member_id"
        const val IDEMPOTENCY_KEY = "idempotency_key"
    }

    private var amountText: TextView? = null
    private var banner: TextView? = null
    private var chargeButton: Button? = null
    private var expandOptionsButton: ImageButton? = null
    private var tipAmountText: TextView? = null
    private var tipAdvice: TextView? = null
    private var feeAdvice: TextView? = null
    private lateinit var viewModel: KeypadViewModel
    private var currentAmount = 0L
    private var tipAmount = 0L

    private fun appendDigit(digit: Int): (View) -> Unit = { viewModel.appendDigit(digit) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel = ViewModelProvider(this)[KeypadViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            chargeButton = findViewById(R.id.charge)
            amountText = findViewById(R.id.amount)
            expandOptionsButton = findViewById(R.id.expand_options_button)
            tipAmountText = findViewById(R.id.tip_money)
            tipAdvice = findViewById(R.id.tip_advice)
            feeAdvice = findViewById(R.id.fee_advice)
            banner = findViewById(R.id.firmware_banner)

            findViewById<Button>(R.id.keypad_1).setOnClickListener(appendDigit(1))
            findViewById<Button>(R.id.keypad_2).setOnClickListener(appendDigit(2))
            findViewById<Button>(R.id.keypad_3).setOnClickListener(appendDigit(3))
            findViewById<Button>(R.id.keypad_4).setOnClickListener(appendDigit(4))
            findViewById<Button>(R.id.keypad_5).setOnClickListener(appendDigit(5))
            findViewById<Button>(R.id.keypad_6).setOnClickListener(appendDigit(6))
            findViewById<Button>(R.id.keypad_7).setOnClickListener(appendDigit(7))
            findViewById<Button>(R.id.keypad_8).setOnClickListener(appendDigit(8))
            findViewById<Button>(R.id.keypad_9).setOnClickListener(appendDigit(9))
            findViewById<Button>(R.id.keypad_0).setOnClickListener(appendDigit(0))

            findViewById<Button>(R.id.keypad_clear).setOnClickListener {
                viewModel.clear()
            }

            // The optional field amounts uses the normal Android numeric keypad, but we don't want arbitrary
            // decimals, so we use an integer input and TextWatchers to enforce it.
            tipAmountText?.addTextChangedListener(
                AmountFormatter { newAmount ->
                    viewModel.setTip(newAmount)
                }
            )
            findViewById<TextView>(R.id.fee_money).addTextChangedListener(AmountFormatter())

            chargeButton?.setOnClickListener {
                val bundle = Bundle().apply {
                    putLong(PARAM_BASE_AMOUNT, currentAmount)
                    putLong(PARAM_TIP_AMOUNT, tipAmount)
                    putLong(
                        PARAM_FEE_AMOUNT,
                        parseAmount(findViewById<TextView>(R.id.fee_money).text.toString())
                    )
                    putBoolean(PARAM_AUTOCOMPLETE, findViewById<CheckBox>(R.id.complete).isChecked)
                    putBoolean(
                        PARAM_ACCEPT_PARTIAL,
                        findViewById<CheckBox>(R.id.accept_partial_auth).isChecked
                    )
                    putString(PARAM_ORDER_ID, findViewById<TextView>(R.id.order_id).text.toString())
                    putString(
                        PARAM_CUSTOMER_ID,
                        findViewById<TextView>(R.id.customer_id).text.toString()
                    )
                    putString(
                        PARAM_TEAM_MEMBER_ID,
                        findViewById<TextView>(R.id.team_member_id).text.toString()
                    )
                    putString(
                        PARAM_LOCATION_ID,
                        findViewById<TextView>(R.id.location_id).text.toString()
                    )
                    putString(
                        PARAM_REFERENCE_ID,
                        findViewById<TextView>(R.id.reference_id).text.toString()
                    )
                    putString(PARAM_NOTE, findViewById<TextView>(R.id.note).text.toString())
                    putString(
                        PARAM_STATEMENT_DESCRIPTION,
                        findViewById<TextView>(R.id.statement_description).text.toString()
                    )
                    putString(
                        IDEMPOTENCY_KEY,
                        findViewById<TextView>(R.id.idempotency_key).text.toString()
                    )
                    putLong(
                        PARAM_DELAY_DURATION,
                        parseAmount(findViewById<TextView>(R.id.delay_duration).text.toString())
                    )
                }
                findNavController().navigate(R.id.action_keypad_to_charge, bundle)
            }

            expandOptionsButton?.setOnClickListener {
                findViewById<DrawerLayout>(R.id.drawer).openDrawer(GravityCompat.END)
            }

            // Update displayed amount text in response to the value change in the viewmodel
            viewModel.getAmount().observe(
                viewLifecycleOwner
            ) { amount ->
                amountText?.text = formatAmount(amount)
                updateAmountUi(amount)
                chargeButton?.isEnabled = amount != 0L
                currentAmount = amount
                updateAdvice()
            }
            viewModel.getTip().observe(
                viewLifecycleOwner
            ) { amount ->
                tipAmount = amount
                updateAdvice()
            }

            // Display-and-update, or hide, blocking firmware update warning
            viewModel.getUpdateStatus().observe(
                viewLifecycleOwner
            ) { status ->
                when (status) {
                    KeypadViewModel.UpdateStatus.NotUpdating -> banner?.visibility = View.GONE
                    is KeypadViewModel.UpdateStatus.BlockingUpdate -> {
                        banner?.text = context.getString(R.string.firmware_message, status.percent)
                        banner?.visibility = View.VISIBLE
                    }

                    else -> {}
                }
            }

            findViewById<Button>(R.id.crash_now).setOnClickListener {
                throw SimulatedCrash("user generated a simulated crash")
            }
            findViewById<Button>(R.id.anr_now).setOnClickListener {
                Thread.sleep(10000)
            }
        }
    }

    private class SimulatedCrash(override val message: String) : RuntimeException(message)


    override fun onDestroyView() {
        // Clear references to the layout to prevent leaking it
        amountText = null
        banner = null
        chargeButton = null
        expandOptionsButton = null
        tipAmountText = null
        tipAdvice = null
        feeAdvice = null
        super.onDestroyView()
    }

    private fun updateAmountUi(amount: Long) {
        amountText?.setTextColor(
            ResourcesCompat.getColor(
                resources,
                if (amount == 0L) {
                    R.color.black_30p
                } else {
                    R.color.black_90p
                },
                context?.theme
            )
        )
    }

    private fun updateAdvice() {
        val percent = if (currentAmount == 0L) 0.0 else tipAmount * 100.0 / currentAmount
        tipAdvice?.text = getString(R.string.tip_advice_format, percent, currentAmount / 100.0)
        feeAdvice?.text = getString(R.string.fee_advice_format, 0.009 * (tipAmount + currentAmount))
    }

}
