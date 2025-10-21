package com.zjgsu.kiratheresa.ispot_app.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zjgsu.kiratheresa.ispot_app.R
import com.zjgsu.kiratheresa.ispot_app.data.network.NetworkModule
import com.zjgsu.kiratheresa.ispot_app.data.network.dto.LoginRequest
import com.zjgsu.kiratheresa.ispot_app.data.network.dto.LoginResponse
import com.zjgsu.kiratheresa.ispot_app.ui.social.SocialActivity
import com.zjgsu.kiratheresa.ispot_app.ui.register.RegisterActivity
import com.zjgsu.kiratheresa.ispot_app.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        session = SessionManager(this)
        // auto-login
        val saved = session.getUserId()
        if (saved != null) {
            startActivity(Intent(this, SocialActivity::class.java))
            finish()
            return
        }

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)

        btnLogin.setOnClickListener { attemptLogin() }
        btnRegister.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
    }

    private fun attemptLogin() {
        val u = etUsername.text.toString().trim()
        val p = etPassword.text.toString().trim()
        if (u.isEmpty() || p.isEmpty()) {
            Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show()
            return
        }
        btnLogin.isEnabled = false
        NetworkModule.apiService.login(LoginRequest(u, p)).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                btnLogin.isEnabled = true
                val body = response.body()
                if (body?.success == true && body.token != null && body.user != null) {
                    session.saveToken(body.token)
                    session.saveUserId(body.user.id)
                    Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, SocialActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, body?.message ?: "登录失败", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                btnLogin.isEnabled = true
                Toast.makeText(this@LoginActivity, "网络错误: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
