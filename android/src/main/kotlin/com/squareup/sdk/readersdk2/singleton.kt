package com.squareup.sdk.readersdk2

import com.squareup.sdk.reader2.payment.Payment


object GlobleSingleTon {
    var paymentResult: Payment? = null
    var paymentFailure: String? = null
}