package com.squareup.sdk.readersdk2.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.squareup.sdk.reader2.ReaderSdk
import com.squareup.sdk.reader2.mockreader.ui.MockReaderUI
import com.squareup.sdk.readersdk2.DEMO_APP_PREFS
import com.squareup.sdk.readersdk2.PREFS_AUTH_LOCATION_ID
import com.squareup.sdk.readersdk2.PREFS_AUTH_LOCATION_NAME
import com.squareup.sdk.readersdk2.R
import com.squareup.sdk.readersdk2.auth.AuthorizeActivity
import com.squareup.sdk.readersdk2.auth.OAuthHelper

/**
 * The "Settings" screen includes de-authentication button
 */
class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var prefs: SharedPreferences
    private var locationName: Preference? = null
    private var locationId: Preference? = null

    override fun onCreatePreferences(
      savedInstanceState: Bundle?,
      rootKey: String?,
    ) {
        setPreferencesFromResource(R.xml.setting_screen, rootKey)
    }

    override fun onViewCreated(
      view: View,
      savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        findPreference<Preference>(getString(R.string.log_out_button_key))?.run {
            setOnPreferenceClickListener {
                deauthorize()
                true
            }
        }

        findPreference<Preference>(getString(R.string.version_button_key))?.run {
            // summary = BuildConfig.READER_SDK_RELEASE_VERSION
            setOnClickCopyToClipboard()
        }

        findPreference<Preference>(getString(R.string.environment_button_key))?.run {
            summary = OAuthHelper.getCurrentEnvironmentName(context)
            setOnClickCopyToClipboard()
        }
        findPreference<Preference>(getString(R.string.application_id_button_key))?.run {
            summary = OAuthHelper.getAppId(context, "").toString()
            setOnClickCopyToClipboard()
        }

        locationName = findPreference(getString(R.string.location_name_button_key))
        locationId = findPreference(getString(R.string.location_id_button_key))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            prefs = it.getSharedPreferences(DEMO_APP_PREFS, MODE_PRIVATE)
        }
        locationName?.run {
            summary = prefs.getString(
                PREFS_AUTH_LOCATION_NAME,
                getString(R.string.settings_not_available)
            )
            setOnClickCopyToClipboard()
        }
        locationId?.run {
            summary = prefs.getString(
                PREFS_AUTH_LOCATION_ID,
                getString(R.string.settings_not_available)
            )
            setOnClickCopyToClipboard()
        }
    }

    private fun Preference.setOnClickCopyToClipboard() {
        setOnPreferenceClickListener {
            val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            clipboard?.setPrimaryClip(ClipData.newPlainText(title, summary))
            Toast.makeText(context, R.string.text_copied, Toast.LENGTH_LONG).show()
            true
        }
    }

    /**
     * De-authorizes, which takes us back to the login screen because we can't be here without being
     * authorized.
     */
    private fun deauthorize() {
        ReaderSdk.authorizationManager().deauthorize()

        MockReaderUI.hide()

        // Redirect user to Authorization screen
        val intent = Intent(context, AuthorizeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
