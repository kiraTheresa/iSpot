package com.zjgsu.kiratheresa.iblog.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.amap.api.maps.AMap
import com.amap.api.maps.model.Marker
import com.zjgsu.kiratheresa.iblog.R
import com.zjgsu.kiratheresa.iblog.manager.SocialMarkerManager
import com.zjgsu.kiratheresa.iblog.model.User
import com.zjgsu.kiratheresa.iblog.service.SocialService

class SocialInfoWindowAdapter(
    private val context: Context,
    private val aMap: AMap,
    private val socialMarkerManager: SocialMarkerManager,
    private val onViewProfile: (User) -> Unit,
    private val onSendMessage: (User) -> Unit,
    private val onAddFriend: (User) -> Unit,
    private val onViewPosts: (User) -> Unit
) : AMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View {
        return createView(marker)
    }

    override fun getInfoContents(marker: Marker): View? {
        return null
    }

    private fun createView(marker: Marker): View {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_social_info_window, null)

        val userId = socialMarkerManager.getUserByMarker(marker)
        userId?.let { id ->
            // 从服务中获取用户完整信息
            val allUsers = SocialService.getFriends() + SocialService.getNearbyUsers()
            val user = allUsers.find { it.id == id }
            user?.let {
                bindUserData(view, it, marker)
                setupClickListeners(view, it, marker) // 传递 marker 参数
            }
        }

        return view
    }

    private fun bindUserData(view: View, user: User, marker: Marker) {
        val tvUserName = view.findViewById<TextView>(R.id.tvUserName)
        val tvUserBio = view.findViewById<TextView>(R.id.tvUserBio)
        val tvUserStatus = view.findViewById<TextView>(R.id.tvUserStatus)
        val btnPrimaryAction = view.findViewById<Button>(R.id.btnPrimaryAction)
        val btnSecondaryAction = view.findViewById<Button>(R.id.btnSecondaryAction)
        val tvRelationship = view.findViewById<TextView>(R.id.tvRelationship)

        tvUserName.text = user.nickname ?: user.username
        tvUserBio.text = user.bio ?: "这个人很懒，什么都没有写"

        // 设置用户状态
        user.lastActiveTime?.let { lastActive ->
            val minutesAgo = (System.currentTimeMillis() - lastActive) / 60000
            tvUserStatus.text = when {
                minutesAgo < 5 -> "在线"
                minutesAgo < 60 -> "${minutesAgo}分钟前活跃"
                else -> "${minutesAgo / 60}小时前活跃"
            }
        } ?: run {
            tvUserStatus.text = "未知状态"
        }

        // 根据用户关系显示不同的操作按钮
        val isFriend = SocialService.getFriends().any { it.id == user.id }
        val hasPendingRequest = SocialService.getFriendRequests().any { it.fromUserId == user.id }

        when {
            isFriend -> {
                btnPrimaryAction.text = "发送消息"
                btnSecondaryAction.text = "查看动态"
                tvRelationship.text = "好友"
            }
            hasPendingRequest -> {
                btnPrimaryAction.text = "接受请求"
                btnSecondaryAction.text = "拒绝请求"
                tvRelationship.text = "好友请求"
            }
            else -> {
                btnPrimaryAction.text = "添加好友"
                btnSecondaryAction.text = "发送消息"
                tvRelationship.text = "附近的人"
            }
        }
    }

    private fun setupClickListeners(view: View, user: User, marker: Marker) {
        val btnPrimaryAction = view.findViewById<Button>(R.id.btnPrimaryAction)
        val btnSecondaryAction = view.findViewById<Button>(R.id.btnSecondaryAction)
        val btnViewProfile = view.findViewById<Button>(R.id.btnViewProfile)

        val isFriend = SocialService.getFriends().any { it.id == user.id }
        val hasPendingRequest = SocialService.getFriendRequests().any { it.fromUserId == user.id }

        btnPrimaryAction.setOnClickListener {
            when {
                isFriend -> onSendMessage(user)
                hasPendingRequest -> {
                    // 接受好友请求
                    val request = SocialService.getFriendRequests().find { it.fromUserId == user.id }
                    request?.id?.let { requestId ->
                        SocialService.handleFriendRequest(requestId, true)
                    }
                    onViewProfile(user)
                }
                else -> onAddFriend(user)
            }
            marker.hideInfoWindow() // 修正：在 marker 上调用
        }

        btnSecondaryAction.setOnClickListener {
            when {
                isFriend -> onViewPosts(user)
                hasPendingRequest -> {
                    // 拒绝好友请求
                    val request = SocialService.getFriendRequests().find { it.fromUserId == user.id }
                    request?.id?.let { requestId ->
                        SocialService.handleFriendRequest(requestId, false)
                    }
                    marker.hideInfoWindow() // 修正：在 marker 上调用
                }
                else -> onSendMessage(user)
            }
        }

        btnViewProfile.setOnClickListener {
            onViewProfile(user)
            marker.hideInfoWindow() // 修正：在 marker 上调用
        }
    }
}