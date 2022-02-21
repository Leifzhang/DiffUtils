package com.kronos.sample.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kronos.diffutil.SimpleDiffHelper
import com.kronos.sample.R
import com.kronos.sample.widget.HeaderAdapterCallBack
import com.kronos.sample.widget.HeaderBaseAdapter

class StringAdapter(private val parcelDiffHelper: SimpleDiffHelper<String>) :
    HeaderBaseAdapter<StringViewHolder>() {

    init {
        parcelDiffHelper.callBack = HeaderAdapterCallBack(this)
    }

    override fun listItemCount(): Int {
        return parcelDiffHelper.getItemSize()
    }

    override fun onCreateListViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
        return StringViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_item_test,
                parent, false
            )
        )
    }

    override fun onBindListViewHolder(holder: StringViewHolder, position: Int) {
        val entity = parcelDiffHelper.getEntity<String>(position)
        holder.bindData(entity)
    }
}

class StringViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindData(entity: String?) {
        entity?.apply {
            itemView.findViewById<TextView>(R.id.titleTv).text = entity
        }
    }
}