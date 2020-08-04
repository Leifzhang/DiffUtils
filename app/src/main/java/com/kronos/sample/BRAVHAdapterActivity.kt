package com.kronos.sample

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kronos.diffutil.DiffHelper
import com.kronos.sample.adapter.BRAVHAdapter
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import kotlinx.android.synthetic.main.activity_recyclerview.*
import java.util.*

class BRAVHAdapterActivity : AppCompatActivity() {
    private val items by lazy {
        mutableListOf<TestEntity>()
    }

    private val diffHelper: DiffHelper<TestEntity> = DiffHelper()
    private val adapter by lazy {
        BRAVHAdapter(diffHelper)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        mockEntity()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.addHeaderView(LayoutInflater.from(this@BRAVHAdapterActivity).inflate(R.layout.recycler_item_header, recyclerView, false))
        adapter.setNewInstance(items)
        recyclerView.itemAnimator = SlideInRightAnimator()
        addTv.setOnClickListener {
            mockEntity()
            diffHelper.notifyItemChanged()
        }
        removeTv.setOnClickListener {
            items.removeAt(0)
            mockEntity(2)
            diffHelper.notifyItemChanged()
        }
        swapTv.setOnClickListener {
            swap(items, 1, 4)
            diffHelper.notifyItemChanged()
        }
        refreshTv.setOnClickListener {
            items.clear()
            mockEntity()
            diffHelper.notifyItemChanged()
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
