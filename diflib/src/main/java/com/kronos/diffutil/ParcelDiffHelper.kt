package com.kronos.diffutil

import android.os.Parcel
import android.os.Parcelable
import java.util.concurrent.CopyOnWriteArrayList


class ParcelDiffHelper<T : Parcelable> : BaseDiffHelper<T>() {

    override fun clone() {
        try {
            itemsCursor?.apply {
                mData = CopyOnWriteArrayList()
                for (entity in this) {
                    val parcel = Parcel.obtain()
                    (entity as Parcelable).writeToParcel(parcel, 0)
                    parcel.setDataPosition(0)
                    val constructor = entity.javaClass.getDeclaredConstructor(Parcel::class.java)
                    constructor.isAccessible = true
                    val dateEntity = constructor.newInstance(parcel) as T
                    mData?.add(dateEntity)
                    parcel.recycle()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

