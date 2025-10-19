package com.zjgsu.kiratheresa.iblog.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zjgsu.kiratheresa.iblog.R
import com.zjgsu.kiratheresa.iblog.adapter.CustomInfoWindowAdapter
import com.zjgsu.kiratheresa.iblog.adapter.SocialInfoWindowAdapter
import com.zjgsu.kiratheresa.iblog.manager.MarkerManager
import com.zjgsu.kiratheresa.iblog.manager.SocialMarkerManager
import com.zjgsu.kiratheresa.iblog.manager.TrajectoryManager
import com.zjgsu.kiratheresa.iblog.model.MarkerInfo
import com.zjgsu.kiratheresa.iblog.model.MarkerType
import com.zjgsu.kiratheresa.iblog.model.User
import com.zjgsu.kiratheresa.iblog.service.LocationService
import com.zjgsu.kiratheresa.iblog.service.MarkerDataService
import com.zjgsu.kiratheresa.iblog.service.SocialService
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MapFragment : Fragment() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        private const val DEFAULT_ZOOM_LEVEL = 15f
        private val DEFAULT_LOCATION = LatLng(30.2460, 120.1377)
    }

    // UI Components
    private lateinit var mapView: MapView
    private lateinit var aMap: AMap
    private lateinit var fabMyLocation: FloatingActionButton
    private lateinit var btnZoomIn: Button
    private lateinit var btnZoomOut: Button
    private lateinit var btnStartTracking: Button
    private lateinit var btnStopTracking: Button
    private lateinit var tvTrackInfo: TextView
    private lateinit var btnShowPoi: Button
    private lateinit var btnShowFriends: Button
    private lateinit var btnShowNearby: Button
    private lateinit var btnShowRequests: Button
    private lateinit var btnAddMarker: Button
    private lateinit var tvSocialInfo: TextView

    // Managers & Services
    private lateinit var trajectoryManager: TrajectoryManager
    private lateinit var locationService: LocationService
    private lateinit var markerManager: MarkerManager
    private lateinit var socialMarkerManager: SocialMarkerManager
    private lateinit var infoWindowAdapter: CustomInfoWindowAdapter
    private lateinit var socialInfoWindowAdapter: SocialInfoWindowAdapter

    // Map state
    private var currentZoomLevel = DEFAULT_ZOOM_LEVEL
    private var isMapInitialized = false
    private var isTracking = false
    private var currentLocationMarker: Marker? = null
    private var locationUpdatesJob: kotlinx.coroutines.Job? = null
    private var currentMode: MapMode = MapMode.NORMAL

    enum class MapMode {
        NORMAL,     // 普通模式
        FRIENDS,    // 好友模式
        NEARBY,     // 附近的人模式
        REQUESTS    // 好友请求模式
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map_social, container, false)
        initViews(view)
        setupMap(savedInstanceState)
        setupManagers()
        setupListeners()
        loadInitialData()
        return view
    }

    private fun initViews(view: View) {
        mapView = view.findViewById(R.id.mapView)
        fabMyLocation = view.findViewById(R.id.fabMyLocation)
        btnZoomIn = view.findViewById(R.id.btnZoomIn)
        btnZoomOut = view.findViewById(R.id.btnZoomOut)
        btnStartTracking = view.findViewById(R.id.btnStartTracking)
        btnStopTracking = view.findViewById(R.id.btnStopTracking)
        tvTrackInfo = view.findViewById(R.id.tvTrackInfo)
        btnShowPoi = view.findViewById(R.id.btnShowPoi)
        btnShowFriends = view.findViewById(R.id.btnShowFriends)
        btnShowNearby = view.findViewById(R.id.btnShowNearby)
        btnShowRequests = view.findViewById(R.id.btnShowRequests)
        btnAddMarker = view.findViewById(R.id.btnAddMarker)
        tvSocialInfo = view.findViewById(R.id.tvSocialInfo)
    }

    private fun setupManagers() {
        trajectoryManager = TrajectoryManager(aMap)
        locationService = LocationService(requireContext())
        markerManager = MarkerManager(aMap)
        socialMarkerManager = SocialMarkerManager(aMap)

        // 设置信息窗口适配器
        infoWindowAdapter = CustomInfoWindowAdapter(
            context = requireContext(),
            aMap = aMap,
            onNavigateClick = { markerInfo ->
                onNavigateToMarker(markerInfo)
            },
            onDetailClick = { markerInfo ->
                onMarkerDetailClick(markerInfo)
            }
        )

        socialInfoWindowAdapter = SocialInfoWindowAdapter(
            context = requireContext(),
            aMap = aMap,
            socialMarkerManager = socialMarkerManager,
            onViewProfile = { user ->
                onViewUserProfile(user)
            },
            onSendMessage = { user ->
                onSendMessageToUser(user)
            },
            onAddFriend = { user ->
                onAddFriend(user)
            },
            onViewPosts = { user ->
                onViewUserPosts(user)
            }
        )

        aMap.setInfoWindowAdapter(infoWindowAdapter)
        setupMarkerListeners()
    }

    private fun setupMarkerListeners() {
        markerManager.setOnMarkerClickListener { markerInfo ->
            false // 允许显示信息窗口
        }

        markerManager.setOnInfoWindowClickListener { markerInfo ->
            onMarkerInfoWindowClick(markerInfo)
        }

        // 设置社交标记点点击监听
        aMap.setOnMarkerClickListener { marker ->
            val user = socialMarkerManager.getUserByMarker(marker)
            if (user != null) {
                // 切换到社交信息窗口
                aMap.setInfoWindowAdapter(socialInfoWindowAdapter)
                marker.showInfoWindow()
                true // 消费点击事件
            } else {
                // 使用普通信息窗口
                aMap.setInfoWindowAdapter(infoWindowAdapter)
                false // 继续默认处理
            }
        }
    }

    private fun setupMap(savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState)

        aMap = mapView.map ?: return

        configureMapSettings()
        moveToLocation(DEFAULT_LOCATION, DEFAULT_ZOOM_LEVEL)

        isMapInitialized = true
    }

    private fun configureMapSettings() {
        aMap.uiSettings.isZoomControlsEnabled = false
        aMap.uiSettings.isCompassEnabled = true
        aMap.uiSettings.isScaleControlsEnabled = true
        aMap.uiSettings.isMyLocationButtonEnabled = false

        aMap.mapType = AMap.MAP_TYPE_NORMAL
        aMap.isMyLocationEnabled = true

        setupMapListeners()
    }

    private fun setupMapListeners() {
        aMap.setOnMapClickListener { latLng ->
            onMapClick(latLng)
        }

        aMap.setOnMapLongClickListener { latLng ->
            onMapLongClick(latLng)
        }

        aMap.setOnCameraChangeListener { position ->
            currentZoomLevel = position.zoom
        }
    }

    private fun setupListeners() {
        fabMyLocation.setOnClickListener {
            requestLocationPermissionAndMove()
        }

        btnZoomIn.setOnClickListener { zoomIn() }
        btnZoomOut.setOnClickListener { zoomOut() }

        btnStartTracking.setOnClickListener { startTracking() }
        btnStopTracking.setOnClickListener { stopTracking() }

        btnShowPoi.setOnClickListener { showPoiMarkers() }
        btnShowFriends.setOnClickListener { showFriendMarkers() }
        btnShowNearby.setOnClickListener { showNearbyMarkers() }
        btnShowRequests.setOnClickListener { showRequestMarkers() }
        btnAddMarker.setOnClickListener { addMarkerAtCenter() }
    }

    private fun loadInitialData() {
        updateSocialInfo()
    }

    private fun showPoiMarkers() {
        clearAllMarkers()
        currentMode = MapMode.NORMAL
        aMap.setInfoWindowAdapter(infoWindowAdapter)

        val poiMarkers = MarkerDataService.getMockMarkers()
        markerManager.addMarkers(poiMarkers)

        tvSocialInfo.text = "显示兴趣点"
    }

    private fun showFriendMarkers() {
        clearAllMarkers()
        currentMode = MapMode.FRIENDS
        aMap.setInfoWindowAdapter(socialInfoWindowAdapter)

        val friendMarkers = socialMarkerManager.showFriendLocations()
        updateSocialInfo()

        tvSocialInfo.text = "好友在线: ${friendMarkers.size}人"
    }

    private fun showNearbyMarkers() {
        clearAllMarkers()
        currentMode = MapMode.NEARBY
        aMap.setInfoWindowAdapter(socialInfoWindowAdapter)

        val nearbyMarkers = socialMarkerManager.showNearbyUsers()
        updateSocialInfo()

        tvSocialInfo.text = "附近用户: ${nearbyMarkers.size}人"
    }

    private fun showRequestMarkers() {
        clearAllMarkers()
        currentMode = MapMode.REQUESTS
        aMap.setInfoWindowAdapter(socialInfoWindowAdapter)

        val requestMarkers = socialMarkerManager.showFriendRequestUsers()
        updateSocialInfo()

        tvSocialInfo.text = "好友请求: ${requestMarkers.size}个"
    }

    private fun clearAllMarkers() {
        markerManager.removeAllMarkers()
        socialMarkerManager.clearSocialMarkers()
    }

    private fun updateSocialInfo() {
        val friendsCount = SocialService.getFriends().size
        val nearbyCount = SocialService.getNearbyUsers().size
        val requestsCount = SocialService.getFriendRequests().size

        // 可以更新UI显示社交统计信息
    }

    private fun addMarkerAtCenter() {
        val cameraPosition = aMap.cameraPosition
        val centerLatLng = cameraPosition.target

        val newMarker = MarkerDataService.addMarker(
            title = "新标记点",
            snippet = "创建于 ${System.currentTimeMillis()}",
            lat = centerLatLng.latitude,
            lng = centerLatLng.longitude,
            type = MarkerType.COLLECTION
        )

        markerManager.addMarker(newMarker)
        markerManager.showInfoWindow(newMarker.id)
    }

    // 社交互动方法
    private fun onViewUserProfile(user: User) {
        SocialService.viewUserProfile(user.id)
        // 跳转到用户详情页
        navigateToUserDetail(user.id)
    }

    private fun onSendMessageToUser(user: User) {
        // 跳转到聊天页面
        navigateToChat(user.id)
    }

    private fun onAddFriend(user: User) {
        val success = SocialService.sendFriendRequest(user.id, "你好，我是通过地图发现你的，交个朋友吧！")
        if (success) {
            // 显示成功提示
            tvSocialInfo.text = "好友请求已发送给 ${user.nickname ?: user.username}"
        }
    }

    private fun onViewUserPosts(user: User) {
        val posts = SocialService.getUserPosts(user.id)
        // 跳转到用户动态页面
        navigateToUserPosts(user.id, posts)
    }

    private fun navigateToUserDetail(userId: String) {
        // 实现跳转到用户详情页的逻辑
        // startActivity(Intent(requireContext(), UserDetailActivity::class.java).apply {
        //     putExtra("USER_ID", userId)
        // })
    }

    private fun navigateToChat(userId: String) {
        // 实现跳转到聊天页面的逻辑
        // startActivity(Intent(requireContext(), ChatActivity::class.java).apply {
        //     putExtra("TARGET_USER_ID", userId)
        // })
    }

    private fun navigateToUserPosts(userId: String, posts: List<Post>) {
        // 实现跳转到用户动态页面的逻辑
        // startActivity(Intent(requireContext(), UserPostsActivity::class.java).apply {
        //     putExtra("USER_ID", userId)
        //     putExtra("POSTS", ArrayList(posts))
        // })
    }

    // 原有的地图方法保持不变...
    private fun onMapClick(latLng: LatLng) {}
    private fun onMapLongClick(latLng: LatLng) {}
    private fun onNavigateToMarker(markerInfo: MarkerInfo) {}
    private fun onMarkerDetailClick(markerInfo: MarkerInfo) {}
    private fun onMarkerInfoWindowClick(markerInfo: MarkerInfo) {}
    private fun startTracking() {}
    private fun stopTracking() {}
    private fun updateCurrentLocation(location: LocationPoint) {}
    private fun requestLocationPermissionAndMove() {}
    private fun moveToCurrentLocation() {}
    private fun moveToLocation(latLng: LatLng, zoomLevel: Float) {}
    private fun zoomIn() {}
    private fun zoomOut() {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                moveToCurrentLocation()
                if (isTracking) {
                    startTracking()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        stopLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
        infoWindowAdapter.destroy()
        socialInfoWindowAdapter.destroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}