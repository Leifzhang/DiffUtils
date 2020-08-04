package com.kronos.sample.widget

import androidx.recyclerview.widget.ListUpdateCallback
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


class BRVHAdapterCallBack(private val adapter: BaseQuickAdapter<out Any, BaseViewHolder>) : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {
        adapter.notifyItemRangeInserted(adapter.headerLayoutCount + position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        adapter.notifyItemRangeRemoved(adapter.headerLayoutCount + position, count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        adapter.notifyItemMoved(adapter.headerLayoutCount + fromPosition, adapter.headerLayoutCount + toPosition)
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        adapter.notifyItemRangeChanged(adapter.headerLayoutCount + position, count, payload)
    }

}