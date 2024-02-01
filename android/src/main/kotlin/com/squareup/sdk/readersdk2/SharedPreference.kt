package com.squareup.sdk.readersdk2

import android.R.attr.name
import android.content.Context
import android.content.Context.MODE_APPEND
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


public class SharedPreference {
    val MYSHAREDPREFERENCES = "MYSHAREDPREFERENCES"
    var currentEnvironment = "currentEnvironment"
    var currentAccessTokens = "currentAccessTokens"
    var currentLocationId = "currentLocationId"
    public fun setData(
        context: Context,
        currentEnv: String,
        currentToken: String,
        currentLocationID: String,
    ) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(MYSHAREDPREFERENCES, MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        myEdit.putString(currentEnvironment, currentEnv)
        myEdit.putString(currentAccessTokens, currentToken)
        myEdit.putString(currentLocationId, currentLocationID)
        myEdit.commit()
    }

    fun getData(context: Context): Triple<String?,String?,String?> {
        val sh = context.getSharedPreferences(MYSHAREDPREFERENCES, MODE_PRIVATE);
        val s1 = sh.getString(currentEnvironment, "")
        val s2 = sh.getString(currentAccessTokens, "")
        val s3 = sh.getString(currentLocationId, "")
        return Triple(s1,s2,s3)

    }
}