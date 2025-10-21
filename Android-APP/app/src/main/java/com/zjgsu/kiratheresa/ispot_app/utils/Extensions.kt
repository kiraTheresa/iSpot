package com.zjgsu.kiratheresa.ispot_app.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadUrl(url: String?) {
    if (url.isNullOrBlank()) return
    Glide.with(this.context).load(url).into(this)
}
