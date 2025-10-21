package com.zjgsu.kiratheresa.ispot_app.data.repository

import com.zjgsu.kiratheresa.ispot_app.model.Post
import com.zjgsu.kiratheresa.ispot_app.data.network.NetworkModule
import com.zjgsu.kiratheresa.ispot_app.data.network.dto.CreatePostRequest
import retrofit2.Call

object PostRepository {
    private val api = NetworkModule.apiService

    fun getAllPosts(): Call<List<Post>> = api.getAllPosts()
    fun createPost(req: CreatePostRequest): Call<Post> = api.createPost(req)
    fun likePost(postId: String) = api.likePost(postId)
}
