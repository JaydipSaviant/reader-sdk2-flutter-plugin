package com.squareup.sdk.readersdk2.auth

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.squareup.sdk.reader2.authorization.AuthorizationManager
import com.squareup.sdk.readersdk2.R

object OAuthHelper {
    private const val AUTH_CODES_TEMPLATE = "%s/oauth2/authorize?client_id=%s&" +
            "scope=PAYMENTS_WRITE,PAYMENTS_WRITE_IN_PERSON,MERCHANT_PROFILE_READ" +
            ",PAYMENTS_WRITE_ADDITIONAL_RECIPIENTS"

    private const val ENVIRONMENT_KEY = "ENVIRONMENT"
    private const val SHARED_PREFS_NAME = "reader_sdk_demo_2_activity_prefs"

    const val PRODUCTION = "Production"
    const val SANDBOX = "Sandbox"
    const val UI_TESTING_SANDBOX = "Testing Sandbox"

    fun setEnvironmentName(
        context: Context,
        environmentName: String,
    ) {
        val sharedPreferences = context.getSharedPreferences(
            SHARED_PREFS_NAME,
            AppCompatActivity.MODE_PRIVATE
        )
        sharedPreferences.edit(commit = true) {
            putString(ENVIRONMENT_KEY, environmentName)
        }
    }

    /**
     * Returns currently set environment's name or Production if initial environment selection hasn't
     * happened yet.
     */
    fun getCurrentEnvironmentName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(
            SHARED_PREFS_NAME,
            AppCompatActivity.MODE_PRIVATE
        )
        return sharedPreferences.getString(ENVIRONMENT_KEY, PRODUCTION)!!
    }

    /**
     * Returns application id for current environment or for Production if it is first app launch.
     */
    fun getAppId(context: Context): String {
        val currentEnvironmentName = getCurrentEnvironmentName(context)
        val currentEnvironment = getAllEnvironments(context)
            .firstOrNull { it.name == currentEnvironmentName }
        checkNotNull(currentEnvironment) {
            "Configuration for environment \"$currentEnvironmentName\" not found. Make sure that " +
                    "environments.xml contains this configuration."
        }
        return currentEnvironment.applicationId
    }

    /**
     * Inspects the parameters used in [.onCreate] and tries to (re)authorize if
     * possible.
     *
     * @param authorizationManager the ReaderSdk2 auth manager to authorize with
     * @param uri the URI from the instance state, if any
     * @return `true` if authorization was attempted (regardless of success, which is
     * asynchronous)
     */
    @SuppressWarnings("UnusedPrivateMember")
    fun authorizeIfPossible(
        authorizationManager: AuthorizationManager,
        uri: Uri?,
    ): Boolean {
        Log.d("TAG", "onCreate: 79 $uri")
        if (uri != null) {
            checkNotNull(uri.getQueryParameter("authorization_code")) { "Missing authorization code!" }
            val locationId = uri.getQueryParameter("location_id") ?: error("Missing location id!")
            val accessToken =
                uri.getQueryParameter("access_token") ?: error("Missing access token!")
            val mac = uri.getQueryParameter("authorization_code") ?: ""
            authorizationManager.authorize(accessToken, locationId)
            return true
        }
        return false
    }

    fun startOAuthFlow(
        environment: Environment,
        context: Context,
    ) {
        val urlString =
            String.format(AUTH_CODES_TEMPLATE, environment.urlBase, environment.applicationId)
        val intent = Intent(ACTION_VIEW)
        intent.data = Uri.parse(urlString)
        context.startActivity(intent)
    }

    fun getAllEnvironments(context: Context): List<Environment> {
        with(context.resources) {
            val scopes = getStringArray(R.array.sqrsdk_2_environment_scopes)
            val urls = getStringArray(R.array.sqrsdk_2_environment_url_base)
            val applicationIds = getStringArray(R.array.sqrsdk_2_environment_application_id)
            val authTokens = getStringArray(R.array.sqrsdk_2_environment_auth_token)
            val locationIds = getStringArray(R.array.sqrsdk_2_environment_location_id)

            return scopes.indices.map { i ->
                Environment(
                    viewId = View.generateViewId(),
                    name = scopes[i],
                    urlBase = urls[i],
                    applicationId = applicationIds[i],
                    authToken = authTokens[i],
                    locationId = locationIds[i]
                )
            }
        }
    }

    data class Environment(
        val viewId: Int,
        val name: String,
        val urlBase: String,
        val applicationId: String,
        val authToken: String,
        val locationId: String,
    )
}
