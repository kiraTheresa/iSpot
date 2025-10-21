package com.ispot.android.ui.feed

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ispot.android.R
import com.ispot.android.data.network.NetworkModule
import com.ispot.android.data.network.dto.CreatePostRequest
import com.ispot.android.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostActivity : AppCompatActivity() {

    private lateinit var etContent: EditText
    private lateinit var btnPost: Button
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        etContent = findViewById(R.id.etContent)
        btnPost = findViewById(R.id.btnPost)
        session = SessionManager(this)

        btnPost.setOnClickListener { submit() }
    }

    private fun submit() {
        val content = etContent.text.toString().trim()
        if (content.isEmpty()) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show()
            return
        }
        val userId = session.getUserId() ?: "u1"
        btnPost.isEnabled = false
        NetworkModule.apiService.createPost(CreatePostRequest(userId, content, null)).enqueue(object : Callback<com.ispot.android.data.model.Post> {
            override fun onResponse(call: Call<com.ispot.android.data.model.Post>, response: Response<com.ispot.android.data.model.Post>) {
                btnPost.isEnabled = true
                if (response.isSuccessful) {
                    Toast.makeText(this@PostActivity, "发布成功", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@PostActivity, "发布失败", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<com.ispot.android.data.model.Post>, t: Throwable) {
                btnPost.isEnabled = true
                Toast.makeText(this@PostActivity, "网络错误: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
