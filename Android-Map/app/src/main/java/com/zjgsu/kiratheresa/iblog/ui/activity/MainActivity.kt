package com.zjgsu.kiratheresa.iblog.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zjgsu.kiratheresa.iblog.R
import com.zjgsu.kiratheresa.iblog.ui.fragment.map.MapFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 加载地图Fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MapFragment())
                .commit()
        }
    }
}