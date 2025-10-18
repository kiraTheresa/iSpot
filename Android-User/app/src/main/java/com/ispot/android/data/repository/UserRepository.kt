package com.ispot.android.data.repository

import com.ispot.android.data.model.User
import com.ispot.android.data.network.NetworkModule
import com.ispot.android.data.network.dto.LoginRequest
import com.ispot.android.data.network.dto.RegisterRequest
import com.ispot.android.data.network.dto.UserUpdateRequest
import retrofit2.Call

object UserRepository {
    private val api = NetworkModule.apiService

    fun login(username: String, password: String): Call<com.ispot.android.data.network.dto.LoginResponse> =
        api.login(LoginRequest(username, password))

    fun register(username: String, password: String, nickname: String?) : Call<com.ispot.android.data.network.dto.LoginResponse> =
        api.register(RegisterRequest(username, password, nickname))

    fun getUser(userId: String): Call<User> = api.getUser(userId)

    fun updateUser(userId: String, body: UserUpdateRequest): Call<User> = api.updateUser(userId, body)
}
