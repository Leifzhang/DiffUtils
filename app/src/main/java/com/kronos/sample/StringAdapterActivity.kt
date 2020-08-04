package com.kronos.sample

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kronos.diffutil.SimpleDiffHelper
import com.kronos.sample.adapter.StringAdapter
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

    private val parcelDiffHelper: SimpleDiffHelper<String> = SimpleDiffHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        GlobalScope.launch {
            delay(1000)
            mockEntity()
            parcelDiffHelper.setData(items)
            parcelDiffHelper.notifyItemChanged()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SlideInRightAnimationAdapter(StringAdapter(parcelDiffHelper).apply {
            addHeaderView(LayoutInflater.from(this@StringAdapterActivity).inflate(R.layout.recycler_item_header,
                    recyclerView, false))
        })
        recyclerView.itemAnimator = SlideInRightAnimator()

        addTv.setOnClickListener {
            mockEntity()
            parcelDiffHelper.notifyItemChanged()
        }
        removeTv.setOnClickListener {
            items.removeAt(0)
            mockEntity(2)
            parcelDiffHelper.notifyItemChanged()
        }
        swapTv.setOnClickListener {
            swap(items, 1, 4)
            parcelDiffHelper.notifyItemChanged()
        }
        refreshTv.setOnClickListener {
            items.clear()
            mockEntity()
            parcelDiffHelper.notifyItemChanged()
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

    private fun swap(list: MutableList<String>?, oldPosition: Int, newPosition: Int) {
        Collections.swap(list, oldPosition, newPosition)
    }

}
