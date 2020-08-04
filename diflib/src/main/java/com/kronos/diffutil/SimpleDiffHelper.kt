package com.kronos.diffutil

import java.util.concurrent.CopyOnWriteArrayList

class SimpleDiffHelper<T> : BaseDiffHelper<T>() {

    override fun clone() {
        try {
            itemsCursor?.apply {
                mData = CopyOnWriteArrayList(this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}