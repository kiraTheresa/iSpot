package com.zjgsu.kiratheresa.ispot_app.ui.comment

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zjgsu.kiratheresa.ispot_app.R
import com.zjgsu.kiratheresa.ispot_app.data.network.NetworkModule
import com.zjgsu.kiratheresa.ispot_app.model.Comment
import com.zjgsu.kiratheresa.ispot_app.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentActivity : AppCompatActivity() {
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: CommentAdapter
    private lateinit var input: EditText
    private lateinit var btnSend: Button
    private lateinit var session: SessionManager
    private var postId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        postId = intent.getStringExtra("postId") ?: ""
        session = SessionManager(this)
        recycler = findViewById(R.id.recyclerComments)
        input = findViewById(R.id.inputComment)
        btnSend = findViewById(R.id.btnSend)
        adapter = CommentAdapter()
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        loadComments()

        btnSend.setOnClickListener {
            val text = input.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener
            val userId = session.getUserId() ?: return@setOnClickListener
            NetworkModule.apiService.addComment(postId, mapOf("userId" to userId, "content" to text))
                .enqueue(object : Callback<Comment> {
                    override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                        response.body()?.let {
                            adapter.addComment(it)
                            input.text.clear()
                        }
                    }
                    override fun onFailure(call: Call<Comment>, t: Throwable) {
                        Toast.makeText(this@CommentActivity, "发送失败", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun loadComments() {
        NetworkModule.apiService.getComments(postId).enqueue(object : Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                response.body()?.let { adapter.setComments(it) }
            }
            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                Toast.makeText(this@CommentActivity, "加载失败", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
