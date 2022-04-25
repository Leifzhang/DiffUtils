package com.kronos.sample.entity

import android.os.Parcelable
import com.kronos.diffutil.IDifference
import com.kronos.diffutil.IEqualsAdapter
import kotlinx.parcelize.Parcelize
import java.util.Random

@Parcelize
data class TestEntity(
        var id: Int = 0,
        var displayTime: Long = 0,
        var text: String? = Random().nextInt(10000).toString()
) : Parcelable, IDifference, IEqualsAdapter {

    override val uniqueId: String
        get() = id.toString()

    fun update() {
        displayTime = System.currentTimeMillis()
        text = "更新数据"
    }
}