package com.zjgsu.kiratheresa.iblog.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zjgsu.kiratheresa.iblog.R

class MapFragment : Fragment() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        private const val DEFAULT_ZOOM_LEVEL = 15f
        private val DEFAULT_LOCATION = LatLng(39.9042, 116.4074) // 北京
    }

    // UI Components
    private lateinit var mapView: MapView
    private lateinit var aMap: AMap
    private lateinit var fabMyLocation: FloatingActionButton
    private lateinit var btnZoomIn: Button
    private lateinit var btnZoomOut: Button

    // Map state
    private var currentZoomLevel = DEFAULT_ZOOM_LEVEL
    private var isMapInitialized = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        initViews(view)
        setupMap(savedInstanceState)
        setupListeners()
        return view
    }

    private fun initViews(view: View) {
        mapView = view.findViewById(R.id.mapView)
        fabMyLocation = view.findViewById(R.id.fabMyLocation)
        btnZoomIn = view.findViewById(R.id.btnZoomIn)
        btnZoomOut = view.findViewById(R.id.btnZoomOut)
    }

    private fun setupMap(savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState)

        aMap = mapView.map ?: return

        // 基础地图配置
        configureMapSettings()

        // 设置默认位置
        moveToLocation(DEFAULT_LOCATION, DEFAULT_ZOOM_LEVEL)

        isMapInitialized = true
    }

    private fun configureMapSettings() {
        // 启用缩放控件
        aMap.uiSettings.isZoomControlsEnabled = false // 使用自定义控件
        aMap.uiSettings.isCompassEnabled = true // 显示指南针
        aMap.uiSettings.isScaleControlsEnabled = true // 显示比例尺
        aMap.uiSettings.isMyLocationButtonEnabled = false // 使用自定义定位按钮

        // 地图类型：普通地图
        aMap.mapType = AMap.MAP_TYPE_NORMAL

        // 启用定位图层
        aMap.isMyLocationEnabled = true

        // 设置地图点击监听
        aMap.setOnMapClickListener { latLng ->
            // 处理地图点击事件
            onMapClick(latLng)
        }

        // 设置相机移动监听
        aMap.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChange(position: CameraPosition) {
                // 相机位置变化
                currentZoomLevel = position.zoom
            }

            override fun onCameraChangeFinish(position: CameraPosition) {
                // 相机移动结束
                currentZoomLevel = position.zoom
            }
        })
    }

    private fun setupListeners() {
        // 定位按钮点击
        fabMyLocation.setOnClickListener {
            requestLocationPermissionAndMove()
        }

        // 缩放按钮
        btnZoomIn.setOnClickListener {
            zoomIn()
        }

        btnZoomOut.setOnClickListener {
            zoomOut()
        }
    }

    private fun requestLocationPermissionAndMove() {
        if (hasLocationPermission()) {
            moveToCurrentLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun moveToCurrentLocation() {
        // 这里先移动到默认位置，后续阶段会实现真实定位
        // TODO:  
        moveToLocation(DEFAULT_LOCATION, DEFAULT_ZOOM_LEVEL)
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

    private fun onMapClick(latLng: LatLng) {
        // 地图点击处理，后续阶段可以添加标记点等
        // showToast("点击位置: ${latLng.latitude}, ${latLng.longitude}")
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
            }
        }
    }

    // 生命周期管理
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}