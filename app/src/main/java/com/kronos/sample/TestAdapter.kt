package com.kronos.sample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kronos.diffutil.DiffHelper
import com.kronos.diffutil.SimpleAdapterCallBack
import kotlinx.android.synthetic.main.recycler_item_test.view.*

class TestAdapter(private val diffHelper: DiffHelper) : RecyclerView.Adapter<TestAdapter.VieHolder>() {

    init {
        diffHelper.callBack = SimpleAdapterCallBack(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VieHolder {
        return VieHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_test,
                parent, false))
    }

    override fun onBindViewHolder(holder: VieHolder, position: Int) {
        val entity = diffHelper.getEntity<TestEntity>(position)
        entity?.let { holder.bindData(it) }
        holder.itemView.setOnClickListener {
            entity?.update()
            diffHelper.notifyItemChanged()
        }
    }

    override fun getItemCount(): Int {
        return diffHelper.getItemSize()
    }

    class VieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(entity: TestEntity) {
            itemView.titleTv.text = entity.id.toString() + " ${entity.text} " + DateHelper.getTime(entity.displayTime, "yyyy-MM-dd HH:mm")
        }
    }
}
