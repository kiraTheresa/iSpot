package com.zjgsu.kiratheresa.ispot_app.manager

import android.location.Location
import com.amap.api.maps.AMap
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Polyline
import com.amap.api.maps.model.PolylineOptions
import com.zjgsu.kiratheresa.ispot_app.model.LocationPoint

class TrajectoryManager(private val aMap: AMap) {

    private var currentTrajectory: Polyline? = null
    private val trajectoryPoints = mutableListOf<LatLng>()

    // 轨迹样式配置
    private val polylineOptions by lazy {
        PolylineOptions().apply {
            width(15f)
            color(0xFF2196F3.toInt())
            useGradient(true)
            setDottedLine(false)
        }
    }

    fun startNewTrajectory() {
        clearTrajectory()
        trajectoryPoints.clear()
    }

    fun addLocationToTrajectory(location: LocationPoint) {
        val latLng = LatLng(location.lat, location.lng)
        trajectoryPoints.add(latLng)
        updateTrajectoryOnMap()
    }

    fun addLocationsToTrajectory(locations: List<LocationPoint>) {
        locations.map { LatLng(it.lat, it.lng) }.let { latLngs ->
            trajectoryPoints.addAll(latLngs)
            updateTrajectoryOnMap()
        }
    }

    private fun updateTrajectoryOnMap() {
        currentTrajectory?.remove()

        if (trajectoryPoints.size >= 2) {
            currentTrajectory = aMap.addPolyline(
                polylineOptions.addAll(trajectoryPoints)
            )
        }
    }

    fun clearTrajectory() {
        currentTrajectory?.remove()
        currentTrajectory = null
        trajectoryPoints.clear()
    }

    fun getTrajectoryPoints(): List<LatLng> = trajectoryPoints.toList()

    fun getTrajectoryDistance(): Float {
        if (trajectoryPoints.size < 2) return 0f

        var totalDistance = 0f
        for (i in 1 until trajectoryPoints.size) {
            totalDistance += calculateDistance(
                trajectoryPoints[i - 1],
                trajectoryPoints[i]
            )
        }
        return totalDistance
    }

    private fun calculateDistance(point1: LatLng, point2: LatLng): Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            point1.latitude,
            point1.longitude,
            point2.latitude,
            point2.longitude,
            results
        )
        return results[0]
    }

    fun setTrajectoryStyle(
        width: Float = 15f,
        color: Int = 0xFF2196F3.toInt(),
        isDotted: Boolean = false
    ) {
        polylineOptions.width(width)
        polylineOptions.color(color)
        polylineOptions.setDottedLine(isDotted)
        updateTrajectoryOnMap()
    }
}