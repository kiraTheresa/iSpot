package com.zjgsu.kiratheresa.iblog.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.maps.MapsInitializer
import com.zjgsu.kiratheresa.iblog.R
import com.zjgsu.kiratheresa.iblog.ui.fragment.map.MapFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 确保高德SDK提前初始化
        try {
            MapsInitializer.updatePrivacyShow(this, true, true)
            MapsInitializer.updatePrivacyAgree(this, true)
            // 其他高德SDK初始化配置
        } catch (e: Exception) {
            Log.e("AMap", "高德SDK初始化失败", e)
        }

        // 加载地图Fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MapFragment())
                .commit()
        }
    }
}