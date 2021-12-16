package com.kronos.sample.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kronos.diffutil.BaseDiffHelper
import com.kronos.diffutil.ParcelDiffHelper
import com.kronos.sample.R
import com.kronos.sample.databinding.RecyclerItemTestBinding
import com.kronos.sample.entity.TestEntity
import com.kronos.sample.getTime
import com.kronos.sample.widget.HeaderAdapterCallBack
import com.kronos.sample.widget.HeaderBaseAdapter

class TestAdapter(private val parcelDiffHelper: BaseDiffHelper<TestEntity>) :
    HeaderBaseAdapter<VieHolder>() {

    init {
        parcelDiffHelper.callBack = HeaderAdapterCallBack(this)
    }

    override fun listItemCount(): Int {
        return parcelDiffHelper.getItemSize()
    }

    override fun onCreateListViewHolder(parent: ViewGroup, viewType: Int): VieHolder {
        return VieHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_item_test,
                parent, false
            )
        )
    }

    override fun onBindListViewHolder(holder: VieHolder, position: Int) {
        val entity = parcelDiffHelper.getEntity<TestEntity>(position)
        holder.bindData(entity)
        holder.itemView.setOnClickListener {
            entity?.update()
            parcelDiffHelper.notifyItemChanged()
        }
    }
}

class VieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val viewbind :RecyclerItemTestBinding by viewBinding { RecyclerItemTestBinding.bind(itemView) }
    fun bindData(entity: TestEntity?) {
        entity?.apply {
            viewbind.titleTv.text = entity.id.toString() + " ${entity.text} " +
                    entity.displayTime.getTime("yyyy-MM-dd HH:mm")
        }
    }
}