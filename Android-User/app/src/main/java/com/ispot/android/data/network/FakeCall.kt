package com.ispot.android.data.network

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

class FakeCall<T>(private val response: Response<T>) : Call<T> {
    private val executed = AtomicBoolean(false)
    private var canceled = false

    override fun clone(): Call<T> = FakeCall(response)

    override fun execute(): Response<T> {
        if (executed.getAndSet(true)) throw IOException("Already executed")
        if (canceled) throw IOException("Canceled")
        return response
    }

    override fun enqueue(callback: Callback<T>) {
        if (executed.getAndSet(true)) {
            callback.onFailure(this, IOException("Already executed"))
            return
        }
        if (canceled) {
            callback.onFailure(this, IOException("Canceled"))
            return
        }
        callback.onResponse(this, response)
    }

    override fun isExecuted(): Boolean = executed.get()

    override fun cancel() { canceled = true }

    override fun isCanceled(): Boolean = canceled

    override fun request(): Request = Request.Builder().url("http://localhost/").build()

    override fun timeout(): Timeout = Timeout.NONE
}
