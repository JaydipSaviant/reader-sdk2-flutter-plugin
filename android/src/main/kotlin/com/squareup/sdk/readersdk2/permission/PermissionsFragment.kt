package com.squareup.sdk.readersdk2.permission

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.squareup.sdk.readersdk2.R
import com.squareup.sdk.readersdk2.permission.SystemPermission.BLUETOOTH_CONNECT
import com.squareup.sdk.readersdk2.permission.SystemPermission.BLUETOOTH_SCAN
import com.squareup.sdk.readersdk2.permission.SystemPermission.LOCATION
import com.squareup.sdk.readersdk2.permission.SystemPermission.MICROPHONE
import com.squareup.sdk.readersdk2.permission.SystemPermission.PHONE

class PermissionsFragment : Fragment(R.layout.permissions_fragment) {

    private lateinit var bluetoothConnect: PermissionsBox
    private lateinit var bluetoothScan: PermissionsBox
    private lateinit var location: PermissionsBox
    private lateinit var microphone: PermissionsBox
    private lateinit var phone: PermissionsBox
    private lateinit var launchAppButton: Button

    override fun onViewCreated(
      view: View,
      savedInstanceState: Bundle?,
    ) {
        if (allRequiredPermissionsGranted(requireContext())) {
            navigateToNextScreen()
        }

        launchAppButton = view.findViewById(R.id.start_taking_payments)
        launchAppButton.setOnClickListener {
            navigateToNextScreen()
        }

        microphone = view.findViewById(R.id.microphone)
        microphone.setUp(
            permission = MICROPHONE,
            title = R.string.microphone_title,
            description = R.string.microphone_subtitle,
        )

        phone = view.findViewById(R.id.phone)
        phone.setUp(
            permission = PHONE,
            title = R.string.phone_title,
            description = R.string.phone_subtitle
        )
        location = view.findViewById(R.id.location)
        location.setUp(
            permission = LOCATION,
            title = R.string.location_title,
            description = R.string.location_subtitle,
        )

        // System permissions required if the App is running on the API 31 and up.
        if (Build.VERSION.SDK_INT >= 31) {
            bluetoothScan = view.findViewById(R.id.bluetooth_scan)
            bluetoothScan.visibility = View.VISIBLE
            bluetoothScan.setUp(
                permission = BLUETOOTH_SCAN,
                title = R.string.bluetooth_scan_title,
                description = R.string.bluetooth_scan_subtitle,
            )

            bluetoothConnect = view.findViewById(R.id.bluetooth_connect)
            bluetoothConnect.visibility = View.VISIBLE
            bluetoothConnect.setUp(
                permission = BLUETOOTH_CONNECT,
                title = R.string.bluetooth_connect_title,
                description = R.string.bluetooth_connect_subtitle,
            )
            checkPermission(BLUETOOTH_SCAN)
            checkPermission(BLUETOOTH_CONNECT)
        }

        checkPermission(LOCATION)
        checkPermission(MICROPHONE)
        checkPermission(PHONE)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun PermissionsBox.setUp(
        permission: SystemPermission,
        @StringRes title: Int,
        @StringRes description: Int,
    ) {
        setTitle(resources.getString(title))
        setSubtitle(resources.getString(description))
        setOnClickListener {
            requestPermission(permission)
        }
    }

    private fun navigateToNextScreen() {
        findNavController().navigate(R.id.action_permission_to_keypad)
    }

    private fun requestPermission(
      permission: SystemPermission,
    ) {
        if (permission.isGranted(requireContext())) {
            onPermissionGranted(permission)
            return
        }

        permission.requestPermission(this, PERMISSIONS_REQUEST_CODE)
    }

    private fun onPermissionGranted(permission: SystemPermission) {
        when (permission) {
            BLUETOOTH_CONNECT -> bluetoothConnect.setChecked()
            BLUETOOTH_SCAN -> bluetoothScan.setChecked()
            LOCATION -> location.setChecked()
            MICROPHONE -> microphone.setChecked()
            PHONE -> phone.setChecked()
        }
        if (allRequiredPermissionsGranted(requireContext())) {
            launchAppButton.isEnabled = true
        }
    }

    private fun checkPermission(permission: SystemPermission) {
        if (permission.isGranted(requireContext())) {
            onPermissionGranted(permission)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        for (i in permissions.indices) {
            val permission = permissions[i].toPermission()
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission)
            }
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 100
    }
}
