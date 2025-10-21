package com.zjgsu.kiratheresa.ispot_app.service

import com.zjgsu.kiratheresa.ispot_app.model.LocationPoint
import android.annotation.SuppressLint
import android.content.Context
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationService(private val context: Context) {

    // 添加初始化状态
    private var isInitialized = false
    private var locationClient: AMapLocationClient? = null
    private var isLocationStarted = false

    // 模拟用户ID（实际应该从登录信息获取）
    private val currentUserId = "user_001"

    // 修正后的定位配置 - 使用正确的API调用方式
    private val locationOption by lazy {
        AMapLocationClientOption().apply {
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            isOnceLocation = false
            isNeedAddress = true
            isMockEnable = true
            interval = 5000
            isSensorEnable = true
            isGpsFirst = true
            httpTimeOut = 30000
            isLocationCacheEnable = true
        }
    }

    // 单次定位配置
    private val singleLocationOption by lazy {
        AMapLocationClientOption().apply {
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            isOnceLocation = true
            isNeedAddress = true
        }
    }

    private fun initLocationClient() {
        // 这里可以放置其他初始化逻辑
        // 主要的定位客户端初始化在具体使用的地方进行
    }

    private fun ensureInitialized() {
        if (!isInitialized) {
            initLocationClient()
            isInitialized = true
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(): Flow<LocationPoint> = callbackFlow {
        ensureInitialized()
        if (isLocationStarted) {
            return@callbackFlow
        }

        locationClient = AMapLocationClient(context.applicationContext).apply {
            setLocationOption(locationOption)
            setLocationListener(object : AMapLocationListener {
                override fun onLocationChanged(aMapLocation: AMapLocation?) {
                    aMapLocation?.let { location ->
                        if (location.errorCode == 0) {
                            // 定位成功，转换为 LocationPoint
                            val locationPoint = LocationPoint(
                                id = UUID.randomUUID().toString(),
                                userId = currentUserId,
                                lat = location.latitude,
                                lng = location.longitude,
                                timestamp = System.currentTimeMillis()
                            )
                            trySend(locationPoint)
                        } else {
                            // 定位失败，使用模拟数据
                            val mockLocation = createMockLocation()
                            trySend(mockLocation)
                        }
                    }
                }
            })
            startLocation()
        }

        isLocationStarted = true

        awaitClose {
            stopLocationUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): LocationPoint = suspendCoroutine { continuation ->
        ensureInitialized()
        val singleLocationClient = AMapLocationClient(context.applicationContext).apply {
            setLocationOption(singleLocationOption)
            setLocationListener(object : AMapLocationListener {
                override fun onLocationChanged(aMapLocation: AMapLocation?) {
                    aMapLocation?.let { location ->
                        if (location.errorCode == 0) {
                            val locationPoint = LocationPoint(
                                id = UUID.randomUUID().toString(),
                                userId = currentUserId,
                                lat = location.latitude,
                                lng = location.longitude,
                                timestamp = System.currentTimeMillis()
                            )
                            continuation.resume(locationPoint)
                        } else {
                            // 定位失败返回模拟位置
                            continuation.resume(createMockLocation())
                        }
                    }
                    this@apply.stopLocation()
                    this@apply.onDestroy()
                }
            })
            startLocation()
        }
    }

    // 创建模拟位置（杭州西湖附近）
    private fun createMockLocation(): LocationPoint {
        val baseLat = 30.2460
        val baseLng = 120.1377
        val randomOffset = (Math.random() - 0.5) * 0.01

        return LocationPoint(
            id = UUID.randomUUID().toString(),
            userId = currentUserId,
            lat = baseLat + randomOffset,
            lng = baseLng + randomOffset,
            timestamp = System.currentTimeMillis()
        )
    }

    fun stopLocationUpdates() {
        locationClient?.let {
            it.stopLocation()
            it.onDestroy()
        }
        locationClient = null
        isLocationStarted = false
    }

    fun isLocationStarted(): Boolean = isLocationStarted
}