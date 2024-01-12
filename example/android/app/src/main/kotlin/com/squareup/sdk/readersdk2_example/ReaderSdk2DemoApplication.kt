package com.squareup.sdk.readersdk2_example

import io.flutter.app.FlutterApplication
import com.squareup.sdk.reader2.ReaderSdk
class ReaderSdk2DemoApplication: FlutterApplication() {
    override fun onCreate() {
        super.onCreate()

        ReaderSdk.initialize("readersdk2-alpha-tester", this)
    }
}