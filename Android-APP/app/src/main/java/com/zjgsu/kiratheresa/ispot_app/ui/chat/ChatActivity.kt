package com.zjgsu.kiratheresa.ispot_app.ui.chat

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.zjgsu.kiratheresa.ispot_app.R
import com.zjgsu.kiratheresa.ispot_app.data.model.Message

class ChatActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button
    private val messages = mutableListOf<Message>()
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        listView = findViewById(R.id.listViewMessages)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)
        adapter = MessageAdapter(this, messages)
        listView.adapter = adapter

        btnSend.setOnClickListener {
            val text = etMessage.text.toString().trim()
            if (text.isNotEmpty()) {
                val msg = Message("m${messages.size + 1}", "me", "you", text, System.currentTimeMillis(), false)
                messages.add(msg)
                adapter.notifyDataSetChanged()
                etMessage.setText("")
            }
        }
    }
}
