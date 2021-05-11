package com.kronos.sample.concat

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter

/**
 *
 *  @Author LiABao
 *  @Since 2021/5/11
 *
 */

inline fun builderConcatAdapter(invoke: MutableList<RecyclerView.Adapter<out RecyclerView.ViewHolder>>.() -> Unit):
        ConcatAdapter {
    val list = mutableListOf<RecyclerView.Adapter<out RecyclerView.ViewHolder>>()
    invoke.invoke(list)
    return ConcatAdapter(list)
}

inline fun MutableList<RecyclerView.Adapter<out RecyclerView.ViewHolder>>.adapter(invoke: () -> RecyclerView.Adapter<out RecyclerView.ViewHolder>) {
    add(invoke.invoke())
}

inline fun builderSlideInRightAnimationAdapter(invoke: () -> RecyclerView.Adapter<out RecyclerView.ViewHolder>): SlideInLeftAnimationAdapter {
    return SlideInLeftAnimationAdapter(invoke.invoke())
}

@DslMarker
annotation class ConcatAdapterDsl

