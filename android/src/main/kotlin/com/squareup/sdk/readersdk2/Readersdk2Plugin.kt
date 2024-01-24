package com.squareup.sdk.readersdk2

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import com.squareup.sdk.reader2.ReaderSdk
import com.squareup.sdk.reader2.mockreader.ui.MockReaderUI
import com.squareup.sdk.reader2.payment.PaymentManager
import com.squareup.sdk.readersdk2.auth.AuthorizeActivity
import com.squareup.sdk.readersdk2.auth.OAuthHelper
import com.squareup.sdk.readersdk2.payment.ChargeViewModel
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** Readersdk2Plugin */
class Readersdk2Plugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///89[] 3
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private var currentActivity: Activity? = null

    lateinit var authorisedModule: AuthorizeModule
    private var paymentModule: PaymentModule = PaymentModule()
    private var viewModel: ChargeViewModel? = null
    private var authorizeActivity: AuthorizeActivity = AuthorizeActivity()
    private lateinit var contextReader: Context

    private var isAuthorize = false
    var currentAuthentication: String = "Production"

    private var paymentManager: PaymentManager? = null

    override fun onMethodCall(call: MethodCall, @NonNull result: Result) {
        when (call.method) {

//            "currentEnv" -> {
//                 currentAuthentication = call.argument<String>("envPrams").toString()
//                Log.d("TAG", "onMethodCall--->: $currentAuthentication")
//                autorizationInit(currentActivity!!.applicationContext)
//                result.success(currentAuthentication)
//            }

//            "isAuthorized" -> {
//                Log.d("TAG", "onMethodCall---> invoked 35")
//                Log.d("TAG", "onMethodCall---> isAuthorized 35: $result")
//
//                isAuthorize = authorisedModule.isAuthorized()
//                if (!isAuthorize){
//                    isAuthorize =  authorisedModule.isAuthorized()
//                    Log.d("TAG", "onMethodCall---> isAuthorized 47: $isAuthorize")
//                }
//
//                Log.d("TAG", "onMethodCall---> isAuthorized 123: $isAuthorize")
//                result.success(isAuthorize)
//            }
            "mockReaderUI" -> {
                val mockReader = authorisedModule.isMockReaderUI()
                Log.d("TAG", "onMethodCall: mockreader = $mockReader")
                result.success(mockReader)
            }

            "isAuthorizationInProgress" -> {
                val isAuthorizationInProgress = authorisedModule.isAuthorizationInProgress()
                result.success(isAuthorizationInProgress)
            }

            "authorizedLocation" -> {
                val authorizedLocation = authorisedModule.authorizedLocation(result)
                result.success(authorizedLocation)
            }

            "authorize" -> {
                val authCode: String? = call.argument("authCode")
                val authorize = authorisedModule.authorize(authCode!!, result)
                result.success(authorize)
            }

            "currentAuthorisation" -> {
                currentAuthentication = call.argument<String>("currentEnvironment").toString()
                autorizationInit(currentActivity!!.applicationContext)
                val currentEnvironments =
                    authorisedModule.currentEnvironment(currentAuthentication, contextReader)
                result.success(currentEnvironments)
            }

            "startCheckout" -> {
                val checkoutParams: HashMap<String, Any>? = call.argument("checkoutParams")
                var resultPayment = paymentModule.startCheckout(
                    checkoutParams,
                    result,
                    paymentManager!!,
                    viewModel!!,
                    contextReader
                )
                Log.d("TAG", "onMethodCall--->: $checkoutParams")
                Log.d("TAG", "startPayment: 104 - $resultPayment")
                result.success(resultPayment)
            }

            else -> {
                result.notImplemented()
                Log.d("TAG", "FlutterOnCreate: else part run")
            }
        }
    }

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        //onAttachedToEngine(flutterPluginBinding.applicationContext, flutterPluginBinding.binaryMessenger)
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "readersdk2")
        Log.d("TAG", "onAttachedToEngine: $channel")
        channel.setMethodCallHandler(this)

    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }


    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        Log.d("TAG", "world 94:--> ")
        setContextForModules(binding.activity)
    }

    fun setContextForModules(activity: Activity) {
        Log.d("TAG", "world 123:--> ")
        currentActivity = activity
        Log.d("TAG", "world 125:--> $currentActivity")
        //autorizationInit(currentActivity!!.applicationContext)
    }


    override fun onDetachedFromActivityForConfigChanges() {

    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        setContextForModules(binding.activity)
    }

    override fun onDetachedFromActivity() {

    }

    private fun autorizationInit(context: Context) {
        contextReader = context
        val appId = OAuthHelper.getAppId(context, currentAuthentication)
        ReaderSdk.initialize(appId, currentActivity!!.application)
        authorisedModule = AuthorizeModule()
        if (::authorisedModule.isInitialized) {
            // Use authorisedModule
        } else {
            // Handle the case where the property is not initialized
        }
        paymentManager = ReaderSdk.paymentManager()
        viewModel = ChargeViewModel()
        isAuthorize = authorisedModule.isAuthorized()
        Log.d("TAG", "authorizationInit: 1 = $isAuthorize")
        if (!isAuthorize) {
            isAuthorize = authorisedModule.isAuthorized()
            Log.d("TAG", "authorizationInit: 2 = $isAuthorize")
        }
        if (isAuthorize) {
            Log.d("TAG", "authorizationInit: mock reader sdk 121 = $isAuthorize")
            if (ReaderSdk.isSandboxEnvironment()) {
                Log.d(
                    "TAG",
                    "authorizationInit: mock reader sdk = ${ReaderSdk.isSandboxEnvironment()}"
                )
                MockReaderUI.show()
                Log.d("TAG", "authorizationInit: mock reader show 99898 ")
            }
        }
    }
}
