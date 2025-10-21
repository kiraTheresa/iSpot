package com.zjgsu.kiratheresa.ispot_app.data.repository

import com.zjgsu.kiratheresa.ispot_app.data.model.User
import com.zjgsu.kiratheresa.ispot_app.data.network.NetworkModule
import com.zjgsu.kiratheresa.ispot_app.data.network.dto.LoginRequest
import com.zjgsu.kiratheresa.ispot_app.data.network.dto.LoginResponse
import com.zjgsu.kiratheresa.ispot_app.data.network.dto.RegisterRequest
import com.zjgsu.kiratheresa.ispot_app.data.network.dto.UserUpdateRequest
import retrofit2.Call

object UserRepository {
    private val api = NetworkModule.apiService

    fun login(username: String, password: String): Call<LoginResponse> =
        api.login(LoginRequest(username, password))

    fun register(username: String, password: String, nickname: String?) : Call<LoginResponse> =
        api.register(RegisterRequest(username, password, nickname))

    fun getUser(userId: String): Call<User> = api.getUser(userId)

    fun updateUser(userId: String, body: UserUpdateRequest): Call<User> = api.updateUser(userId, body)
}
