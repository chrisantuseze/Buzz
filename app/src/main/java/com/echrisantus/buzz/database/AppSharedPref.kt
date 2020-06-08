package com.echrisantus.buzz.database

import android.content.Context
import android.content.SharedPreferences

class AppSharedPref(val context: Context) {
    private val PREFS_NAME = "buzzPref"
    private val IS_LOGIN = "IS_LOGIN"

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveData(key: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun retrieveData(key: String): String? {
        return sharedPreferences.getString(key, key)
    }

    fun setLogin(isLogin: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(IS_LOGIN, isLogin)
        editor.apply()
    }

    fun isLogin(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGIN, false)
    }
}