package com.ispot.android.data.model

data class LocationPoint(
    val id: String,
    val userId: String,
    val lat: Double,
    val lng: Double,
    val timestamp: Long
)
