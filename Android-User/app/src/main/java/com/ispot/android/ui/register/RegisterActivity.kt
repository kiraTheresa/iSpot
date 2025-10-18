package com.ispot.android.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ispot.android.R
import com.ispot.android.data.network.NetworkModule
import com.ispot.android.data.network.dto.RegisterRequest
import com.ispot.android.ui.main.MainActivity
import com.ispot.android.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etNickname: EditText
    private lateinit var btnSubmit: Button
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        session = SessionManager(this)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        etNickname = findViewById(R.id.etNickname)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener { attemptRegister() }
    }

    private fun attemptRegister() {
        val u = etUsername.text.toString().trim()
        val p = etPassword.text.toString().trim()
        val n = etNickname.text.toString().trim().ifEmpty { null }
        if (u.isEmpty() || p.isEmpty()) {
            Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show()
            return
        }
        btnSubmit.isEnabled = false
        NetworkModule.apiService.register(RegisterRequest(u, p, n)).enqueue(object : Callback<com.ispot.android.data.network.dto.LoginResponse> {
            override fun onResponse(call: Call<com.ispot.android.data.network.dto.LoginResponse>, response: Response<com.ispot.android.data.network.dto.LoginResponse>) {
                btnSubmit.isEnabled = true
                val body = response.body()
                if (body?.success == true && body.token != null && body.user != null) {
                    session.saveToken(body.token)
                    session.saveUserId(body.user.id)
                    Toast.makeText(this@RegisterActivity, "注册成功", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@RegisterActivity, body?.message ?: "注册失败", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<com.ispot.android.data.network.dto.LoginResponse>, t: Throwable) {
                btnSubmit.isEnabled = true
                Toast.makeText(this@RegisterActivity, "网络错误: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
