package com.zjgsu.kiratheresa.iblog.model

data class SocialInteraction(
    val userId: String,
    val targetId: String, // 标记点ID或用户ID
    val type: InteractionType,
    val timestamp: Long = System.currentTimeMillis()
)

enum class InteractionType {
    VIEW_PROFILE,    // 查看资料
    VIEW_POST,       // 查看动态
    SEND_MESSAGE,    // 发送消息
    ADD_FRIEND,      // 添加好友
    LIKE_POST,       // 点赞动态
    SHARE_LOCATION   // 分享位置
}