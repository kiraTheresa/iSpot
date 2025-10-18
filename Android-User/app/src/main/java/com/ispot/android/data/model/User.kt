package com.ispot.android.data.model

data class User(
    val id: String,
    val username: String,
    val password: String? = null,
    val nickname: String? = null,
    val avatarUrl: String? = null,
    val bio: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val lastActiveTime: Long? = null
)


