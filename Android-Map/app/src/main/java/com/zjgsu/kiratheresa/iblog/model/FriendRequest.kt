package com.zjgsu.kiratheresa.iblog.model

data class FriendRequest(
    val id: String,
    val fromUserId: String,
    val toUserId: String,
    val message: String? = null,
    var status: FriendRequestStatus = FriendRequestStatus.PENDING,
    val timestamp: Long = System.currentTimeMillis()
)

enum class FriendRequestStatus {
    PENDING,    // 待处理
    ACCEPTED,   // 已接受
    REJECTED,   // 已拒绝
    CANCELLED   // 已取消
}