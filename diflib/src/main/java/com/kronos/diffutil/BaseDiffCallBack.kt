package com.kronos.diffutil

import android.text.TextUtils
import androidx.recyclerview.widget.DiffUtil

class BaseDiffCallBack(oldData: List<Any>?, newData: List<Any>?) : DiffUtil.Callback() {
    private val oldData: List<Any>? = oldData
    private val newData: List<Any>? = newData

    override fun getOldListSize(): Int {
        return oldData?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newData?.size ?: 0
    }

    override fun areItemsTheSame(p0: Int, p1: Int): Boolean {
        val object1 = oldData?.get(p0)
        val object2 = newData?.get(p1)
        if (object1 == null || object2 == null)
            return false
        return if (object1 is IDifference && object2 is IDifference) {
            TextUtils.equals(object1.uniqueId, object2.uniqueId)
        } else {
            object1 == object2
        }
    }


    override fun areContentsTheSame(p0: Int, p1: Int): Boolean {
        val object1 = oldData?.get(p0)
        val object2 = newData?.get(p1)
        return if (object1 is IEqualsAdapter && object2 is IEqualsAdapter) {
            object1 == object2
        } else {
            true
        }
    }

}