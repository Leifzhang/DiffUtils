package com.kronos.sample.widget

import androidx.recyclerview.widget.ListUpdateCallback


class HeaderAdapterCallBack(private val adapter: HeaderBaseAdapter<*>) : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {
        adapter.notifyItemRangeInserted(adapter.headerLayoutCount() + position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        adapter.notifyItemRangeRemoved(adapter.headerLayoutCount() + position, count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        adapter.notifyItemMoved(adapter.headerLayoutCount() + fromPosition, adapter.headerLayoutCount() + toPosition)
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        adapter.notifyItemRangeChanged(adapter.headerLayoutCount() + position, count, payload)
    }

}