package com.zjgsu.kiratheresa.iblog.model

data class MarkerInfo(
    val id: String,
    val userId: String,
    val title: String,
    val snippet: String? = null,
    val lat: Double,
    val lng: Double,
    val type: MarkerType = MarkerType.POI,
    val timestamp: Long = System.currentTimeMillis(),
    val userName: String? = null, // 改为用户名而非完整User对象
    val userAvatar: String? = null // 改为头像URL
)
enum class MarkerType {
    POI,        // 兴趣点
    USER,       // 用户位置
    CHECK_IN,   // 打卡点
    FRIEND,     // 好友位置
    COLLECTION  // 收藏点
}