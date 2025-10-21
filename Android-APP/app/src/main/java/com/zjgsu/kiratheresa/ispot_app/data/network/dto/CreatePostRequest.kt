package com.ispot.android.data.network.dto

data class CreatePostRequest(val userId: String, val content: String, val imageUrl: String? = null)

