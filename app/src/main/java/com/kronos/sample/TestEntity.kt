package com.kronos.sample

import android.os.Parcel
import android.os.Parcelable
import android.os.SystemClock
import android.text.TextUtils

import com.kronos.diffutil.IDifference
import com.kronos.diffutil.IEqualsAdapter

import java.util.Random

data class TestEntity(var id: Int = 0,
                      var displayTime: Long = 0,
                      var text: String? = Random().nextInt(10000).toString()) : Parcelable, IDifference, IEqualsAdapter {

    override val uniqueId: String
        get() = id.toString()

    fun update() {
        displayTime = System.currentTimeMillis()
        text = "更新数据"
    }


    constructor(source: Parcel) : this(
            source.readInt(),
            source.readLong(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeLong(displayTime)
        writeString(text)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TestEntity> = object : Parcelable.Creator<TestEntity> {
            override fun createFromParcel(source: Parcel): TestEntity = TestEntity(source)
            override fun newArray(size: Int): Array<TestEntity?> = arrayOfNulls(size)
        }
    }
}