package com.zjgsu.kiratheresa.ispot_app.utils

import android.content.Context
import android.text.format.DateUtils

object TimeUtils {
    fun prettyTime(context: Context, timeMs: Long): String {
        return DateUtils.getRelativeTimeSpanString(timeMs, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString()
    }
}
