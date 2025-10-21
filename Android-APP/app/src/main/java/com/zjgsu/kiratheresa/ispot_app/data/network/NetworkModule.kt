package com.zjgsu.kiratheresa.ispot_app.data.network

object NetworkModule {
    val apiService: ApiService by lazy {
        FakeApiService()
    }
}
