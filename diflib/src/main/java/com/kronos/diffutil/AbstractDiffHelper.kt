package com.kronos.diffutil

interface AbstractDiffHelper<T> {

    fun clone()

    fun setData(itemsCursor: MutableList<T>?, ignore: Boolean = false)

    fun notifyItemChanged()

    fun getItemSize(): Int

    fun <T> getEntity(pos: Int): T?
}