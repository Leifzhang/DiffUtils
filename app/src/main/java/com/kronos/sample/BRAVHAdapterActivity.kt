package com.kronos.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kronos.diffutil.ParcelDiffHelper
import com.kronos.sample.databinding.ActivityRecyclerviewBinding
import com.kronos.sample.entity.TestEntity
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import java.util.*

class BRAVHAdapterActivity : AppCompatActivity() {
    private val items by lazy {
        mutableListOf<TestEntity>()
    }
    private val parcelDiffHelper: ParcelDiffHelper<TestEntity> by lazy {
        ParcelDiffHelper()
    }
    private val viewBind by viewBinding {
        ActivityRecyclerviewBinding.inflate(it.layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBind.root)
        mockEntity()
        viewBind.recyclerView.layoutManager = LinearLayoutManager(this)
        /*   recyclerView.adapter = adapter
           adapter.addHeaderView(
               LayoutInflater.from(this@BRAVHAdapterActivity)
                   .inflate(R.layout.recycler_item_header, recyclerView, false)
           )*/
        //  adapter.setNewInstance(items)
        viewBind.recyclerView.itemAnimator = SlideInRightAnimator()
        viewBind.addTv.setOnClickListener {
            mockEntity()
            parcelDiffHelper.notifyItemChanged()
        }
        viewBind.removeTv.setOnClickListener {
            items.removeAt(0)
            mockEntity(2)
            parcelDiffHelper.notifyItemChanged()
        }
        viewBind.swapTv.setOnClickListener {
            swap(items, 1, 4)
            parcelDiffHelper.notifyItemChanged()
        }
        viewBind.refreshTv.setOnClickListener {
            items.clear()
            mockEntity()
            parcelDiffHelper.notifyItemChanged()
        }

    }

    private fun mockEntity() {
        var count = 0
        val itemSize = if (items.isEmpty()) 0 else items[items.size - 1].id + 1
        while (count < 20) {
            val entity = TestEntity(itemSize.plus(count))
            count++
            items.add(entity)
        }
    }

    private fun mockEntity(index: Int) {
        var count = 0
        val itemSize = if (items.isEmpty()) 0 else items.size + 1
        while (count < 20) {
            val entity = TestEntity(itemSize.plus(count))
            count++
            items.add(index, entity)
        }
    }

    private fun swap(list: MutableList<TestEntity>?, oldPosition: Int, newPosition: Int) {
        Collections.swap(list, oldPosition, newPosition)
    }
}
