package com.squareup.sdk.readersdk2.permission

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

internal enum class SystemPermission(val permissionName: String, val apiLevel: Int) {
    BLUETOOTH_CONNECT(permission.BLUETOOTH_CONNECT, 31),
    BLUETOOTH_SCAN(permission.BLUETOOTH_SCAN, 31),
    LOCATION(permission.ACCESS_FINE_LOCATION, 24),
    MICROPHONE(permission.RECORD_AUDIO, 24),
    PHONE(permission.READ_PHONE_STATE, 24)
}

/**
 * Note that if the app's targetSdk is before 23, this method will return true as long as the
 * permission is in the manifest, regardless of the permission setting.
 */
internal fun SystemPermission.isGranted(context: Context) =
    ContextCompat.checkSelfPermission(context, permissionName) == PackageManager.PERMISSION_GRANTED

internal fun SystemPermission.requestPermission(
  fragment: Fragment,
  requestCode: Int,
) {
    fragment.requestPermissions(
        arrayOf(permissionName),
        requestCode
    )
}

internal fun allRequiredPermissionsGranted(context: Context) = if (Build.VERSION.SDK_INT >= 31) {
    SystemPermission.values().all { it.isGranted(context) }
} else {
    val apiLevelBelow31 = SystemPermission.values().filter { it.apiLevel < 31 }
    apiLevelBelow31.all { it.isGranted(context) }
}

internal fun String.toPermission() =
    SystemPermission.values().first { it.permissionName == this }
