package com.kronos.sample

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kronos.diffutil.KotlinDataDiffHelper
import com.kronos.diffutil.by.dataDiff
import com.kronos.sample.adapter.TestAdapter
import com.kronos.sample.entity.TestEntity
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import kotlinx.android.synthetic.main.activity_recyclerview.*
import kotlinx.coroutines.*
import java.util.*

class SimpleAdapterActivity : AppCompatActivity() {

    private val items by lazy {
        mutableListOf<TestEntity>()
    }
    private val dataDiffHelper: KotlinDataDiffHelper<TestEntity> by dataDiff {
        it.copy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        GlobalScope.launch {
            delay(1000)
            mockEntity()
            dataDiffHelper.setData(items)
            dataDiffHelper.notifyItemChanged()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SlideInRightAnimationAdapter(TestAdapter(dataDiffHelper).apply {
            addHeaderView(LayoutInflater.from(this@SimpleAdapterActivity).inflate(R.layout.recycler_item_header,
                    recyclerView, false))
        })
        dataDiffHelper.bindLifeCycle(this)
        recyclerView.itemAnimator = SlideInRightAnimator()
        addTv.setOnClickListener {
            mockEntity()
            dataDiffHelper.notifyItemChanged()
        }
        removeTv.setOnClickListener {
            items.removeAt(0)
            mockEntity(2)
            dataDiffHelper.notifyItemChanged()
        }
        swapTv.setOnClickListener {
            swap(items, 1, 4)
            dataDiffHelper.notifyItemChanged()
        }
        refreshTv.setOnClickListener {
            items.clear()
            mockEntity()
            dataDiffHelper.notifyItemChanged()
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
