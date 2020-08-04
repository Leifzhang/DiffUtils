package com.kronos.sample.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kronos.diffutil.DiffHelper
import com.kronos.sample.R
import com.kronos.sample.TestEntity
import com.kronos.sample.getTime
import com.kronos.sample.widget.HeaderAdapterCallBack
import com.kronos.sample.widget.HeaderBaseAdapter
import kotlinx.android.synthetic.main.recycler_item_test.view.*

class TestAdapter(private val diffHelper: DiffHelper<TestEntity>) : HeaderBaseAdapter<VieHolder>() {

    init {
        diffHelper.callBack = HeaderAdapterCallBack(this)
    }

    override fun listItemCount(): Int {
        return diffHelper.getItemSize()
    }

    override fun onCreateListViewHolder(parent: ViewGroup, viewType: Int): VieHolder {
        return VieHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_test,
                parent, false))
    }

    override fun onBindListViewHolder(holder: VieHolder, position: Int) {
        val entity = diffHelper.getEntity<TestEntity>(position)
        holder.bindData(entity)
        holder.itemView.setOnClickListener {
            entity?.update()
            diffHelper.notifyItemChanged()
        }
    }
}

class VieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindData(entity: TestEntity?) {
        entity?.apply {
            itemView.titleTv.text = entity.id.toString() + " ${entity.text} " +
                    entity.displayTime.getTime("yyyy-MM-dd HH:mm")
        }
    }
}