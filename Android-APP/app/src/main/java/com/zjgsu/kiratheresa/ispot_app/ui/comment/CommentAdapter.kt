package com.zjgsu.kiratheresa.ispot_app.ui.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zjgsu.kiratheresa.ispot_app.R
import com.zjgsu.kiratheresa.ispot_app.model.Comment
import java.text.SimpleDateFormat
import java.util.*

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.VH>() {
    private val items = mutableListOf<Comment>()

    fun setComments(list: List<Comment>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun addComment(c: Comment) {
        items.add(c)
        notifyItemInserted(items.size - 1)
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val txtContent: TextView = v.findViewById(R.id.txtCommentContent)
        val txtTime: TextView = v.findViewById(R.id.txtCommentTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return VH(v)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val c = items[position]
        holder.txtContent.text = c.content
        holder.txtTime.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(c.timestamp))
    }
}
