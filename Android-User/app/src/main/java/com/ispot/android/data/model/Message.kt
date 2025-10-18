package com.ispot.android.data.model

data class Message(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val timestamp: Long,
    val read: Boolean = false
)

