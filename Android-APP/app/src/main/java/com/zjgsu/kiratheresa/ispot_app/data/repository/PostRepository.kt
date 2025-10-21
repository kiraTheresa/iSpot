package com.ispot.android.data.repository

import com.ispot.android.data.model.Post
import com.ispot.android.data.network.NetworkModule
import com.ispot.android.data.network.dto.CreatePostRequest
import retrofit2.Call

object PostRepository {
    private val api = NetworkModule.apiService

    fun getAllPosts(): Call<List<Post>> = api.getAllPosts()
    fun createPost(req: CreatePostRequest): Call<Post> = api.createPost(req)
    fun likePost(postId: String) = api.likePost(postId)
}
