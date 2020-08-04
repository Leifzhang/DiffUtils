package com.kronos.sample

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.getTime(dateFormat: SimpleDateFormat): String {
    return dateFormat.format(Date(this))
}

fun Long.getTime(format: String): String {
    val dateFormat = SimpleDateFormat(format, Locale.CHINA)
    return getTime(dateFormat)
}

