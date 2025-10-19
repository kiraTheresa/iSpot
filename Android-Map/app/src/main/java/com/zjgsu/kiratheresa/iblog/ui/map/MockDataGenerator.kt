package com.zjgsu.kiratheresa.iblog.ui.map

import com.zjgsu.kiratheresa.iblog.model.LocationPoint
import com.zjgsu.kiratheresa.iblog.model.User
import java.util.UUID

object MockDataGenerator {

    // 生成模拟用户位置
    fun generateMockUsers(): List<User> {
        return listOf(
            User(
                id = "user_001",
                username = "user1",
                nickname = "小明",
                avatarUrl = null,
                bio = "热爱旅行",
                lat = 30.2500,
                lng = 120.1400,
                lastActiveTime = System.currentTimeMillis()
            ),
            User(
                id = "user_002",
                username = "user2",
                nickname = "小红",
                avatarUrl = null,
                bio = "摄影爱好者",
                lat = 30.2450,
                lng = 120.1350,
                lastActiveTime = System.currentTimeMillis() - 300000
            ),
            User(
                id = "user_003",
                username = "user3",
                nickname = "小刚",
                avatarUrl = null,
                bio = "户外运动",
                lat = 30.2420,
                lng = 120.1420,
                lastActiveTime = System.currentTimeMillis() - 600000
            )
        )
    }

    // 生成模拟历史轨迹
    fun generateMockTrajectory(userId: String, count: Int = 10): List<LocationPoint> {
        val baseLat = 30.2460
        val baseLng = 120.1377
        val points = mutableListOf<LocationPoint>()

        for (i in 0 until count) {
            val randomOffset = (Math.random() - 0.5) * 0.005
            points.add(
                LocationPoint(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    lat = baseLat + randomOffset,
                    lng = baseLng + randomOffset,
                    timestamp = System.currentTimeMillis() - (count - i) * 60000
                )
            )
        }
        return points
    }
}