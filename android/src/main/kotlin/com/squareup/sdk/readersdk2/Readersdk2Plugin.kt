package com.squareup.sdk.readersdk2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat.startActivity
import com.squareup.sdk.reader2.ReaderSdk
import com.squareup.sdk.reader2.ReaderSdk.authorizationManager
import com.squareup.sdk.readersdk2.auth.AuthorizeActivity
import com.squareup.sdk.readersdk2.auth.OAuthHelper
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
    private var authorizeActivity: AuthorizeActivity = AuthorizeActivity()

    override fun onMethodCall(call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "isAuthorized" -> {
                Log.d("TAG", "onMethodCall---> invoked 35")
                Log.d("TAG", "onMethodCall---> isAuthorized 35: $result")
                val isAuthorize = authorisedModule.isAuthorized()
                if (!isAuthorize){
                    authorizeActivity.firstTimeCreateInten()
                }

                Log.d("TAG", "onMethodCall---> isAuthorized 123: $isAuthorize")
                result.success(isAuthorize)
            }

            "isAuthorizationInProgress" -> {
                Log.d("TAG", "onMethodCall---> invoked 43")
                val isAuthorizationInProgress = authorisedModule.isAuthorizationInProgress(result)
                Log.d(
                    "TAG",
                    "onMethodCall---> isAuthorizationInProgress 456: $isAuthorizationInProgress"
                )
                result.success(isAuthorizationInProgress)
            }

            "authorizedLocation" -> {
                Log.d("TAG", "onMethodCall---> invoked 53")
                val authorizedLocation = authorisedModule.authorizedLocation(result)
                Log.d("TAG", "onMethodCall---> authorizedLocation 456: $authorizedLocation")
                result.success(authorizedLocation)
            }

            "authorize" -> {
                Log.d("TAG", "onMethodCall---> invoked 60")
                val authCode: String? = call.argument("authCode")
                val authorize = authorisedModule.authorize(authCode!!, result)
                Log.d("TAG", "onMethodCall---> authorizedLocation 1: $authCode")
                Log.d("TAG", "onMethodCall---> authorizedLocation 2: $result")
                result.success(authorize)
            }

            "currentAuthorisation" -> {
                Log.d("TAG", "onMethodCall---> invoked 69")
                val currentAuthentication: String? = call.argument("currentEnvironment")
                Log.d("TAG", "onMethodCall: currentAuthentication = $currentAuthentication ")
                val currentEnvironments = authorisedModule.currentEnvironment(currentAuthentication)
                Log.d("TAG", "onMethodCall: currentAuthentication 73 = $currentEnvironments ")
                result.success(currentEnvironments)
            }

            "startPaymentCheckout" -> {
                val checkoutParameters = call.arguments as? HashMap<String, Any>
                paymentModule.startCheckout(checkoutParameters, result)
                Log.d("TAG", "onMethodCall--->: $checkoutParameters")
                result.success("start payment checkout")
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
        //com.squareup.sdk
        autorizationInit(currentActivity!!.applicationContext)
        Log.d("TAG", "world 98:--> ")
    }

    fun setContextForModules(activity: Activity) {
        currentActivity = activity
    }


    override fun onDetachedFromActivityForConfigChanges() {

    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        setContextForModules(binding.activity)
    }

    override fun onDetachedFromActivity() {

    }

    private fun autorizationInit(context: Context) {
        val appId = OAuthHelper.getAppId(context)
        ReaderSdk.initialize(appId, currentActivity!!.application)
        authorisedModule = AuthorizeModule()
        if (::authorisedModule.isInitialized) {
            // Use authorisedModule
            Log.d("TAG", "onMethodCall---> isAuthorized 33")
        } else {
            // Handle the case where the property is not initialized
            Log.d("TAG", "onMethodCall---> isAuthorized 36")
        }
    }


}
