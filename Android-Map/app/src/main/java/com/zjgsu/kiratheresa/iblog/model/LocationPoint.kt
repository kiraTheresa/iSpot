package com.zjgsu.kiratheresa.iblog.model

data class LocationPoint(
    val id: String,
    val userId: String,
    val lat: Double,
    val lng: Double,
    val timestamp: Long
)
