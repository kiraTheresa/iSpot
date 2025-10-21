package com.zjgsu.kiratheresa.ispot_app.utils

import android.text.format.DateUtils
import android.content.Context

object TimeUtils {
    fun prettyTime(context: Context, timeMs: Long): String {
        return DateUtils.getRelativeTimeSpanString(timeMs, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString()
    }
}
