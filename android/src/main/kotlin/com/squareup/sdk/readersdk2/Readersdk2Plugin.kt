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
//               val startCheckOut =  paymentModule.startCheckout(
             //       checkoutParams, result, paymentManager!!, viewModel!!, contextReader,
                var resultPayment = paymentModule.startCheckout(
                    checkoutParams, result, paymentManager!!, viewModel!!, contextReader  )
                result.success(resultPayment)
              //  result.success(paymentCheckout)
            }

            "paymentSuccess" -> {
                Log.d("TAG", "onMethodCall: payment failure 1== ${GlobleSingleTon.paymentFailure}")
                Log.d("TAG", "onMethodCall: payment failure 2== ${GlobleSingleTon.paymentResult}")

                if(GlobleSingleTon.paymentFailure != "") {
                    Log.d("TAG", "onMethodCall: payment failure == ${GlobleSingleTon.paymentFailure}")
                    return result.success(GlobleSingleTon.paymentFailure)
                } else {
                    Log.d("TAG", "onMethodCall: payment successssss == ${GlobleSingleTon.paymentResult}")
                    return result.success(GlobleSingleTon.paymentResult)
                }
            }

            "directMockReaderUI" -> {
                ReaderSdk.authorizationManager().deauthorize()
                Log.d("TAG", "isAuthorized: deauthorized")
//                MockReaderUI.hide()
//                Log.d("TAG", "isAuthorized: deauthorized UHUDHUHDU")
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
                Log.d("TAG", "FlutterOnCreate: else part run")
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
        Log.d("TAG", "authorize: authorized callback ref it 123254235235235")
//        ReaderSdk.authorizationManager().addAuthorizeCallback {
//            Log.d("TAG", "authorize: authorized callback ref it 12 = $it")
//            authorizeActivity.onAuthorizeResult(it)
//        }
    }

    fun setContextForModules(activity: Activity) {
        currentActivity = activity
        Log.d("TAG", "world 125:--> $currentActivity")
        //authorizationInit(currentActivity!!.applicationContext)
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
//        this.currentAccessToken = OAuthHelper.getAppId(context, currentAuthentication)!!.authToken
//        this.currentLocationId = OAuthHelper.getAppId(context, currentAuthentication)!!.locationId
//        currentAuthentication = OAuthHelper.getAppId(context, currentAuthentication)!!.name
//        appIds = OAuthHelper.getAppId(context, currentAuthentication)!!.applicationId

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
        Log.d("TAG", "authorizationInit: 1 = $isAuthorize")

        authorisedModule.authorizationManager!!.addAuthorizeCallback {
            authorizeActivity.onAuthorizeResult(it, context)
            Log.d("TAG", "authorizationInit: 17000000  ${it}")
            MockReaderUI.show()
        }


    }

    private fun mockReaderUIShow(currentAccessToken: String, currentLocationId: String) {
        Log.d("TAG", "isAuthorized: second 45 = $currentAccessToken")
        Log.d("TAG", "isAuthorized: second 34 = $currentLocationId")
//        ReaderSdk.authorizationManager().deauthorize()
//        Log.d("TAG", "isAuthorized: second 1090910910")
//        MockReaderUI.hide()
        if (!authorisedModule.isAuthorized(currentAccessToken, currentLocationId)) {
            Log.d("TAG", "isAuthorized: second 999 =")
            ReaderSdk.authorizationManager().authorize(
                currentAccessToken, currentLocationId
            )
            Log.d("TAG", "isAuthorized: second 9876 =")
        }
        if (ReaderSdk.isSandboxEnvironment()) {
            MockReaderUI.show()
        }
    }
}