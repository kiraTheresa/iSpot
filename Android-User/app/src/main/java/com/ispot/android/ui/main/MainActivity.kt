package com.ispot.android.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ispot.android.R
import com.ispot.android.ui.feed.FeedFragment
import com.ispot.android.ui.user.UserActivity
import com.ispot.android.utils.SessionManager

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
            startActivity(Intent(this, com.ispot.android.ui.login.LoginActivity::class.java))
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
