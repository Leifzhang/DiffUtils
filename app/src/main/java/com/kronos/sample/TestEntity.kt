package com.kronos.sample

import android.os.Parcel
import android.os.Parcelable
import android.os.SystemClock
import android.text.TextUtils

import com.kronos.diffutil.IDifference
import com.kronos.diffutil.IEqualsAdapter

import java.util.Random

class TestEntity : Parcelable, IDifference, IEqualsAdapter {

    var id: Int = 0
    var displayTime: Long = 0
        private set
    var text: String? = null
        private set

    override val uniqueId: String
        get() = id.toString()

    fun update() {
        displayTime = System.currentTimeMillis()
        text = "更新数据"
    }


    constructor() {
        displayTime = System.currentTimeMillis()
        text = Random().nextInt(10000).toString()
    }

    override fun equals(obj: Any?): Boolean {
        return if (obj is TestEntity) {
            return if (displayTime == obj.displayTime) {
                true
            } else {
                TextUtils.equals(text, obj.text)
            }
        } else super.equals(obj)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(this.id)
        dest.writeLong(this.displayTime)
        dest.writeString(this.text)
    }

    protected constructor(`in`: Parcel) {
        this.id = `in`.readInt()
        this.displayTime = `in`.readLong()
        this.text = `in`.readString()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TestEntity> = object : Parcelable.Creator<TestEntity> {
            override fun createFromParcel(source: Parcel): TestEntity {
                return TestEntity(source)
            }

            override fun newArray(size: Int): Array<TestEntity?> {
                return arrayOfNulls(size)
            }
        }
    }
}
