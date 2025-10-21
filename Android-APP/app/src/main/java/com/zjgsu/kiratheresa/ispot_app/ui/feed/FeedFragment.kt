package com.ispot.android.ui.feed

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ispot.android.R
import com.ispot.android.data.model.Post
import com.ispot.android.data.network.NetworkModule
import com.ispot.android.ui.feed.PostActivity
import com.ispot.android.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedFragment : Fragment() {

    private lateinit var recyclerFeed: RecyclerView
    private lateinit var btnCreatePost: Button
    private val adapter = PostAdapter(mutableListOf())
    private lateinit var session: SessionManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_feed, container, false)
        recyclerFeed = v.findViewById(R.id.recyclerFeed)
        btnCreatePost = v.findViewById(R.id.btnCreatePost)
        session = SessionManager(requireContext())

        recyclerFeed.layoutManager = LinearLayoutManager(requireContext())
        recyclerFeed.adapter = adapter

        btnCreatePost.setOnClickListener {
            startActivity(Intent(requireContext(), PostActivity::class.java))
        }

        loadPosts()
        return v
    }

    override fun onResume() {
        super.onResume()
        loadPosts()
    }

    private fun loadPosts() {
        NetworkModule.apiService.getAllPosts().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                val list = response.body().orEmpty()
                adapter.update(list)
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) { /* ignore */ }
        })
    }
}
