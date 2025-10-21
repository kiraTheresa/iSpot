package com.zjgsu.kiratheresa.iblog.service

import com.zjgsu.kiratheresa.ispot_app.model.FriendRequest
import com.zjgsu.kiratheresa.ispot_app.model.FriendRequestStatus
import com.zjgsu.kiratheresa.ispot_app.model.InteractionType
import com.zjgsu.kiratheresa.ispot_app.model.Message
import com.zjgsu.kiratheresa.ispot_app.model.Post
import com.zjgsu.kiratheresa.ispot_app.model.SocialInteraction
import com.zjgsu.kiratheresa.ispot_app.model.User
import java.util.UUID

object SocialService {

    // 统一当前用户ID
    private const val CURRENT_USER_ID = "user_001"

    // 模拟社交数据
    private val friends = mutableListOf<User>()
    private val friendRequests = mutableListOf<FriendRequest>()
    private val nearbyUsers = mutableListOf<User>()
    private val socialInteractions = mutableListOf<SocialInteraction>()

    init {
        initializeMockData()
    }

    private fun initializeMockData() {
        // 模拟好友数据
        friends.addAll(listOf(
            User(
                id = "user_friend_1",
                username = "xiaoming",
                nickname = "小明",
                avatarUrl = null,
                bio = "热爱旅行和摄影",
                lat = 30.2500,
                lng = 120.1400,
                lastActiveTime = System.currentTimeMillis()
            ),
            User(
                id = "user_friend_2",
                username = "xiaohong",
                nickname = "小红",
                avatarUrl = null,
                bio = "美食探店达人",
                lat = 30.2450,
                lng = 120.1350,
                lastActiveTime = System.currentTimeMillis() - 300000
            ),
            User(
                id = "user_friend_3",
                username = "xiaogang",
                nickname = "小刚",
                avatarUrl = null,
                bio = "户外运动爱好者",
                lat = 30.2420,
                lng = 120.1420,
                lastActiveTime = System.currentTimeMillis() - 600000
            )
        ))

        // 模拟附近用户
        nearbyUsers.addAll(listOf(
            User(
                id = "user_nearby_1",
                username = "user123",
                nickname = "旅行者",
                avatarUrl = null,
                bio = "刚刚来到这个城市",
                lat = 30.2480,
                lng = 120.1390,
                lastActiveTime = System.currentTimeMillis() - 120000
            ),
            User(
                id = "user_nearby_2",
                username = "photographer",
                nickname = "摄影师Leo",
                avatarUrl = null,
                bio = "专业摄影师，寻找美景",
                lat = 30.2430,
                lng = 120.1360,
                lastActiveTime = System.currentTimeMillis() - 180000
            )
        ))

        // 模拟好友请求
        friendRequests.add(
            FriendRequest(
                id = "req_1",
                fromUserId = "user_nearby_1",
                toUserId = CURRENT_USER_ID,
                message = "你好，看到你在附近，交个朋友吧！",
                status = FriendRequestStatus.PENDING
            )
        )
    }

    // 获取当前用户ID
    fun getCurrentUserId(): String = CURRENT_USER_ID

    // 获取好友列表
    fun getFriends(): List<User> {
        return friends.toList()
    }

    // 获取附近用户
    fun getNearbyUsers(maxDistance: Double = 2.0): List<User> {
        return nearbyUsers.toList()
    }

    // 获取好友请求
    fun getFriendRequests(): List<FriendRequest> {
        return friendRequests.filter { it.toUserId == CURRENT_USER_ID && it.status == FriendRequestStatus.PENDING }
    }

    // 发送好友请求
    fun sendFriendRequest(toUserId: String, message: String? = null): Boolean {
        val request = FriendRequest(
            id = UUID.randomUUID().toString(),
            fromUserId = CURRENT_USER_ID,
            toUserId = toUserId,
            message = message,
            status = FriendRequestStatus.PENDING
        )
        friendRequests.add(request)
        recordInteraction(toUserId, InteractionType.ADD_FRIEND)
        return true
    }

    // 处理好友请求
    fun handleFriendRequest(requestId: String, accepted: Boolean): Boolean {
        val request = friendRequests.find { it.id == requestId }
        request?.let {
            it.status = if (accepted) FriendRequestStatus.ACCEPTED else FriendRequestStatus.REJECTED

            if (accepted) {
                val user = nearbyUsers.find { user -> user.id == it.fromUserId }
                user?.let { friend ->
                    if (!friends.any { f -> f.id == friend.id }) {
                        friends.add(friend)
                    }
                }
            }
            return true
        }
        return false
    }

    // 查看用户资料
    fun viewUserProfile(userId: String) {
        recordInteraction(userId, InteractionType.VIEW_PROFILE)
    }

    // 发送消息
    fun sendMessage(toUserId: String, content: String): Message {
        recordInteraction(toUserId, InteractionType.SEND_MESSAGE)

        return Message(
            id = UUID.randomUUID().toString(),
            senderId = CURRENT_USER_ID,
            receiverId = toUserId,
            content = content,
            timestamp = System.currentTimeMillis()
        )
    }

    // 获取用户动态
    fun getUserPosts(userId: String): List<Post> {
        recordInteraction(userId, InteractionType.VIEW_POST)

        return listOf(
            Post(
                id = "post_1_${userId}",
                userId = userId,
                content = "今天在西湖边散步，风景真美！",
                imageUrl = null,
                timestamp = System.currentTimeMillis() - 3600000,
                likeCount = 5,
                commentCount = 2
            ),
            Post(
                id = "post_2_${userId}",
                userId = userId,
                content = "发现一家很棒的咖啡馆，推荐给大家！",
                imageUrl = null,
                timestamp = System.currentTimeMillis() - 86400000,
                likeCount = 12,
                commentCount = 5
            )
        )
    }

    // 点赞动态
    fun likePost(postId: String): Boolean {
        recordInteraction(postId, InteractionType.LIKE_POST)
        return true
    }

    // 分享位置
    fun shareLocation(targetUserId: String, lat: Double, lng: Double): Boolean {
        recordInteraction(targetUserId, InteractionType.SHARE_LOCATION)
        return true
    }

    // 记录社交互动
    private fun recordInteraction(targetId: String, type: InteractionType) {
        socialInteractions.add(
            SocialInteraction(
                userId = CURRENT_USER_ID,
                targetId = targetId,
                type = type
            )
        )
    }

    // 获取社交互动统计
    fun getInteractionStats(): Map<InteractionType, Int> {
        return socialInteractions
            .groupingBy { it.type }
            .eachCount()
    }
}