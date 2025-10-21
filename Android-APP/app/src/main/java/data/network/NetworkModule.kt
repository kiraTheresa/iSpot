package com.ispot.android.data.network

object NetworkModule {
    val apiService: ApiService by lazy {
        FakeApiService()
    }
}
