package com.kronos.diffutil

import androidx.lifecycle.LifecycleOwner

interface AbstractDiffHelper<T> {

    fun clone()

    fun setData(itemsCursor: MutableList<T>?, ignore: Boolean = false)

    fun notifyItemChanged()

    fun getItemSize(): Int

    fun <T> getEntity(pos: Int): T?

    fun bindLifeCycle(lifecycleOwner: LifecycleOwner)
}