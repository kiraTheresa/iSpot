package com.zjgsu.kiratheresa.ispot_app.data.network.dto

import com.zjgsu.kiratheresa.ispot_app.model.User

data class LoginResponse(val success: Boolean, val message: String, val user: User?, val token: String? = null)

