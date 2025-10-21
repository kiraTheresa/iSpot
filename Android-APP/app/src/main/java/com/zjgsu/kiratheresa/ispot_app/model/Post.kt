package com.zjgsu.kiratheresa.ispot_app.model

data class Post(
    val id: String,
    val userId: String,
    val content: String,
    val imageUrl: String? = null,
    val timestamp: Long,
    var likeCount: Int = 0,
    var commentCount: Int = 0,
    var likedByMe: Boolean = false
)


