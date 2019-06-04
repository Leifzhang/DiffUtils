package com.kronos.sample

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateHelper {
    fun getTime(timeInMillis: Long, dateFormat: SimpleDateFormat): String {
        return dateFormat.format(Date(timeInMillis))
    }


    fun getTime(timeInMillis: Long, format: String): String {
        val dateFormat = SimpleDateFormat(format, Locale.CHINA)
        return getTime(timeInMillis, dateFormat)
    }
}
