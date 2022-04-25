package com.kronos.diffutil

import android.os.Parcel
import android.os.Parcelable
import java.util.concurrent.CopyOnWriteArrayList


class ParcelDiffHelper<T : Parcelable> : BaseDiffHelper<T>() {

    override fun clone() {
        itemsCursor?.run {
            snapshot = CopyOnWriteArrayList()
            forEach {
                snapshot?.add(it.deepCopy() ?: it)
            }
        }
    }

    private fun <T : Parcelable> T.deepCopy(): T? {
        var parcel: Parcel? = null
        return try {
            parcel = Parcel.obtain().also {
                it.writeParcelable(this, 0)
                it.setDataPosition(0)
            }
            parcel.readParcelable(this::class.java.classLoader)
        } finally {
            parcel?.recycle()
        }
    }
}

