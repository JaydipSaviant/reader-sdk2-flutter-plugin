package com.squareup.sdk.readersdk2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.util.Log
import com.squareup.sdk.reader2.ReaderSdk
import com.squareup.sdk.reader2.authorization.AuthorizationManager
import com.squareup.sdk.reader2.core.CallbackReference
import com.squareup.sdk.reader2.mockreader.ui.MockReaderUI
import com.squareup.sdk.readersdk2.ErrorHandler.ErrorHandlerUtils
import com.squareup.sdk.readersdk2.auth.AuthorizeActivity
import com.squareup.sdk.readersdk2.auth.OAuthHelper
import com.squareup.sdk.readersdk2.converter.LocationConverter
import io.flutter.plugin.common.MethodChannel


class AuthorizeModule {
    // Define all the authorization error debug codes and messages below
    // These error codes and messages **MUST** align with iOS error codes and javascript error codes
    // Search KEEP_IN_SYNC_AUTHORIZE_ERROR to update all places

    companion object {
        // Flutter plugin debug error codes
        private const val FL_AUTH_LOCATION_NOT_AUTHORIZED = "fl_auth_location_not_authorized"

        // Flutter plugin debug messages
        private const val FL_MESSAGE_AUTH_LOCATION_NOT_AUTHORIZED =
            "This device must be authorized with a Square location in order to get that location. Obtain an authorization code for a Square location from the mobile/authorization-code endpoint and then call authorizeAsync."

        // Android only Flutter errors and messages
        private const val FL_AUTHORIZE_ALREADY_IN_PROGRESS = "fl_authorize_already_in_progress"
        private const val FL_MESSAGE_AUTHORIZE_ALREADY_IN_PROGRESS =
            "Authorization is already in progress. Please wait for authorizeAsync to complete."

        private const val RN_DEAUTHORIZE_ALREADY_IN_PROGRESS = "rn_deauthorize_already_in_progress"
        private const val RN_MESSAGE_DEAUTHORIZE_ALREADY_IN_PROGRESS =
            "Deauthorization is already in progress. Please wait for deauthorizeAsync to complete."
    }

    private var authorizeCallbackRef: CallbackReference? = null
    private var deauthorizeCallbackRef: CallbackReference? = null
    private val mainLooperHandler = Handler()
    private lateinit var callbackRef: CallbackReference
    private val authorizeActivity = AuthorizeActivity()
    private lateinit var prefs: SharedPreferences

    private lateinit var currentEnvironment: OAuthHelper.Environment
    private lateinit var environments: List<OAuthHelper.Environment>

    private var authorizationManager: AuthorizationManager? = null

    init {
        Log.d("TAG", "configureFlutterEngine: 51")
        authorizationManager = ReaderSdk.authorizationManager()
        Log.d("TAG", "configureFlutterEngine: 53")
    }

    fun isMockReaderUI() {
        return MockReaderUI.show()
    }

    fun isAuthorized(): Boolean {
        Log.d(
            "TAG",
            "authorizationInit: 090 = ${authorizationManager?.authorizationState!!.isAuthorized}"
        )
        return authorizationManager?.authorizationState!!.isAuthorized
    }

    fun isAuthorizationInProgress(): Boolean {
        return authorizationManager?.authorizationState!!.isAuthorizationInProgress
    }

    fun authorizedLocation(result: MethodChannel.Result) {
        val authorizationState = ReaderSdk.authorizationManager().authorizationState

        if (authorizationState.isAuthorized) {
            val locationConverter = LocationConverter()
            result.success(locationConverter.toMapObject(ReaderSdk.authorizationManager().authorizedLocation!!))
        } else {
            result.error(
                ErrorHandlerUtils.USAGE_ERROR,
                ErrorHandlerUtils.getNativeModuleErrorMessage(FL_AUTH_LOCATION_NOT_AUTHORIZED),
                ErrorHandlerUtils.getDebugErrorObject(
                    FL_AUTH_LOCATION_NOT_AUTHORIZED,
                    FL_MESSAGE_AUTH_LOCATION_NOT_AUTHORIZED
                )
            )
        }
    }

    fun authorize(authCode: String, flutterResult: MethodChannel.Result) {
        Log.d("TAG", "authorize: authcode = $authCode")
        Log.d("TAG", "authorize: flutterResult = $flutterResult")
        authorizeCallbackRef = ReaderSdk.authorizationManager().addAuthorizeCallback {
            Log.d("TAG", "authorize: authorized callback ref it 12 = $it")
            authorizeActivity.onAuthorizeResult(it)
            Log.d("TAG", "authorize: authorized callback ref it = $it")
        }
        Log.d("TAG", "authorize: authorized callback ref = $authorizeCallbackRef")
        mainLooperHandler.post {
            Log.d("TAG", "authorize: mainLooper ")
            ReaderSdk.authorizationManager().authorize(
                "EAAAFNbbmssq_Adi_nZhJXZ1n5Sg0So5eBeYLxAvJ0pfvMX1A_OFtlwxPti1T3xW",
                "LBBSYN1QKHJSY"
            )
        }
    }

    fun currentEnvironment(currentAuthentication: String?, context: Context): Boolean {
        var isValue: Boolean = false
        when {
            (currentAuthentication == OAuthHelper.SANDBOX || currentAuthentication == OAuthHelper.UI_TESTING_SANDBOX) ->
                isValue = authorizeActivity.authorizeWithSandbox(authorizationManager!!)

            authorizeActivity.useOAuth -> authorizeActivity.authorizeWithOauth(context)
            else -> authorizeActivity.authorizeWithPAT(authorizationManager!!)
        }
        Log.d("TAG", "currentEnvironment: 99 $isValue")


//        ReaderSdk.authorizationManager().authorize(
//            "EAAAFNbbmssq_Adi_nZhJXZ1n5Sg0So5eBeYLxAvJ0pfvMX1A_OFtlwxPti1T3xW",
//            "LBBSYN1QKHJSY"
//        )
        return isValue
    }
}