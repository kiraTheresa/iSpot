package com.zjgsu.kiratheresa.iblog.service

import android.annotation.SuppressLint
import android.content.Context
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.zjgsu.kiratheresa.ispot_app.model.LocationPoint
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


    private fun initLocationClient() {
        // 将原来的初始化代码移到这里


    }

    private fun ensureInitialized() {
        if (!isInitialized) {
            // 延迟初始化关键组件
            initLocationClient()
            isInitialized = true
        }
    }

    // 定位配置 - 修正API调用方式
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

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(): Flow<LocationPoint> = callbackFlow {
        ensureInitialized() // 确保已初始化
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
        ensureInitialized() // 确保已初始化
        val singleLocationClient = AMapLocationClient(context.applicationContext).apply {
            setLocationOption(AMapLocationClientOption().apply {
                locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
                isOnceLocation = true
                isNeedAddress = true
            })
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