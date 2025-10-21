package com.zjgsu.kiratheresa.ispot_app.data.network.dto

data class CreatePostRequest(val userId: String, val content: String, val imageUrl: String? = null)

