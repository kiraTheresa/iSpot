package com.zjgsu.kiratheresa.ispot_app.ui.social

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.zjgsu.kiratheresa.ispot_app.R
import com.zjgsu.kiratheresa.ispot_app.ui.feed.FeedFragment
import com.zjgsu.kiratheresa.ispot_app.ui.login.LoginActivity
import com.zjgsu.kiratheresa.ispot_app.ui.map.MapActivity // 请确保导入正确的MapActivity
import com.zjgsu.kiratheresa.ispot_app.ui.user.UserActivity
import com.zjgsu.kiratheresa.ispot_app.utils.SessionManager

class SocialActivity : AppCompatActivity() {

    private lateinit var btnTabFeed: Button
    private lateinit var btnTabProfile: Button
    private lateinit var btnGoToMap: Button // 新增的地图按钮
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social)
        session = SessionManager(this)

        // if not logged in, go to Login
        if (session.getUserId() == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        initViews()
        setupClickListeners()

        // default
        supportFragmentManager.beginTransaction().replace(R.id.container, FeedFragment()).commit()
    }

    private fun initViews() {
        btnTabFeed = findViewById(R.id.btnTabFeed)
        btnTabProfile = findViewById(R.id.btnTabProfile)
        btnGoToMap = findViewById(R.id.btnGoToMap) // 初始化地图按钮
    }

    private fun setupClickListeners() {
        // 动态标签点击事件
        btnTabFeed.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.container, FeedFragment()).commit()
        }

        // 我的标签点击事件
        btnTabProfile.setOnClickListener {
            startActivity(Intent(this, UserActivity::class.java))
        }

        // 跳转到地图按钮点击事件
        btnGoToMap.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
    }
}