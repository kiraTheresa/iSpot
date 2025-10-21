package com.zjgsu.kiratheresa.iblog.ui.fragment.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zjgsu.kiratheresa.iblog.R
import com.zjgsu.kiratheresa.ispot_app.manager.MarkerManager
import com.zjgsu.kiratheresa.ispot_app.manager.SocialMarkerManager
import com.zjgsu.kiratheresa.ispot_app.manager.TrajectoryManager
import com.zjgsu.kiratheresa.ispot_app.model.LocationPoint
import com.zjgsu.kiratheresa.ispot_app.model.MarkerInfo
import com.zjgsu.kiratheresa.ispot_app.model.MarkerType
import com.zjgsu.kiratheresa.ispot_app.model.User
import com.zjgsu.kiratheresa.ispot_app.service.LocationService
import com.zjgsu.kiratheresa.ispot_app.service.MarkerDataService
import com.zjgsu.kiratheresa.ispot_app.service.SocialService
import com.zjgsu.kiratheresa.iblog.ui.adapter.CustomInfoWindowAdapter
import com.zjgsu.kiratheresa.iblog.ui.adapter.SocialInfoWindowAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
    private var locationUpdatesJob: Job? = null
    private var currentMode: MapMode = MapMode.NORMAL

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    enum class MapMode {
        NORMAL, FRIENDS, NEARBY, REQUESTS
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map_social, container, false)
        initViews(view)
        setupMap(savedInstanceState)
//        setupManagers()
        setupListeners()
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


    // 添加延迟初始化
    private fun initializeLocationService() {
        if (!this::locationService.isInitialized) {
            locationService = LocationService(requireContext())
        }
    }

    private fun setupManagers() {
        trajectoryManager = TrajectoryManager(aMap)
//        locationService = LocationService(requireContext())
        markerManager = MarkerManager(aMap)
        socialMarkerManager = SocialMarkerManager(aMap)

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

    }

    private fun setupMarkerListeners() {
        markerManager.setOnMarkerClickListener { markerInfo ->
            false
        }

        markerManager.setOnInfoWindowClickListener { markerInfo ->
            onMarkerInfoWindowClick(markerInfo)
        }

        aMap.setOnMarkerClickListener { marker ->
            val userId = socialMarkerManager.getUserByMarker(marker)
            if (userId != null) {
                aMap.setInfoWindowAdapter(socialInfoWindowAdapter)
                marker.showInfoWindow()
                true
            } else {
                aMap.setInfoWindowAdapter(infoWindowAdapter)
                false
            }
        }
    }

    // 在地图加载完成后初始化管理器
    private fun setupMap(savedInstanceState: Bundle?) {

        try {
            mapView.onCreate(savedInstanceState)
            aMap = mapView.map ?: return

            // 其余初始化代码...
            configureMapSettings()
            moveToLocation(DEFAULT_LOCATION, DEFAULT_ZOOM_LEVEL)

            aMap.setOnMapLoadedListener {
                // 在地图完全加载后初始化管理器
                setupManagers()
                aMap.setInfoWindowAdapter(infoWindowAdapter)
                setupMarkerListeners()
                loadInitialData()
                isMapInitialized = true
            }

        } catch (e: Exception) {
            Log.e("MapFragment", "地图初始化失败", e)
            Toast.makeText(requireContext(), "地图初始化失败，请重试", Toast.LENGTH_LONG).show()
        }

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

        // 使用新的相机移动监听器
        aMap.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChange(cameraPosition: CameraPosition) {
                // 相机位置变化中
            }

            override fun onCameraChangeFinish(cameraPosition: CameraPosition) {
                currentZoomLevel = cameraPosition.zoom
            }
        })
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
        showPoiMarkers()
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
        // 可以更新其他UI显示社交统计信息
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
        Toast.makeText(requireContext(), "查看用户资料: ${user.nickname}", Toast.LENGTH_SHORT).show()
    }

    private fun onSendMessageToUser(user: User) {
        Toast.makeText(requireContext(), "发送消息给: ${user.nickname}", Toast.LENGTH_SHORT).show()
    }

    private fun onAddFriend(user: User) {
        val success = SocialService.sendFriendRequest(user.id, "你好，交个朋友吧！")
        if (success) {
            Toast.makeText(requireContext(), "好友请求已发送给 ${user.nickname}", Toast.LENGTH_SHORT).show()
            tvSocialInfo.text = "好友请求已发送给 ${user.nickname ?: user.username}"
        }
    }

    private fun onViewUserPosts(user: User) {
        val posts = SocialService.getUserPosts(user.id)
        Toast.makeText(requireContext(), "查看用户动态: ${posts.size}条", Toast.LENGTH_SHORT).show()
    }

    // 地图操作方法
    private fun onMapClick(latLng: LatLng) {
        // 地图点击处理
    }

    private fun onMapLongClick(latLng: LatLng) {
        // 地图长按处理
    }

    private fun onNavigateToMarker(markerInfo: MarkerInfo) {
        Toast.makeText(requireContext(), "导航到: ${markerInfo.title}", Toast.LENGTH_SHORT).show()
    }

    private fun onMarkerDetailClick(markerInfo: MarkerInfo) {
        Toast.makeText(requireContext(), "查看详情: ${markerInfo.title}", Toast.LENGTH_SHORT).show()
    }

    private fun onMarkerInfoWindowClick(markerInfo: MarkerInfo) {
        // 信息窗口点击处理
    }

    // 在需要使用时才初始化
    private fun startTracking() {
        if (checkLocationPermission()) {
            initializeLocationService() // 确保在使用前初始化
            isTracking = true
            trajectoryManager.startNewTrajectory()
            startLocationUpdates()
            btnStartTracking.isEnabled = false
            btnStopTracking.isEnabled = true
            tvTrackInfo.text = "轨迹记录中..."
        } else {
            requestLocationPermission()
        }
    }

    private fun stopTracking() {
        isTracking = false
        stopLocationUpdates()
        btnStartTracking.isEnabled = true
        btnStopTracking.isEnabled = false
        val distance = trajectoryManager.getTrajectoryDistance()
        tvTrackInfo.text = "轨迹长度: ${"%.2f".format(distance / 1000)} km"
    }

    private fun startLocationUpdates() {
        locationUpdatesJob = coroutineScope.launch {
            locationService.startLocationUpdates().collect { location ->
                updateCurrentLocation(location)
            }
        }
    }

    private fun stopLocationUpdates() {
        locationUpdatesJob?.cancel()
        locationService.stopLocationUpdates()
    }

    private fun updateCurrentLocation(location: LocationPoint) {
        if (isTracking) {
            trajectoryManager.addLocationToTrajectory(location)
        }
        // 更新当前位置标记
        currentLocationMarker?.remove()
        val latLng = LatLng(location.lat, location.lng)
        currentLocationMarker = aMap.addMarker(
            MarkerOptions().position(latLng).title("我的位置")
        )
    }

    private fun requestLocationPermissionAndMove() {
        if (checkLocationPermission()) {
            moveToCurrentLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun moveToCurrentLocation() {
        initializeLocationService() // 确保在使用前初始化
        coroutineScope.launch {
            try {
                val location = locationService.getCurrentLocation()
                val latLng = LatLng(location.lat, location.lng)
                moveToLocation(latLng, DEFAULT_ZOOM_LEVEL)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "获取位置失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun moveToLocation(latLng: LatLng, zoomLevel: Float) {
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
    }

    private fun zoomIn() {
        currentZoomLevel = (currentZoomLevel + 1).coerceAtMost(20f)
        aMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomLevel))
    }

    private fun zoomOut() {
        currentZoomLevel = (currentZoomLevel - 1).coerceAtLeast(3f)
        aMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomLevel))
    }

    private fun checkLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

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
            } else {
                Toast.makeText(requireContext(), "需要位置权限才能使用此功能", Toast.LENGTH_SHORT).show()
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
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}