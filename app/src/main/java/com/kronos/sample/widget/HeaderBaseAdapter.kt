package com.kronos.sample.widget

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class HeaderBaseAdapter<VH : ViewHolder> : RecyclerView.Adapter<ViewHolder>() {

    private val headerViews = mutableListOf<View>()
    private var viewCursor: View? = null

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType > HEAD_VIEW_TYPE) {
            return SimpleViewHolder(requireNotNull(viewCursor))
        }
        return onCreateListViewHolder(parent, viewType)
    }

    final override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is SimpleViewHolder) {
            return
        }
        onBindListViewHolder(holder as VH, position - headerViews.size)
    }

    fun headerLayoutCount(): Int {
        return headerViews.size
    }

    fun addHeaderView(view: View?) {
        view?.apply {
            if (!headerViews.contains(this)) {
                headerViews.add(this)
                notifyItemInserted(headerViews.size - 1)
            }
        }
    }

    fun removeHeaderView(view: View?) {
        view?.apply {
            if (headerViews.contains(this)) {
                headerViews.remove(this)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return headerViews.size + listItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        if (position < headerViews.size) {
            viewCursor = headerViews[position]
            return viewCursor.hashCode() % HEAD_VIEW_TYPE + HEAD_VIEW_TYPE
        }
        return getListViewType(position - (headerViews.size))
    }

    abstract fun listItemCount(): Int

    abstract fun onCreateListViewHolder(parent: ViewGroup, viewType: Int): VH

    abstract fun onBindListViewHolder(holder: VH, position: Int)

    open fun getListViewType(position: Int): Int {
        return 1
    }

    companion object {
        const val HEAD_VIEW_TYPE = 10000
    }
}