package com.kronos.sample.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kronos.diffutil.DiffHelper
import com.kronos.sample.R
import com.kronos.sample.TestEntity
import com.kronos.sample.getTime
import com.kronos.sample.widget.BRVHAdapterCallBack
import kotlinx.android.synthetic.main.recycler_item_test.view.*

class BRAVHAdapter(private val diffHelper: DiffHelper<TestEntity>) : BaseQuickAdapter<TestEntity, BaseViewHolder>(R.layout.recycler_item_test) {

    init {
        diffHelper.callBack = BRVHAdapterCallBack(this)
    }

    override fun setNewInstance(list: MutableList<TestEntity>?) {
        super.setNewInstance(list)
        // super 内部已经调用过notifyDataChange 所以会有些问题
        diffHelper.setData(list, true)
    }

    override fun convert(holder: BaseViewHolder, item: TestEntity) {
        holder.itemView.titleTv.text = item.id.toString() + " ${item.text} " +
                item.displayTime.getTime("yyyy-MM-dd HH:mm")
    }

    override fun getDefItemCount(): Int {
        return diffHelper.getItemSize()
    }
}