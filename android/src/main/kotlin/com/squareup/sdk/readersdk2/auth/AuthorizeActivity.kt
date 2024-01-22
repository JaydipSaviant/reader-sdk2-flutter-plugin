package com.squareup.sdk.readersdk2.auth

////Native Modules for react
//import com.facebook.react.bridge.ReactApplicationContext
//import com.facebook.react.bridge.ReactContextBaseJavaModule
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.squareup.sdk.reader2.ReaderSdk
import com.squareup.sdk.reader2.authorization.AuthorizationManager
import com.squareup.sdk.reader2.authorization.AuthorizeErrorCode
import com.squareup.sdk.reader2.authorization.AuthorizeErrorCode.NO_NETWORK
import com.squareup.sdk.reader2.authorization.AuthorizeErrorCode.USAGE_ERROR
import com.squareup.sdk.reader2.authorization.AuthorizedLocation
import com.squareup.sdk.reader2.core.CallbackReference
import com.squareup.sdk.reader2.core.Result.Failure
import com.squareup.sdk.reader2.core.Result.Success
import com.squareup.sdk.reader2.extensions.AuthorizeResult
import com.squareup.sdk.reader2.mockreader.ui.MockReaderUI
import com.squareup.sdk.readersdk2.DEMO_APP_PREFS
import com.squareup.sdk.readersdk2.MainActivity
import com.squareup.sdk.readersdk2.PREFS_AUTH_LOCATION_ID
import com.squareup.sdk.readersdk2.PREFS_AUTH_LOCATION_NAME
import com.squareup.sdk.readersdk2.R
import com.squareup.sdk.readersdk2.auth.OAuthHelper.Environment
import io.flutter.BuildConfig
import kotlin.system.exitProcess


class AuthorizeActivity : AppCompatActivity() {
    //(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext)
    fun getName() = "AuthorizeActivity"

    private var authData: Uri? = null
    private var authorizationManager: AuthorizationManager? = null
    private lateinit var callbackRef: CallbackReference
    private lateinit var prefs: SharedPreferences
    lateinit var currentEnvironment: Environment
    private lateinit var environments: List<Environment>
    var useOAuth: Boolean = false
    var value: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun initializeEnvironmentSelector() {
        val appSelector = findViewById<RadioGroup>(R.id.environment_selector)

        // Generate radio buttons for each environment
        environments.forEach { env ->
            val button = RadioButton(this)
            button.text = env.name
            button.id = env.viewId
            appSelector.addView(button)
        }

        // Set initially focused button based on current environment
        appSelector.check(currentEnvironment.viewId)

        appSelector.setOnCheckedChangeListener { _, checkedId ->
            appSelected(checkedId)
        }
    }

    override fun onResume() {
        super.onResume()
        authData = intent.data
        authorizationManager?.let { OAuthHelper.authorizeIfPossible(it, authData) }
    }

    override fun onDestroy() {
        super.onDestroy()
        callbackRef.clear()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(AUTH_DATA_KEY, authData.toString())
    }

    private fun appSelected(checkedId: Int) {
        MockReaderUI.hide()

        currentEnvironment = environments.first { it.viewId == checkedId }
        OAuthHelper.setEnvironmentName(this, currentEnvironment.name)

        restartApp(this)
    }

    fun authorizeWithSandbox(authorizationMan: AuthorizationManager): Boolean {

        return OAuthHelper.authorizeIfPossible(
            authorizationMan, Uri.parse(
                "mockurl://fake?" +
//                            "access_token=${currentEnvironment.authToken}&" +
//                            "location_id=${currentEnvironment.locationId}&" +
                        "access_token=EAAAEJe7nhcIDV2cXO1edJafX0ZNFQk42lqxVZjYn1kc3Tg7lN-P32GlFey5OepV&" + "location_id=LWTCANRWNHMF0&" + "authorization_code=unusedInSandbox"
            )
        )
    }

    fun authorizeWithOauth(context: Context) {
        Log.d(TAG, "authorizeWithOauth: 164")
        currentEnvironment = Environment(
            4,
            "Sandbox",
            "https://connect.squareup.com",
            "sandbox-sq0idb-7QT2EriOdn1Gz8jw7e2KSw",
            "EAAAFNbbmssq_Adi_nZhJXZ1n5Sg0So5eBeYLxAvJ0pfvMX1A_OFtlwxPti1T3xW",
            "LBBSYN1QKHJSY"
        )
        OAuthHelper.startOAuthFlow(currentEnvironment, context)
    }

    fun authorizeWithPAT(authorizationMan: AuthorizationManager) {
        Log.d(TAG, "authorizeWithPAT: ")
        authorizationMan.authorize(
            "EAAAFNbbmssq_Adi_nZhJXZ1n5Sg0So5eBeYLxAvJ0pfvMX1A_OFtlwxPti1T3xW", "LBBSYN1QKHJSY"
        )
        Log.d(TAG, "authorizeWithPAT: 12 -- ${authorizationMan.authorizationState.isAuthorized}")

//        authorizationMan.authorize(currentEnvironment.authToken, currentEnvironment.locationId)
    }

    fun onAuthorizeResult(result: AuthorizeResult) {
        Log.d(TAG, "onAuthorizeResult: $result ")
        when (result) {
            is Success -> {
                prefs.edit().apply {
                    putString(PREFS_AUTH_LOCATION_NAME, result.value.name)
                    putString(PREFS_AUTH_LOCATION_ID, result.value.locationId)
                    apply()
                }
                finishWithAuthorizedResult()
            }

            is Failure -> {
                when (result.errorCode) {
                    NO_NETWORK -> showRetryDialog(result)
                    USAGE_ERROR -> showUsageErrorDialog(result)
                }
            }
        }
    }

    private fun showRetryDialog(failure: Failure<AuthorizedLocation, AuthorizeErrorCode>) {
        AlertDialog.Builder(this).setTitle("Network Error").setMessage(failure.errorMessage)
            .setPositiveButton("Retry") { _, _ ->
                OAuthHelper.authorizeIfPossible(authorizationManager!!, authData)
            }.show()
    }

    private fun showUsageErrorDialog(failure: Failure<AuthorizedLocation, AuthorizeErrorCode>) {
        var dialogMessage = failure.errorMessage
        if (BuildConfig.DEBUG) {
            dialogMessage += "\n\nDebug Message: " + failure.debugMessage
            Log.d(
                TAG,
                failure.errorCode.toString() + ": " + failure.debugCode + ", " + failure.debugMessage
            )
        }
        AlertDialog.Builder(this).setTitle("Error").setMessage(dialogMessage)
            .setNeutralButton("ok") { _, _ -> authData = null }.show()
    }

    fun firstTimeCreateInten(context: Context) {
//        val context: Context = this@AuthorizeActivity
//        val applicationContext = context.applicationContext


        Log.d(TAG, "firstTimeCreateInten: ${context.applicationContext}")
        val intent = Intent(context, this::class.java)
        Log.d(TAG, "firstTimeCreateInten: 230 ${context.applicationContext}")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        Log.d(TAG, "firstTimeCreateInten: 232 ${context.applicationContext}")
        context.startActivity(intent)
        Log.d(TAG, "firstTimeCreateInten: 234 ${context.applicationContext}")
        finish()
        Log.d(TAG, "firstTimeCreateInten: 236 ${context.applicationContext}")
    }

    fun finishWithAuthorizedResult() {
        // Show MockReader UI if we're in Sandbox and logged in
        if (ReaderSdk.isSandboxEnvironment()) {
            MockReaderUI.show()
        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun restartApp(context: Context) {
        if (Build.VERSION.SDK_INT < 29) {
            // Since Android 10 we no longer can restart the app.
            // https://developer.android.com/guide/components/activities/background-starts
            val pendingIntent = PendingIntent.getActivity(
                context,
                1234,
                Intent(context, AuthorizeActivity::class.java),
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.set(
                AlarmManager.RTC, System.currentTimeMillis() + RESTART_DELAY_MS, pendingIntent
            )
            finish()
        }
        exitProcess(0)
    }

    companion object {
        private const val RESTART_DELAY_MS = 500L
        private const val AUTH_DATA_KEY = "AUTH_DATA_URI"
        private val TAG: String = AuthorizeActivity::class.java.simpleName
    }
}
