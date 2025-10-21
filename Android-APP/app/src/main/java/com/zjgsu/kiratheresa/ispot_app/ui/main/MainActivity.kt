package com.zjgsu.kiratheresa.ispot_app.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.zjgsu.kiratheresa.ispot_app.R
import com.zjgsu.kiratheresa.ispot_app.ui.feed.FeedFragment
import com.zjgsu.kiratheresa.ispot_app.ui.login.LoginActivity
import com.zjgsu.kiratheresa.ispot_app.ui.user.UserActivity
import com.zjgsu.kiratheresa.ispot_app.utils.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var btnTabFeed: Button
    private lateinit var btnTabProfile: Button
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        session = SessionManager(this)
        // if not logged in, go to Login
        if (session.getUserId() == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        btnTabFeed = findViewById(R.id.btnTabFeed)
        btnTabProfile = findViewById(R.id.btnTabProfile)

        btnTabFeed.setOnClickListener { supportFragmentManager.beginTransaction().replace(R.id.container, FeedFragment()).commit() }
        btnTabProfile.setOnClickListener { startActivity(Intent(this, UserActivity::class.java)) }

        // default
        supportFragmentManager.beginTransaction().replace(R.id.container, FeedFragment()).commit()
    }
}
