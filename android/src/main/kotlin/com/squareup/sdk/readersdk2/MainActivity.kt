package com.squareup.sdk.readersdk2

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.sdk.reader2.ReaderSdk
import com.squareup.sdk.reader2.mockreader.ui.MockReaderUI
import com.squareup.sdk.readersdk2.auth.AuthorizeActivity
import com.squareup.sdk.readersdk2.permission.allRequiredPermissionsGranted
import io.flutter.embedding.android.FlutterActivity


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        toolbar = findViewById(R.id.toolbar)
        bottomNavigationView = findViewById(R.id.bottom_nav)

        setupNavigation()
    }

    private fun setupNavigation() {
        setSupportActionBar(toolbar)
        navController = findNavController(R.id.nav_host_fragment)
        bottomNavigationView.setupWithNavController(navController)

        toolbar.setupWithNavController(
            navController,
            AppBarConfiguration.Builder(ROOT_PAGES)
                .setDrawerLayout(null)
                .setFallbackOnNavigateUpListener { false }
                .build()
        )
        navController.addOnDestinationChangedListener {
                _, destination, _ ->
            updateNavVisibility(destination)
        }

        // Decide whether we need to navigate away from Keypad
        // If user is not authorized - launch Auth activity with its own flow
        val authorizationManager = ReaderSdk.authorizationManager()
        if (!authorizationManager.authorizationState.isAuthorized) {
            val intent = Intent(applicationContext, AuthorizeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        } else {
            // If in Sandbox - display Mock Reader floating icon
            if (ReaderSdk.isSandboxEnvironment()) {
                MockReaderUI.show()
            }

            // If user is authorized but didn't provide permissions - ask for them
            if (!allRequiredPermissionsGranted(applicationContext)) {
                if (navController.currentDestination?.id == R.id.keypad_dest) {
                    navController.navigate(R.id.action_keypad_to_permission)
                }
            }
        }

        navController.currentDestination?.let { updateNavVisibility(it) }
    }

    private fun updateNavVisibility(destination: NavDestination) {
        val visibility = when (destination.id) {
            R.id.permission_dest -> View.GONE
            R.id.charge_dest -> View.GONE
            else -> View.VISIBLE
        }

        toolbar.visibility = visibility
        bottomNavigationView.visibility = visibility

        // Change activity's title to match one of the tree root fragments
        if (ROOT_PAGES.contains(destination.id)) {
            title = destination.label
        }
    }

    companion object {
        // Root pages = pages for which the 'Up' navigation button shouldn't appear in Toolbar
        val ROOT_PAGES = setOf(R.id.keypad_dest, R.id.settings_dest, R.id.readers_dest)
    }
}