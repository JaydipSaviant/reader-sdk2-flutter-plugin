package com.squareup.sdk.readersdk2

import android.content.Context
import android.os.Handler
import android.util.Log
import com.squareup.sdk.reader2.ReaderSdk
import com.squareup.sdk.reader2.authorization.AuthorizationManager
import com.squareup.sdk.reader2.core.CallbackReference
import com.squareup.sdk.readersdk2.ErrorHandler.ErrorHandlerUtils
import com.squareup.sdk.readersdk2.auth.AuthorizeActivity
import com.squareup.sdk.readersdk2.auth.OAuthHelper
import com.squareup.sdk.readersdk2.converter.LocationConverter
import io.flutter.plugin.common.MethodChannel

class AuthorizeModule {

    companion object {
        // Flutter plugin debug error codes
        private const val FL_AUTH_LOCATION_NOT_AUTHORIZED = "fl_auth_location_not_authorized"

        // Flutter plugin debug messages
        private const val FL_MESSAGE_AUTH_LOCATION_NOT_AUTHORIZED =
            "This device must be authorized with a Square location in order to get that location. Obtain an authorization code for a Square location from the mobile/authorization-code endpoint and then call authorizeAsync."
    }

    private var authorizeCallbackRef: CallbackReference? = null
    private val mainLooperHandler = Handler()
    private val authorizeActivity = AuthorizeActivity()
    var authorizationManager: AuthorizationManager? = null

    init {
        authorizationManager = ReaderSdk.authorizationManager()
    }

    fun isAuthorized(
        currentAccessToken: String,
        currentLocationId: String,
    ): Boolean {
        if (!authorizationManager?.authorizationState!!.isAuthorized) {
            ReaderSdk.authorizationManager().authorize(
                currentAccessToken, currentLocationId
            )
        }
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
                    FL_AUTH_LOCATION_NOT_AUTHORIZED, FL_MESSAGE_AUTH_LOCATION_NOT_AUTHORIZED
                )
            )
        }
    }

    fun authorize(authCode: String, flutterResult: MethodChannel.Result) {
        Log.d("TAG", "authorize: authcode = $authCode")
        authorizeCallbackRef = ReaderSdk.authorizationManager().addAuthorizeCallback {
            Log.d("TAG", "authorize: authorized callback ref it 12 = $it")
          //  authorizeActivity.onAuthorizeResult(it)
        }
        mainLooperHandler.post {
            Log.d("TAG", "authorize: mainLooper ")
            ReaderSdk.authorizationManager().authorize(
                "EAAAFNbbmssq_Adi_nZhJXZ1n5Sg0So5eBeYLxAvJ0pfvMX1A_OFtlwxPti1T3xW", "LBBSYN1QKHJSY"
            )
        }
    }

    fun currentEnvironment(
        currentAuthentication: String?,
        context: Context,
    ): Boolean {
        var isValue = false
        when {
            (currentAuthentication == OAuthHelper.SANDBOX || currentAuthentication == OAuthHelper.UI_TESTING_SANDBOX) -> isValue =
                authorizeActivity.authorizeWithSandbox(authorizationManager!!)

            authorizeActivity.useOAuth -> authorizeActivity.authorizeWithOauth(context)
            else -> authorizeActivity.authorizeWithPAT(authorizationManager!!)
        }
//        if (isValue){
//            Log.d("TAG", "currentEnvironment: MockReaderUI 1 = $isValue ")
//            MockReaderUI.show()
//            Log.d("TAG", "currentEnvironment: MockReaderUI 121 ")
//        }
        return isValue
    }
}