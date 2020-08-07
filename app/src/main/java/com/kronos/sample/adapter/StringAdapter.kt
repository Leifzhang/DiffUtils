package com.kronos.sample.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kronos.diffutil.SimpleDiffHelper
import com.kronos.sample.R
import com.kronos.sample.widget.HeaderAdapterCallBack
import com.kronos.sample.widget.HeaderBaseAdapter
import kotlinx.android.synthetic.main.recycler_item_test.view.*

class StringAdapter(private val diffHelper: SimpleDiffHelper<String>) : HeaderBaseAdapter<StringViewHolder>() {

    init {
        diffHelper.callBack = HeaderAdapterCallBack(this)
    }

    override fun listItemCount(): Int {
        return diffHelper.getItemSize()
    }

    override fun onCreateListViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
        return StringViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_test,
                parent, false))
    }

    override fun onBindListViewHolder(holder: StringViewHolder, position: Int) {
        val entity = diffHelper.getEntity<String>(position)
        holder.bindData(entity)
    }

}

class StringViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindData(entity: String?) {
        entity?.apply {
            itemView.titleTv.text = entity
        }
    }
}