package com.ispot.android.ui.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ispot.android.R
import com.zjgsu.kiratheresa.ispot_app.data.network.NetworkModule
import com.zjgsu.kiratheresa.ispot_app.data.network.dto.UserUpdateRequest
import com.zjgsu.kiratheresa.ispot_app.utils.SessionManager
import com.zjgsu.kiratheresa.ispot_app.data.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserActivity : AppCompatActivity() {

    private lateinit var ivAvatar: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var etNickname: EditText
    private lateinit var etBio: EditText
    private lateinit var btnSave: Button
    private lateinit var btnLogout: Button
    private lateinit var session: SessionManager
    private var userId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        ivAvatar = findViewById(R.id.ivAvatar)
        tvUsername = findViewById(R.id.tvUsername)
        etNickname = findViewById(R.id.etNickname)
        etBio = findViewById(R.id.etBio)
        btnSave = findViewById(R.id.btnSave)
        btnLogout = findViewById(R.id.btnLogout)
        session = SessionManager(this)

        userId = intent.getStringExtra("userId") ?: session.getUserId()
        if (userId == null) {
            startActivity(Intent(this, com.ispot.android.ui.login.LoginActivity::class.java))
            finish(); return
        }
        loadUser()

        btnSave.setOnClickListener { saveUser() }
        btnLogout.setOnClickListener {
            session.clear()
            startActivity(Intent(this, com.ispot.android.ui.login.LoginActivity::class.java))
            finish()
        }
    }

    private fun loadUser() {
        val id = userId ?: return
        NetworkModule.apiService.getUser(id).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val u = response.body() ?: return
                tvUsername.text = u.username
                etNickname.setText(u.nickname ?: "")
                etBio.setText(u.bio ?: "")
                Glide.with(this@UserActivity).load(u.avatarUrl ?: "").placeholder(R.mipmap.ic_launcher).into(ivAvatar)
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@UserActivity, "加载失败: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUser() {
        val id = userId ?: return
        val nick = etNickname.text.toString().trim().ifEmpty { null }
        val bio = etBio.text.toString().trim().ifEmpty { null }
        btnSave.isEnabled = false
        NetworkModule.apiService.updateUser(id, UserUpdateRequest(nick, null, bio)).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                btnSave.isEnabled = true
                if (response.isSuccessful) {
                    Toast.makeText(this@UserActivity, "保存成功", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@UserActivity, "保存失败", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                btnSave.isEnabled = true
                Toast.makeText(this@UserActivity, "网络错误: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
