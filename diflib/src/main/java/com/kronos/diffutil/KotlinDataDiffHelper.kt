package com.kronos.diffutil

import android.util.Log

/**
 *
 *  @Author LiABao
 *  @Since 2021/6/3
 *
 */
class KotlinDataDiffHelper<T>(val copy: (T) -> T) : BaseDiffHelper<T>() {
    override fun clone() {
        try {
            val startTime = System.currentTimeMillis()
            snapshot = mutableListOf()
            itemsCursor?.forEach {
                snapshot?.add(copy.invoke(it))
            }
            val usage = System.currentTimeMillis() - startTime
            Log.i("ChannelDiffHelper", "time coast:${usage}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

