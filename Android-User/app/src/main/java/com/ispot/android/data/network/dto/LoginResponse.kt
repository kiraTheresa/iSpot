package com.ispot.android.data.network.dto

import com.ispot.android.data.model.User

data class LoginResponse(val success: Boolean, val message: String, val user: User?, val token: String? = null)

