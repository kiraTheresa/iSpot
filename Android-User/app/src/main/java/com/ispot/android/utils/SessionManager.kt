package com.ispot.android.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("ispot_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) = prefs.edit().putString(KEY_TOKEN, token).apply()
    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun saveUserId(userId: String) = prefs.edit().putString(KEY_USER_ID, userId).apply()
    fun getUserId(): String? = prefs.getString(KEY_USER_ID, null)

    fun clear() = prefs.edit().clear().apply()

    companion object {
        private const val KEY_TOKEN = "key_token"
        private const val KEY_USER_ID = "key_user_id"
    }
}
