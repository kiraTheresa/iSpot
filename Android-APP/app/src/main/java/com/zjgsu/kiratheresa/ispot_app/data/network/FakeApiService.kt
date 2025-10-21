package com.zjgsu.kiratheresa.ispot_app.data.network

import com.zjgsu.kiratheresa.ispot_app.model.Post
import com.zjgsu.kiratheresa.ispot_app.model.User
import com.zjgsu.kiratheresa.ispot_app.data.network.dto.CreatePostRequest
import com.zjgsu.kiratheresa.ispot_app.data.network.dto.LoginRequest
import com.zjgsu.kiratheresa.ispot_app.data.network.dto.LoginResponse
import com.zjgsu.kiratheresa.ispot_app.data.network.dto.RegisterRequest
import com.zjgsu.kiratheresa.ispot_app.data.network.dto.UserUpdateRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.atomic.AtomicInteger

class FakeApiService : ApiService {

    private val idGen = AtomicInteger(100)
    private val users = mutableListOf<User>()
    private val posts = mutableListOf<Post>()
    private val tokens = mutableMapOf<String, String>() // token -> userId

    init {
        val u1 = User(id = "u1", username = "alice", password = "123456", nickname = "Alice", avatarUrl = null, bio = "I am Alice")
        val u2 = User(id = "u2", username = "bob", password = "abcdef", nickname = "Bob", avatarUrl = null, bio = "Bob here")
        users.add(u1); users.add(u2)

        posts.add(Post(id = genId("p"), userId = u1.id, content = "Hello from Alice", timestamp = System.currentTimeMillis(), likeCount = 2))
        posts.add(Post(id = genId("p"), userId = u2.id, content = "Bob's post", timestamp = System.currentTimeMillis(), likeCount = 0))
    }

    private fun genId(prefix: String) = "$prefix${idGen.incrementAndGet()}"

    override fun login(request: LoginRequest): Call<LoginResponse> {
        val u = users.find { it.username == request.username && it.password == request.password }
        return if (u != null) {
            val token = "token-${genId("t")}"
            tokens[token] = u.id
            FakeCall(
                Response.success(
                    LoginResponse(
                        true,
                        "登录成功",
                        u.copy(password = null),
                        token
                    )
                )
            )
        } else {
            FakeCall(Response.success(LoginResponse(false, "用户名或密码错误", null, null)))
        }
    }

    override fun register(request: RegisterRequest): Call<LoginResponse> {
        if (users.any { it.username == request.username }) {
            return FakeCall(Response.success(LoginResponse(false, "用户名已存在", null, null)))
        }
        val id = genId("u")
        val u = User(id = id, username = request.username, password = request.password, nickname = request.nickname, avatarUrl = null, bio = null)
        users.add(u)
        val token = "token-${genId("t")}"
        tokens[token] = u.id
        return FakeCall(
            Response.success(
                LoginResponse(
                    true,
                    "注册成功",
                    u.copy(password = null),
                    token
                )
            )
        )
    }

    override fun getUser(userId: String): Call<User> {
        val u = users.find { it.id == userId }?.copy(password = null)
        return if (u != null) FakeCall(Response.success(u)) else FakeCall(
            Response.error(
                404,
                ResponseBody.create(null, "not found")
            )
        )
    }

    override fun updateUser(userId: String, body: UserUpdateRequest): Call<User> {
        val idx = users.indexOfFirst { it.id == userId }
        if (idx == -1) return FakeCall(Response.error(404, ResponseBody.create(null, "not found")))
        val cur = users[idx]
        val updated = cur.copy(nickname = body.nickname ?: cur.nickname, avatarUrl = body.avatarUrl ?: cur.avatarUrl, bio = body.bio ?: cur.bio)
        users[idx] = updated
        return FakeCall(Response.success(updated.copy(password = null)))
    }

    override fun getAllPosts(): Call<List<Post>> {
        val snapshot = posts.sortedByDescending { it.timestamp }.map { it.copy() }
        return FakeCall(Response.success(snapshot))
    }

    override fun createPost(request: CreatePostRequest): Call<Post> {
        val post = Post(id = genId("p"), userId = request.userId, content = request.content, imageUrl = request.imageUrl, timestamp = System.currentTimeMillis(), likeCount = 0)
        posts.add(0, post)
        return FakeCall(Response.success(post))
    }

    override fun likePost(postId: String): Call<Void> {
        val idx = posts.indexOfFirst { it.id == postId }
        if (idx != -1) posts[idx].likeCount = posts[idx].likeCount + 1
        return FakeCall(Response.success(null))
    }
}
