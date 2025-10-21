package com.zjgsu.kiratheresa.iblog.service

import com.zjgsu.kiratheresa.ispot_app.model.MarkerInfo
import com.zjgsu.kiratheresa.ispot_app.model.MarkerType
import java.util.UUID

object MarkerDataService {

    // 模拟标记点数据
    fun getMockMarkers(): List<MarkerInfo> {
        return listOf(
            MarkerInfo(
                id = "marker_1",
                userId = "user_001",
                title = "西湖风景区",
                snippet = "美丽的西湖，杭州的标志性景点",
                lat = 30.2460,
                lng = 120.1377,
                type = MarkerType.POI
            ),
            MarkerInfo(
                id = "marker_2",
                userId = "user_002",
                title = "雷峰塔",
                snippet = "白娘子传奇中的雷峰塔",
                lat = 30.2314,
                lng = 120.1475,
                type = MarkerType.POI
            ),
            MarkerInfo(
                id = "marker_3",
                userId = "user_003",
                title = "断桥残雪",
                snippet = "冬季赏雪的好去处",
                lat = 30.2550,
                lng = 120.1430,
                type = MarkerType.CHECK_IN
            ),
            MarkerInfo(
                id = "marker_4",
                userId = "user_004",
                title = "我的收藏点",
                snippet = "个人收藏的美丽景点",
                lat = 30.2400,
                lng = 120.1300,
                type = MarkerType.COLLECTION
            )
        )
    }

    // 模拟好友位置标记点 - 移除User对象引用
    fun getMockFriendMarkers(): List<MarkerInfo> {
        return listOf(
            MarkerInfo(
                id = "friend_1",
                userId = "user_friend_1",
                title = "小明的位置",
                snippet = "在线 - 刚刚活跃",
                lat = 30.2500,
                lng = 120.1400,
                type = MarkerType.FRIEND
            ),
            MarkerInfo(
                id = "friend_2",
                userId = "user_friend_2",
                title = "小红的位置",
                snippet = "在线 - 5分钟前活跃",
                lat = 30.2450,
                lng = 120.1350,
                type = MarkerType.FRIEND
            )
        )
    }

    // 添加新的标记点
    fun addMarker(
        title: String,
        snippet: String,
        lat: Double,
        lng: Double,
        type: MarkerType = MarkerType.POI,
        userId: String = "current_user"
    ): MarkerInfo {
        return MarkerInfo(
            id = UUID.randomUUID().toString(),
            userId = userId,
            title = title,
            snippet = snippet,
            lat = lat,
            lng = lng,
            type = type,
            timestamp = System.currentTimeMillis()
        )
    }
}