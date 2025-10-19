package com.zjgsu.kiratheresa.iblog.manager

import com.amap.api.maps.AMap
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.zjgsu.kiratheresa.iblog.model.MarkerInfo
import com.zjgsu.kiratheresa.iblog.model.MarkerType

class MarkerManager(private val aMap: AMap) {

    private val markers = mutableMapOf<String, Marker>()
    private val markerInfoMap = mutableMapOf<String, MarkerInfo>()

    // 标记点图标配置
    private val markerIcons = mapOf(
        MarkerType.POI to BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_poi),
        MarkerType.USER to BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_user),
        MarkerType.CHECK_IN to BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_checkin),
        MarkerType.FRIEND to BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_friend),
        MarkerType.COLLECTION to BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_collection)
    )

    fun addMarker(markerInfo: MarkerInfo): Marker {
        val markerOptions = MarkerOptions().apply {
            position(LatLng(markerInfo.lat, markerInfo.lng))
            title(markerInfo.title)
            snippet(markerInfo.snippet)
            icon(markerIcons[markerInfo.type] ?: markerIcons[MarkerType.POI])
            draggable(false)
        }

        val marker = aMap.addMarker(markerOptions)
        markers[markerInfo.id] = marker
        markerInfoMap[markerInfo.id] = markerInfo

        return marker
    }

    fun addMarkers(markerInfos: List<MarkerInfo>) {
        markerInfos.forEach { addMarker(it) }
    }

    fun removeMarker(markerId: String) {
        markers[markerId]?.remove()
        markers.remove(markerId)
        markerInfoMap.remove(markerId)
    }

    fun removeAllMarkers() {
        markers.values.forEach { it.remove() }
        markers.clear()
        markerInfoMap.clear()
    }

    fun getMarkerInfo(markerId: String): MarkerInfo? {
        return markerInfoMap[markerId]
    }

    fun getMarkerInfoByMarker(marker: Marker): MarkerInfo? {
        return markerInfoMap.entries.find { it.value == markerInfoMap[it.key] }?.value
    }

    fun updateMarker(markerId: String, newInfo: MarkerInfo) {
        removeMarker(markerId)
        addMarker(newInfo)
    }

    fun showInfoWindow(markerId: String) {
        markers[markerId]?.showInfoWindow()
    }

    fun hideInfoWindow(markerId: String) {
        markers[markerId]?.hideInfoWindow()
    }

    fun getAllMarkers(): List<Marker> = markers.values.toList()

    fun getAllMarkerInfos(): List<MarkerInfo> = markerInfoMap.values.toList()

    fun getMarkersByType(type: MarkerType): List<Marker> {
        return markerInfoMap.values
            .filter { it.type == type }
            .mapNotNull { markers[it.id] }
    }

    // 设置标记点点击监听
    fun setOnMarkerClickListener(onMarkerClick: (MarkerInfo) -> Boolean) {
        aMap.setOnMarkerClickListener { marker ->
            val markerInfo = getMarkerInfoByMarker(marker)
            markerInfo?.let { onMarkerClick(it) } ?: false
        }
    }

    // 设置信息窗口点击监听
    fun setOnInfoWindowClickListener(onInfoWindowClick: (MarkerInfo) -> Unit) {
        aMap.setOnInfoWindowClickListener { marker ->
            val markerInfo = getMarkerInfoByMarker(marker)
            markerInfo?.let { onInfoWindowClick(it) }
        }
    }
}