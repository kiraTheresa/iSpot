package com.zjgsu.kiratheresa.ispot_app.model

data class Comment(
    val id: String,
    val postId: String,
    val userId: String,
    val content: String,
    val timestamp: Long
)
