package com.zjgsu.kiratheresa.ispot_app.utils

import android.app.Application
import com.amap.api.maps.MapsInitializer

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // 1）必须先调用隐私合规
        MapsInitializer.updatePrivacyShow(this, true, true)
        MapsInitializer.updatePrivacyAgree(this, true)

        // 2）SDK 其他初始化 —（保持空也可以，高德 10+ 无强制初始化入口）
        // MapsInitializer.setApiKey("你也可以在此设置 Key（可选）")
    }
}