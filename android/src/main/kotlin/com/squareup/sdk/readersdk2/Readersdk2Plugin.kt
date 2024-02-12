package com.squareup.sdk.readersdk2

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import com.squareup.sdk.reader2.ReaderSdk
import com.squareup.sdk.reader2.mockreader.ui.MockReaderUI
import com.squareup.sdk.reader2.payment.PaymentManager
import com.squareup.sdk.readersdk2.auth.AuthorizeActivity
import com.squareup.sdk.readersdk2.payment.ChargeViewModel
import io.flutter.embedding.android.KeyData.CHANNEL
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** Readersdk2Plugin */
class Readersdk2Plugin : FlutterPlugin, MethodCallHandler, ActivityAware {

    private lateinit var channel: MethodChannel
    private var currentActivity: Activity? = null

    lateinit var authorisedModule: AuthorizeModule
    private var paymentModule: PaymentModule = PaymentModule()
    private var viewModel: ChargeViewModel? = null
    lateinit var contextReader: Context

    private var isAuthorize = false
    private var appIds: String = ""
    private var currentAuthentication: String = "Production"
    private var currentAccessToken: String =
        "EAAAFNbbmssq_Adi_nZhJXZ1n5Sg0So5eBeYLxAvJ0pfvMX1A_OFtlwxPti1T3xW"
    private var currentLocationId: String = "LBBSYN1QKHJSY"

    private var paymentManager: PaymentManager? = null

    private var authorizeActivity = AuthorizeActivity()

    private var sharedPreference = SharedPreference()
    private var flutterEngine: FlutterEngine? = null

    fun setFlutterEngine(engine: FlutterEngine) {
        flutterEngine = engine
    }

    override fun onMethodCall(call: MethodCall, @NonNull result: Result) {
        when (call.method) {

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
                currentAccessToken = call.argument<String>("selectedAccessToken").toString()
                currentLocationId = call.argument<String>("selectedLocationId").toString()
                sharedPreference.setData(
                    currentActivity!!.applicationContext,
                    currentAuthentication,
                    currentAccessToken,
                    currentLocationId
                )
                authorizationInit(
                    currentActivity!!.applicationContext, currentAccessToken, currentLocationId
                )
                val currentEnvironments =
                    authorisedModule.currentEnvironment(currentAuthentication, contextReader)
                setAuth(currentAccessToken, currentLocationId)
                result.success(currentEnvironments)
            }

            "startCheckout" -> {
                val checkoutParams: HashMap<String, Any>? = call.argument("checkoutParams")
                //var resultPayment =
                    paymentModule.startCheckout(
                    checkoutParams, result, paymentManager!!, viewModel!!, contextReader, channel
                )
            }

            "directMockReaderUI" -> {
               // ReaderSdk.authorizationManager().deauthorize()
//                MockReaderUI.hide()
                var getCurrentEnv = sharedPreference.getData(currentActivity!!.applicationContext)
                if (getCurrentEnv != null) {
                    println("myVariable is not null: $getCurrentEnv")
                    authorizationInit(
                        currentActivity!!.applicationContext, currentAccessToken, currentLocationId
                    )
                    //  MockReaderUI.show()
                } else {
                    println("myVariable is null")
                }
            }

            else -> {
                result.notImplemented()
            }
        }
    }
    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        //onAttachedToEngine(flutterPluginBinding.applicationContext, flutterPluginBinding.binaryMessenger)
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "readersdk2")
        channel.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        setContextForModules(binding.activity)
    }

    fun setAuth(currentAccessToken: String, currentLocationId: String) {
        mockReaderUIShow(currentAccessToken, currentLocationId)
    }

    private fun setContextForModules(activity: Activity) {
        currentActivity = activity
    }

    override fun onDetachedFromActivityForConfigChanges() {}

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        setContextForModules(binding.activity)
    }

    override fun onDetachedFromActivity() {}

    private fun authorizationInit(
        context: Context,
        currentAccessToken: String,
        currentLocationId: String,
    ) {
        contextReader = context
        /* this.currentAccessToken = OAuthHelper.getAppId(context, currentAuthentication)!!.authToken
         this.currentLocationId = OAuthHelper.getAppId(context, currentAuthentication)!!.locationId
         currentAuthentication = OAuthHelper.getAppId(context, currentAuthentication)!!.name
         appIds = OAuthHelper.getAppId(context, currentAuthentication)!!.applicationId
 */
        ReaderSdk.initialize("sandbox-sq0idb-7QT2EriOdn1Gz8jw7e2KSw", currentActivity!!.application)
        authorisedModule = AuthorizeModule()
        if (::authorisedModule.isInitialized) {
            // Use authorisedModule
        } else {
            // Handle the case where the property is not initialized
        }
        authorizeActivity = AuthorizeActivity()
        paymentManager = ReaderSdk.paymentManager()
        viewModel = ChargeViewModel()
        isAuthorize = authorisedModule.isAuthorized(currentAccessToken, currentLocationId)

        authorisedModule.authorizationManager!!.addAuthorizeCallback {
            authorizeActivity.onAuthorizeResult(it, context)
            MockReaderUI.show()
        }
    }

    private fun mockReaderUIShow(currentAccessToken: String, currentLocationId: String) {
        if (!authorisedModule.isAuthorized(currentAccessToken, currentLocationId)) {
            ReaderSdk.authorizationManager().authorize(
                currentAccessToken, currentLocationId
            )
        }
        if (ReaderSdk.isSandboxEnvironment()) {
            MockReaderUI.show()
        }
    }
}