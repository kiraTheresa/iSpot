package com.zjgsu.kiratheresa.iblog.model

data class User(
    val id: String,
    val username: String,
    @Transient val password: String? = null, // 标记为不序列化
    val nickname: String? = null,
    val avatarUrl: String? = null,
    val bio: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val lastActiveTime: Long? = null
)