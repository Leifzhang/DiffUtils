package com.kronos.sample

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kronos.diffutil.DiffHelper
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private var items: MutableList<TestEntity>? = null
    private val diffHelper: DiffHelper = DiffHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mockEntity()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SlideInRightAnimationAdapter(TestAdapter(diffHelper))
        recyclerView.itemAnimator = SlideInRightAnimator()
        diffHelper.setData(items)
        addTv.setOnClickListener {
            mockEntity()
            diffHelper.notifyItemChanged()
        }
        removeTv.setOnClickListener {
            items?.removeAt(0)
            diffHelper.notifyItemChanged()
        }
        swapTv.setOnClickListener {
            swap(items, 1, 4)
            diffHelper.notifyItemChanged()
        }
        refreshTv.setOnClickListener {
            items = mutableListOf()
            mockEntity()
            diffHelper.setData(items)
        }

    }

    private fun mockEntity() {
        if (items == null) {
            items = mutableListOf()
        }
        var count = 0
        val itemSize = if (items?.isEmpty()!!) 0 else items!![items!!.size - 1].id + 1
        while (count < 20) {
            val entity = TestEntity()
            entity.id = itemSize.plus(count)
            count++
            items?.add(entity)
        }
    }

    private fun swap(list: MutableList<TestEntity>?, oldPosition: Int, newPosition: Int) {
        Collections.swap(list, oldPosition, newPosition)
    }

}
