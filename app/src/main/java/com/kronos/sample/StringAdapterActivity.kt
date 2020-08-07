package com.kronos.sample

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.ConcatAdapter.Config.StableIdMode
import androidx.recyclerview.widget.LinearLayoutManager
import com.kronos.diffutil.ParcelDiffHelper
import com.kronos.diffutil.SimpleDiffHelper
import com.kronos.sample.adapter.StringAdapter
import com.kronos.sample.adapter.TestAdapter
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import kotlinx.android.synthetic.main.activity_recyclerview.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class StringAdapterActivity : AppCompatActivity() {

    private val items by lazy {
        mutableListOf<String>()
    }
    private val newItems by lazy {
        mutableListOf<TestEntity>()
    }


    private val simpleDiffHelper: SimpleDiffHelper<String> = SimpleDiffHelper()
    private val parcelDiffHelper: ParcelDiffHelper<TestEntity> = ParcelDiffHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        GlobalScope.launch {
            mockEntity()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        val stringAdapter = SlideInRightAnimationAdapter(StringAdapter(simpleDiffHelper).apply {
            addHeaderView(LayoutInflater.from(this@StringAdapterActivity).inflate(R.layout.recycler_item_header,
                    recyclerView, false))
        })
        val testAdapter = SlideInRightAnimationAdapter(TestAdapter(parcelDiffHelper).apply {
            addHeaderView(LayoutInflater.from(this@StringAdapterActivity).inflate(R.layout.recycler_item_header,
                    recyclerView, false))
        })
        val concatAdapter = ConcatAdapter(stringAdapter, testAdapter)
        recyclerView.adapter = concatAdapter
        recyclerView.itemAnimator = SlideInRightAnimator()
        simpleDiffHelper.setData(items)
        parcelDiffHelper.setData(newItems)
        simpleDiffHelper.bindLifeCycle(this)
        parcelDiffHelper.bindLifeCycle(this)
        mockTestEntity()
        addTv.setOnClickListener {
            mockEntity()
            simpleDiffHelper.notifyItemChanged()
        }
        removeTv.setOnClickListener {
            items.removeAt(0)
            mockEntity(2)
            simpleDiffHelper.notifyItemChanged()
        }
        swapTv.setOnClickListener {
            //   swap(items, 1, 4)
            mockTestEntity()
            testAdapter.notifyDataSetChanged()
            //    parcelDiffHelper.notifyItemChanged()
        }
        refreshTv.setOnClickListener {
            items.clear()
            mockEntity()
            simpleDiffHelper.notifyItemChanged()
        }

    }

    private fun mockTestEntity() {
        var count = 0
        val itemSize = if (newItems.isEmpty()) 0 else newItems[items.size - 1].id + 1
        while (count < 20) {
            val entity = TestEntity(itemSize.plus(count))
            count++
            newItems.add(entity)
        }
    }

    private fun mockEntity() {
        var count = 0
        val itemSize = if (items.isEmpty()) 0 else items.size + 1
        while (count < 20) {
            val entity = itemSize.plus(count).toString()
            count++
            items.add(entity)
        }
    }

    private fun mockEntity(index: Int) {
        var count = 0
        val itemSize = if (items.isEmpty()) 0 else items.size + 1
        while (count < 20) {
            val entity = itemSize.plus(count).toString()
            count++
            items.add(index, entity)
        }
    }

    private fun swap(list: MutableList<out Any>?, oldPosition: Int, newPosition: Int) {
        Collections.swap(list, oldPosition, newPosition)
    }

}
