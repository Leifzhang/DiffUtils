package com.kronos.sample.adapter

import com.kronos.diffutil.ParcelDiffHelper
import com.kronos.sample.TestEntity

class BRAVHAdapter(private val parcelDiffHelper: ParcelDiffHelper<TestEntity>)
/*: BaseQuickAdapter<TestEntity, BaseViewHolder>(R.layout.recycler_item_test) {

    init {
        parcelDiffHelper.callBack = BRVHAdapterCallBack(this)
    }

    override fun setNewInstance(list: MutableList<TestEntity>?) {
        super.setNewInstance(list)
        // super 内部已经调用过notifyDataChange 所以会有些问题
        parcelDiffHelper.setData(list, true)
    }

    override fun convert(holder: BaseViewHolder, item: TestEntity) {
        holder.itemView.titleTv.text = item.id.toString() + " ${item.text} " +
                item.displayTime.getTime("yyyy-MM-dd HH:mm")
    }

    override fun getDefItemCount(): Int {
        return parcelDiffHelper.getItemSize()
    }
}*/