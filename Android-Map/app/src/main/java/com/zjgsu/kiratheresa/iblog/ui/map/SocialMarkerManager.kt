package com.zjgsu.kiratheresa.iblog.manager

import com.amap.api.maps.AMap
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.zjgsu.kiratheresa.iblog.model.MarkerInfo
import com.zjgsu.kiratheresa.iblog.model.MarkerType
import com.zjgsu.kiratheresa.iblog.model.User
import com.zjgsu.kiratheresa.iblog.service.SocialService

class SocialMarkerManager(private val aMap: AMap) {

    private val socialMarkers = mutableMapOf<String, Marker>()
    private val userMarkerMap = mutableMapOf<String, String>() // userId -> markerId

    // 社交标记点图标
    private val socialMarkerIcons = mapOf(
        "friend" to BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_friend),
        "nearby_user" to BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_nearby),
        "friend_request" to BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_request)
    )

    // 显示好友位置
    fun showFriendLocations(): List<MarkerInfo> {
        clearSocialMarkers()

        val friends = SocialService.getFriends()
        val markerInfos = mutableListOf<MarkerInfo>()

        friends.forEach { friend ->
            friend.lat?.let { lat ->
                friend.lng?.let { lng ->
                    val markerInfo = MarkerInfo(
                        id = "friend_${friend.id}",
                        userId = friend.id,
                        title = friend.nickname ?: friend.username,
                        snippet = "好友 - ${friend.bio ?: "暂无简介"}",
                        lat = lat,
                        lng = lng,
                        type = MarkerType.FRIEND,
                        user = friend
                    )

                    addSocialMarker(markerInfo, "friend")
                    markerInfos.add(markerInfo)
                    userMarkerMap[friend.id] = markerInfo.id
                }
            }
        }

        return markerInfos
    }

    // 显示附近用户
    fun showNearbyUsers(): List<MarkerInfo> {
        clearSocialMarkers()

        val nearbyUsers = SocialService.getNearbyUsers()
        val markerInfos = mutableListOf<MarkerInfo>()

        nearbyUsers.forEach { user ->
            user.lat?.let { lat ->
                user.lng?.let { lng ->
                    val markerInfo = MarkerInfo(
                        id = "nearby_${user.id}",
                        userId = user.id,
                        title = user.nickname ?: user.username,
                        snippet = "附近用户 - ${user.bio ?: "快来打个招呼吧"}",
                        lat = lat,
                        lng = lng,
                        type = MarkerType.USER,
                        user = user
                    )

                    addSocialMarker(markerInfo, "nearby_user")
                    markerInfos.add(markerInfo)
                    userMarkerMap[user.id] = markerInfo.id
                }
            }
        }

        return markerInfos
    }

    // 显示有好友请求的用户
    fun showFriendRequestUsers(): List<MarkerInfo> {
        clearSocialMarkers()

        val requests = SocialService.getFriendRequests()
        val markerInfos = mutableListOf<MarkerInfo>()

        requests.forEach { request ->
            // 查找发送请求的用户信息
            val allUsers = SocialService.getFriends() + SocialService.getNearbyUsers()
            val requestUser = allUsers.find { it.id == request.fromUserId }

            requestUser?.let { user ->
                user.lat?.let { lat ->
                    user.lng?.let { lng ->
                        val markerInfo = MarkerInfo(
                            id = "request_${request.id}",
                            userId = user.id,
                            title = user.nickname ?: user.username,
                            snippet = "好友请求: ${request.message ?: "想添加你为好友"}",
                            lat = lat,
                            lng = lng,
                            type = MarkerType.USER,
                            user = user
                        )

                        addSocialMarker(markerInfo, "friend_request")
                        markerInfos.add(markerInfo)
                        userMarkerMap[user.id] = markerInfo.id
                    }
                }
            }
        }

        return markerInfos
    }

    private fun addSocialMarker(markerInfo: MarkerInfo, iconType: String) {
        val markerOptions = MarkerOptions().apply {
            position(LatLng(markerInfo.lat, markerInfo.lng))
            title(markerInfo.title)
            snippet(markerInfo.snippet)
            icon(socialMarkerIcons[iconType] ?: socialMarkerIcons["nearby_user"])
            draggable(false)
        }

        val marker = aMap.addMarker(markerOptions)
        socialMarkers[markerInfo.id] = marker
    }

    fun clearSocialMarkers() {
        socialMarkers.values.forEach { it.remove() }
        socialMarkers.clear()
        userMarkerMap.clear()
    }

    fun getUserByMarker(marker: Marker): User? {
        val markerId = socialMarkers.entries.find { it.value == marker }?.key
        return markerId?.let { id ->
            // 从所有用户中查找
            val allUsers = SocialService.getFriends() + SocialService.getNearbyUsers()
            allUsers.find { user ->
                userMarkerMap[user.id] == id
            }
        }
    }

    fun getMarkerByUserId(userId: String): Marker? {
        val markerId = userMarkerMap[userId]
        return markerId?.let { socialMarkers[it] }
    }
}