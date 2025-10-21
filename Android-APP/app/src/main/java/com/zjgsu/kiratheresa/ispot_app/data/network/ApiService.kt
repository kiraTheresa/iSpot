package com.zjgsu.kiratheresa.ispot_app.data.network

import com.zjgsu.kiratheresa.ispot_app.data.model.Post
import com.zjgsu.kiratheresa.ispot_app.data.model.User
import com.zjgsu.kiratheresa.ispot_app.data.network.dto.CreatePostRequest
import com.zjgsu.kiratheresa.ispot_app.data.network.dto.LoginRequest
import com.zjgsu.kiratheresa.ispot_app.data.network.dto.LoginResponse
import com.zjgsu.kiratheresa.ispot_app.data.network.dto.RegisterRequest
import com.zjgsu.kiratheresa.ispot_app.data.network.dto.UserUpdateRequest
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("user/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("user/register")
    fun register(@Body request: RegisterRequest): Call<LoginResponse>

    @GET("user/{id}")
    fun getUser(@Path("id") userId: String): Call<User>

    @PUT("user/{id}")
    fun updateUser(@Path("id") userId: String, @Body body: UserUpdateRequest): Call<User>

    @GET("post/all")
    fun getAllPosts(): Call<List<Post>>

    @POST("post/create")
    fun createPost(@Body request: CreatePostRequest): Call<Post>

    @POST("post/{id}/like")
    fun likePost(@Path("id") postId: String): Call<Void>
}
