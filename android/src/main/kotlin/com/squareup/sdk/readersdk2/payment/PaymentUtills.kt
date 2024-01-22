package com.squareup.sdk.readersdk2.payment

import com.squareup.sdk.reader2.payment.Card
import com.squareup.sdk.reader2.payment.CardPaymentDetails
import com.squareup.sdk.reader2.payment.CashDetails
import com.squareup.sdk.reader2.payment.Money
import com.squareup.sdk.reader2.payment.Payment.OnlinePayment

fun formatAmount(amount: Long) = "$%.2f".format(amount / 100.0)

fun parseAmount(text: String?): Long {
    if (text.isNullOrEmpty()) {
        return 0L
    }
    return text.filter {
        it.isDigit()
    }.toLong()
}

fun Money.toHtml(): String = "$amount¢$currencyCode"

fun CardPaymentDetails.OnlineCardPaymentDetails.toHtml(): String {
    val cobrand = if (card.cardCoBrand == Card.CoBrand.NONE) {
        ""
    } else {
        "(${card.cardCoBrand.name})"
    }
    var stringSoFar = "Entry method: $entryMethod<br>Status: $status <br>" +
            "Card: ${card.brand} ${card.lastFourDigits} $cobrand<br>Name: ${card.cardholderName}<br>" +
            "EXP: ${card.expirationMonth}/${card.expirationYear}<br>"
    if (authorizationCode != null) {
        stringSoFar += "Authorization Code: $authorizationCode<br>"
    }
    if (applicationId != null) {
        stringSoFar += "EMV Application Id: $applicationId<br>"
    }
    if (applicationName != null) {
        stringSoFar += "EMV Application Name $applicationName<br>"
    }
    if (verificationMethod != null) {
        stringSoFar += "EMV Verification Method $verificationMethod<br>"
    }
    if (verificationResults != null) {
        stringSoFar += "EMV Verification Results $verificationResults<br>"
    }
    if (accountType != null) {
        stringSoFar += "Account Type $accountType<br>"
    }
    return stringSoFar
}

fun CashDetails.toHtml() =
    "Buyer supplied ${buyerSuppliedMoney.toHtml()}, change ${changeBackMoney.toHtml()}"

fun OnlinePayment.toHtml() = "<b>Payment Details</b><br>" +
        "<br><b>Payment Id</b><br>$id" +
        "<br><b>Order Id</b><br>$orderId" +
        "<br><b>Reference ID</b><br>$referenceId" +
        "<br><b>Location ID</b><br>$locationId" +
        "<br><b>Team Member ID</b><br>$teamMemberId" +
        "<br><b>Status</b><br>$status" +
        "<br><b>Total Money</b><br>${totalMoney.toHtml()}" +
        "<br><b>Total Tip Money</b><br>${tipMoney.toHtml()}" +
        "<br><b>App Fee Money</b><br>${appFeeMoney.toHtml()}" +
        "<br><b>Completed At</b><br>$createdAt" +
        "<br><b>Statement Description</b><br>$statementDescription" +
        "<br><b>Note</b><br>$note" +
        "<br><b>Capabilities</b><br>$capabilities" +
        "<br><b>Receipt Number</b><br>$receiptNumber" +
        when {
            cardDetails != null -> "<br><br><b>Card Payment Details</b><br>" + cardDetails!!.toHtml()
            cashDetails != null -> "<br><br><b>Cash Payment Details</b><br>" + cashDetails!!.toHtml()
            else -> "<br><br><b>NO PAYMENT DETAILS INCLUDED</b><br>"
        }