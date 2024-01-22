package com.squareup.sdk.readersdk2.payment

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.text.toHtml
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.sdk.reader2.cardreader.CardEntryMethod
import com.squareup.sdk.reader2.core.ErrorDetails
import com.squareup.sdk.reader2.core.Result
import com.squareup.sdk.reader2.payment.AlternatePaymentMethod
import com.squareup.sdk.reader2.payment.CurrencyCode
import com.squareup.sdk.reader2.payment.ExternalPaymentDetails
import com.squareup.sdk.reader2.payment.ExternalTenderType
import com.squareup.sdk.reader2.payment.Money
import com.squareup.sdk.reader2.payment.Payment
import com.squareup.sdk.reader2.payment.PaymentParameters
import com.squareup.sdk.readersdk2.R
import java.util.Locale
import java.util.UUID


class ChargeFragment : Fragment(R.layout.charge_fragment) {

//    private lateinit var viewModel: ChargeViewModel
//
//    // ... other class members ...
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this)[ChargeViewModel::class.java]
//        // Other initialization logic...
//    }

    private var viewModel: ChargeViewModel  = ViewModelProvider(this)[ChargeViewModel::class.java]

    private val baseAmount: Long by lazy {
        arguments?.getLong(KeypadFragment.PARAM_BASE_AMOUNT) ?: 0
    }

    private val tipAmount: Long by lazy {
        arguments?.getLong(KeypadFragment.PARAM_TIP_AMOUNT) ?: 0
    }

    private val totalAmount: Long by lazy {
        baseAmount + tipAmount
    }

    private var alertDialog: AlertDialog? = null

    private lateinit var selectedExternalType: ExternalTenderType

    private val externalPaymentMap = mutableMapOf<String, ExternalTenderType>()

    private val externalPayments = mutableListOf<String>()

    private val customerId: String by lazy {
        arguments?.getString(KeypadFragment.PARAM_CUSTOMER_ID) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel = ViewModelProvider(this)[ChargeViewModel::class.java]
        Log.d("TAG", "FlutterOnCreate2: uiuiui${viewModel}")
        // Handle result of the payment: success or failure
        viewModel.getPaymentResult().observe(
            viewLifecycleOwner
        ) { result ->
            findNavController().popBackStack()
            dismissDialog()
            when (result) {
                is Result.Success -> showChargeSuccessDialog(result.value)
                is Result.Failure -> {
                    Log.e("TAG", "Payment error! $result")
                    showChargeFailureDialog(result.errorMessage, result.details)
                }
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        // Update UI if card methods change
        viewModel.getCardEntryMethods()
            .observe(
                viewLifecycleOwner
            ) { cardEntryMethodsSet ->
                updatePromptUi(cardEntryMethodsSet, view)
            }
        view.findViewById<TextView>(R.id.prompt_amount).text = formatAmount(baseAmount + tipAmount)
        val breakdown = view.findViewById<TextView>(R.id.prompt_breakdown)
        Log.d("TAG", "FlutterOnCreate1: uiuiui${breakdown}")
        if (tipAmount > 0) {
            breakdown.text = getString(
                R.string.breakdown_format,
                formatAmount(baseAmount),
                formatAmount(tipAmount)
            )
            breakdown.visibility = View.VISIBLE
        } else {
            breakdown.visibility = View.GONE
        }
        view.findViewById<ImageView>(R.id.back_button).setOnClickListener {
            viewModel.cancel()
        }
        startPayment()

        // We don't know other methods until here, after start.
        val otherMethodHolder = view.findViewById<ViewGroup>(R.id.other_method_list)
        for (method in viewModel.getPaymentMethods()) {
            // A customer ID is required for card-on-file
            if (method is AlternatePaymentMethod.CardOnFileEntryMethod && customerId.isBlank()) {
                continue
            } else {
                val button = Button(this.activity, null, 0, R.style.CardDetailsManuallyText)
                button.text = this.activity?.resources?.getString(method.label)
                otherMethodHolder.addView(button)
                val listener = when (method) {
                    is AlternatePaymentMethod.ManualEntryMethod -> { _: View ->
                        method.trigger()
                    }

                    is AlternatePaymentMethod.QrScanMethod -> { _: View ->
                        method.trigger(method.tag as AlternatePaymentMethod.PaymentMethodTag.QrScanMethodTag)
                    }

                    is AlternatePaymentMethod.CashEntryMethod -> { _: View ->
                        method.toDialog(R.layout.cash_payment_dialog, ::connectCashViews)
                    }

                    is AlternatePaymentMethod.ExternalEntryMethod -> { _: View ->
                        method.toDialog(
                            R.layout.external_payment_dialog,
                            ::connectExternalTenderViews
                        )
                    }

                    is AlternatePaymentMethod.CardOnFileEntryMethod -> { _: View ->
                        method.toDialog(
                            R.layout.card_on_file_payment_dialog,
                            ::connectCardOnFileViews
                        )
                    }

                    else -> { _: View -> }
                }
                button.setOnClickListener(listener)
            }
        }
    }

    private fun connectCardOnFileViews(
        view: View,
        builder: AlertDialog.Builder,
        method: AlternatePaymentMethod,
    ) {
        check(method is AlternatePaymentMethod.CardOnFileEntryMethod) {
            "This method should only be used for card on file payments"
        }
        val cardIdEditText = view.findViewById<EditText>(R.id.card_on_file_source_id)

        builder.apply {
            setTitle(R.string.card_on_file_payment_dialog_title)
            setPositiveButton(android.R.string.ok) { _, _ ->
                val cardId = cardIdEditText.text.toString()
                method.trigger(cardId, customerId)
            }
        }
    }

    private fun connectExternalTenderViews(
        view: View,
        builder: AlertDialog.Builder,
        method: AlternatePaymentMethod,
    ) {
        check(method is AlternatePaymentMethod.ExternalEntryMethod) {
            "This method should only be used for external payments"
        }

        val externalPaymentSpinner = view.findViewById<Spinner>(R.id.external_payment_spinner)
        val externalSourceEditText = view.findViewById<EditText>(R.id.external_source_edit_text)

        for (tender in ExternalTenderType.values()) {
            val tenderName = tender.name
                .lowercase()
                .split("_")
                .joinToString(" ") { tenderName ->
                    tenderName.replaceFirstChar { it.titlecase(Locale.ROOT) }
                }
            externalPaymentMap[tenderName] = tender
            externalPayments.add(tenderName)
        }

        externalPaymentSpinner.adapter = ArrayAdapter(
            requireActivity(),
            R.layout.external_payment_item,
            externalPayments
        )

        externalPaymentSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    selectedExternalType = externalPaymentMap[externalPaymentSpinner.selectedItem]!!
                    externalSourceEditText.hint = selectedExternalType.toHint()
                }
            }

        builder.apply {
            setTitle(R.string.external_payment_dialog_title)
            setMessage(R.string.external_payment_dialog_message)
            setPositiveButton(android.R.string.ok) { _, _ ->
                val externalSource = externalSourceEditText.text.toString()
                val externalDetails =
                    ExternalPaymentDetails(selectedExternalType.name, externalSource)
                method.trigger(externalDetails)
            }
        }
    }

    private fun connectCashViews(
        view: View,
        builder: AlertDialog.Builder,
        method: AlternatePaymentMethod,
    ) {
        check(method is AlternatePaymentMethod.CashEntryMethod) {
            "This method should only be used for cash payments"
        }
        val cashInputEditText = view.findViewById<EditText>(R.id.cash_input)
        cashInputEditText.hint = formatAmount(totalAmount).drop(1)
        cashInputEditText.addTextChangedListener(AmountFormatter())

        builder.apply {
            setTitle(R.string.cash_payment_dialog_title)
            setMessage(R.string.cash_payment_dialog_message)
            setPositiveButton(android.R.string.ok) { _, _ ->
                val cashInputParsed = parseAmount(cashInputEditText.text.toString())
                val buyerSuppliedMoney = Money(cashInputParsed, CurrencyCode.USD)
                // Checks if buyer supplied cash is null or less than total
                if (cashInputEditText.text.isNullOrEmpty() || buyerSuppliedMoney.amount < totalAmount) {
                    // Show error message
                    showSimpleMessageDialog(
                        title = "Error",
                        message = "Please enter a cash amount equal to or greater than " +
                                "${formatAmount(totalAmount)}."
                    )
                } else {
                    method.trigger(buyerSuppliedMoney)
                }
            }
        }
    }

    private fun AlternatePaymentMethod.toDialog(
        layout: Int,
        connectViews: (view: View, builder: AlertDialog.Builder, method: AlternatePaymentMethod) -> Unit,
    ) {
        val view = layoutInflater.inflate(layout, null)
        val builder = AlertDialog.Builder(requireActivity())
        connectViews(view, builder, this)
        builder.setView(view)
        builder.apply {
            setView(view)
            setNegativeButton(android.R.string.cancel, null)
        }
        alertDialog = builder.create() // Initialize AlertDialog
        alertDialog!!.show()
    }

    private fun ExternalTenderType.toHint(): String {
        val hint = when (this) {
            ExternalTenderType.CHECK -> R.string.check_hint
            ExternalTenderType.BANK_TRANSFER -> R.string.bank_transfer_hint
            ExternalTenderType.OTHER_GIFT_CARD -> R.string.other_gift_card_hint
            ExternalTenderType.CARD -> R.string.card_hint
            else -> R.string.default_hint
        }
        return getString(hint)
    }


    private fun updatePromptUi(
        available: Set<CardEntryMethod>,
        view: View,
    ) {
        val promptText = view.findViewById<TextView>(R.id.prompt_text)
        val readerImage = view.findViewById<ImageView>(R.id.reader)
        when (available.size) {
            0 -> {
                promptText.setText(R.string.no_readers_connected)
                readerImage.setImageResource(R.drawable.ic_reader_slash)
            }

            1 -> when {
                available.contains(CardEntryMethod.SWIPED) -> {
                    promptText.setText(R.string.method_swipe)
                    readerImage.setImageResource(R.drawable.ic_swipe_card_swiping)
                }

                available.contains(CardEntryMethod.CONTACTLESS) -> {
                    promptText.setText(R.string.method_tap)
                    readerImage.setImageResource(R.drawable.ic_reader)
                }

                else -> {
                    promptText.setText(R.string.method_dip)
                    readerImage.setImageResource(R.drawable.ic_reader)
                }
            }

            2 -> when {
                !available.contains(CardEntryMethod.SWIPED) -> {
                    promptText.setText(R.string.method_dip_tap)
                    readerImage.setImageResource(R.drawable.ic_reader)
                }

                !available.contains(CardEntryMethod.CONTACTLESS) -> {
                    promptText.setText(
                        R.string.method_dip_swipe
                    )
                    readerImage.setImageResource(R.drawable.ic_swipe_card_swiping)
                }

                else -> {
                    promptText.setText(R.string.method_swipe_tap)
                    readerImage.setImageResource(R.drawable.ic_reader)
                }
            }

            else -> {
                promptText.setText(R.string.method_dip_swipe_tap)
                readerImage.setImageResource(R.drawable.ic_reader)
            }
        }
    }

    fun startPayment() {
        Log.d("Tageee", "checkoutParameters 666:: ,")

        if (!isAdded) {
            Log.w("Tageee", "Fragment is not added to its activity. Cannot start payment.")
            return
        }

        Log.d("Tageee", "checkoutParameters 1:: ,")

         if (viewModel.paymentInProgress) return
        Log.d("Tageee", "checkoutParameters 222:: ,")
        val autoComplete: Boolean =
            arguments?.getBoolean(KeypadFragment.PARAM_AUTOCOMPLETE) ?: false
         Log.d("Tageee", "checkoutParameters start payment:: , $autoComplete")

         val idempotencyKey = arguments?.getString(KeypadFragment.IDEMPOTENCY_KEY)
        val builder = PaymentParameters.Builder(
            amount = Money(baseAmount, CurrencyCode.USD),
            /* An idempotency key can be optionally provided by the user through manual entry. In cases
            where the idempotency key is not provided by the user, we will generate random IdempotencyKey.
            Manually entered IdempotencyKey is used for internal tests only!
             */
            idempotencyKey = if (idempotencyKey.isNullOrEmpty()) {
                UUID.randomUUID().toString()
            } else {
                idempotencyKey
            },
        ).autocomplete(autoComplete)

        if (!autoComplete) {
            builder.acceptPartialAuthorization(
                arguments?.getBoolean(KeypadFragment.PARAM_ACCEPT_PARTIAL) ?: false
            )
        }
        val tipAmount = arguments?.getLong(KeypadFragment.PARAM_TIP_AMOUNT)
        if (tipAmount != null && tipAmount > 0) {
            builder.tipMoney(Money(tipAmount, CurrencyCode.USD))
        }
        val feeAmount = arguments?.getLong(KeypadFragment.PARAM_FEE_AMOUNT)
        if (feeAmount != null && feeAmount > 0) {
            builder.appFeeMoney(Money(feeAmount, CurrencyCode.USD))
        }
        val delayDuration = arguments?.getLong(KeypadFragment.PARAM_DELAY_DURATION)
        if (delayDuration != null && delayDuration > 0) {
            builder.delayDuration(delayDuration)
        }
        val orderId = arguments?.getString(KeypadFragment.PARAM_ORDER_ID)
        if (!orderId.isNullOrEmpty()) {
            builder.orderId(orderId)
        }
        val customerId = arguments?.getString(KeypadFragment.PARAM_CUSTOMER_ID)
        if (!customerId.isNullOrEmpty()) {
            builder.customerId(customerId)
        }
        val teamMemberId = arguments?.getString(KeypadFragment.PARAM_TEAM_MEMBER_ID)
        if (!teamMemberId.isNullOrEmpty()) {
            builder.teamMemberId(teamMemberId)
        }
        val locationId = arguments?.getString(KeypadFragment.PARAM_LOCATION_ID)
        if (!locationId.isNullOrEmpty()) {
            builder.locationId(locationId)
        }
        val referenceId = arguments?.getString(KeypadFragment.PARAM_REFERENCE_ID)
        if (!referenceId.isNullOrEmpty()) {
            builder.referenceId(referenceId)
        }
        val note = arguments?.getString(KeypadFragment.PARAM_NOTE)
        if (!note.isNullOrEmpty()) {
            builder.note(note)
        }
        val statementDescription = arguments?.getString(KeypadFragment.PARAM_STATEMENT_DESCRIPTION)
        if (!statementDescription.isNullOrEmpty()) {
            builder.statementDescription(statementDescription)
        }

        val parameters = builder.build()
        Log.i("demo-app", "Starting payment with parameters=$parameters")

        viewModel.startPayment(parameters, null)
    }

    private fun dismissDialog() {
        alertDialog?.let {
            if (it.isShowing) it.dismiss()
            alertDialog = null
        }
    }

    private fun showChargeSuccessDialog(paymentResult: Payment) {
        showSimpleMessageDialog(
            title = "Success",
            message = Html.fromHtml(
                (paymentResult as Payment.OnlinePayment).toString()
            ).toHtml()
        )
    }

    private fun showChargeFailureDialog(errorMessage: String, details: List<ErrorDetails>) =
        showSimpleMessageDialog(
            title = "Error",
            message = Html.fromHtml(
                details.joinToString(
                    prefix = "<b>$errorMessage</b><p>",
                    separator = "<p>",
                    limit = 5,
                    transform = {
                        "${it.detail}" + if (it.field.isNullOrBlank()) {
                            ""
                        } else {
                            "(${it.field}"
                        }
                    }
                )
            )
        )

    private fun showSimpleMessageDialog(
        title: String,
        message: CharSequence,
    ) {
        val dialog = AlertDialog.Builder(this.requireActivity())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, null)
            .show()

        // Make the text inside the dialog selectable so one can copy it
        (dialog?.findViewById<View>(android.R.id.message) as TextView).apply {
            setTextIsSelectable(true)
        }
    }

    companion object {
        const val TAG = "ChargeFragment"
    }

}