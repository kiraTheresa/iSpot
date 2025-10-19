package com.zjgsu.kiratheresa.iblog.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
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

    private var binding: LayoutSocialInfoWindowBinding? = null

    override fun getInfoWindow(marker: Marker): View {
        return createView(marker)
    }

    override fun getInfoContents(marker: Marker): View? {
        return null
    }

    private fun createView(marker: Marker): View {
        val binding = LayoutSocialInfoWindowBinding.inflate(
            LayoutInflater.from(context)
        )
        this.binding = binding

        val user = socialMarkerManager.getUserByMarker(marker)
        user?.let {
            bindUserData(binding, it, marker)
            setupClickListeners(binding, it)
        }

        return binding.root
    }

    private fun bindUserData(binding: LayoutSocialInfoWindowBinding, user: User, marker: Marker) {
        binding.tvUserName.text = user.nickname ?: user.username
        binding.tvUserBio.text = user.bio ?: "这个人很懒，什么都没有写"

        // 设置用户状态
        user.lastActiveTime?.let { lastActive ->
            val minutesAgo = (System.currentTimeMillis() - lastActive) / 60000
            binding.tvUserStatus.text = when {
                minutesAgo < 5 -> "在线"
                minutesAgo < 60 -> "${minutesAgo}分钟前活跃"
                else -> "${minutesAgo / 60}小时前活跃"
            }
        } ?: run {
            binding.tvUserStatus.text = "未知状态"
        }

        // 根据用户关系显示不同的操作按钮
        val isFriend = SocialService.getFriends().any { it.id == user.id }
        val hasPendingRequest = SocialService.getFriendRequests().any { it.fromUserId == user.id }

        when {
            isFriend -> {
                binding.btnPrimaryAction.text = "发送消息"
                binding.btnSecondaryAction.text = "查看动态"
                binding.tvRelationship.text = "好友"
                binding.tvRelationship.setTextColor(context.getColor(R.color.green))
            }
            hasPendingRequest -> {
                binding.btnPrimaryAction.text = "接受请求"
                binding.btnSecondaryAction.text = "拒绝请求"
                binding.tvRelationship.text = "好友请求"
                binding.tvRelationship.setTextColor(context.getColor(R.color.orange))
            }
            else -> {
                binding.btnPrimaryAction.text = "添加好友"
                binding.btnSecondaryAction.text = "发送消息"
                binding.tvRelationship.text = "附近的人"
                binding.tvRelationship.setTextColor(context.getColor(R.color.blue))
            }
        }
    }

    private fun setupClickListeners(binding: LayoutSocialInfoWindowBinding, user: User) {
        val isFriend = SocialService.getFriends().any { it.id == user.id }
        val hasPendingRequest = SocialService.getFriendRequests().any { it.fromUserId == user.id }

        binding.btnPrimaryAction.setOnClickListener {
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
            aMap.hideInfoWindow()
        }

        binding.btnSecondaryAction.setOnClickListener {
            when {
                isFriend -> onViewPosts(user)
                hasPendingRequest -> {
                    // 拒绝好友请求
                    val request = SocialService.getFriendRequests().find { it.fromUserId == user.id }
                    request?.id?.let { requestId ->
                        SocialService.handleFriendRequest(requestId, false)
                    }
                    aMap.hideInfoWindow()
                }
                else -> onSendMessage(user)
            }
        }

        binding.btnViewProfile.setOnClickListener {
            onViewProfile(user)
            aMap.hideInfoWindow()
        }
    }

    fun destroy() {
        binding = null
    }
}