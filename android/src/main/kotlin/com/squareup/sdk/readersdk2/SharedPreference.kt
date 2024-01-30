package com.squareup.sdk.readersdk2

import android.R.attr.name
import android.content.Context
import android.content.Context.MODE_APPEND
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


public class SharedPreference {
    val MYSHREDPREGFERNCES="MYSHREDPREGFERNCES"
    var currentEnvertment = "currentEnvertment"
    public fun setData(context: Context,value : String){
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(MYSHREDPREGFERNCES, MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        myEdit.putString(currentEnvertment, value)
        myEdit.commit()
    }
    fun getData(context: Context): String? {
        val sh = context.getSharedPreferences(MYSHREDPREGFERNCES, MODE_PRIVATE);
        val s1 = sh.getString(currentEnvertment, "")
        return s1
    }
}