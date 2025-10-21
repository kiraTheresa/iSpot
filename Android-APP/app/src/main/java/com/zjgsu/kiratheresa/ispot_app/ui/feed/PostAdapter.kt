package com.zjgsu.kiratheresa.ispot_app.ui.feed

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zjgsu.kiratheresa.ispot_app.R
import com.zjgsu.kiratheresa.ispot_app.ui.user.UserActivity
import com.zjgsu.kiratheresa.ispot_app.model.Post
import com.zjgsu.kiratheresa.ispot_app.data.network.NetworkModule
import com.zjgsu.kiratheresa.ispot_app.utils.TimeUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostAdapter(private val items: MutableList<Post>) : RecyclerView.Adapter<PostAdapter.Holder>() {

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar: ImageView = view.findViewById(R.id.ivAvatar)
        val tvUser: TextView = view.findViewById(R.id.tvUser)
        val tvContent: TextView = view.findViewById(R.id.tvContent)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val btnLike: Button = view.findViewById(R.id.btnLike)
        val btnComment: Button = view.findViewById(R.id.btnComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val p = items[position]
        holder.tvUser.text = p.userId
        holder.tvContent.text = p.content
        holder.tvTime.text = TimeUtils.prettyTime(holder.itemView.context, p.timestamp)
        // avatar placeholder
        Glide.with(holder.itemView).load(p.userId).placeholder(R.mipmap.ic_launcher).into(holder.ivAvatar)

        holder.btnLike.text = "点赞 ${p.likeCount}"
        holder.btnLike.setOnClickListener {
            // optimistic update
            p.likeCount += 1
            notifyItemChanged(position)
            NetworkModule.apiService.likePost(p.id).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {}
                override fun onFailure(call: Call<Void>, t: Throwable) {}
            })
        }

        holder.btnComment.setOnClickListener {
            // TODO: open comment activity (not implemented)
        }

        holder.ivAvatar.setOnClickListener {
            // open profile (UserActivity) by userId (we use userId as id)
            val ctx = holder.itemView.context
            val intent = Intent(ctx, UserActivity::class.java)
            intent.putExtra("userId", p.userId)
            ctx.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    fun update(newList: List<Post>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}
