package com.zjgsu.kiratheresa.ispot_app.ui.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.zjgsu.kiratheresa.ispot_app.R
import com.zjgsu.kiratheresa.ispot_app.model.Message

class MessageAdapter(private val context: Context, private val messages: List<Message>) : BaseAdapter() {
    override fun getCount(): Int = messages.size
    override fun getItem(position: Int): Any = messages[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_message, parent, false)
        val txt = v.findViewById<TextView>(R.id.txtMessage)
        val m = messages[position]
        txt.text = "${m.senderId}: ${m.content}"
        return v
    }
}
