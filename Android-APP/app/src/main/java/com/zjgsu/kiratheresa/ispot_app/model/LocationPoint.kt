package com.zjgsu.kiratheresa.ispot_app.model

data class LocationPoint(
    val id: String,
    val userId: String,
    val lat: Double,
    val lng: Double,
    val timestamp: Long
)
