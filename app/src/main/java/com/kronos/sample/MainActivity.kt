package com.kronos.sample

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kronos.diffutil.DiffHelper
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var items: MutableList<TestEntity>? = null
    private val diffHelper: DiffHelper = DiffHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mockEntity()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TestAdapter(diffHelper)
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
        val itemSize = items?.size
        while (count < 20) {
            val entity = TestEntity()
            entity.id = itemSize?.plus(count) ?: 0
            count++
            items?.add(entity)
        }
    }

    private fun swap(list: MutableList<TestEntity>?, oldPosition: Int, newPosition: Int) {
        if (null == list) {
            throw IllegalStateException("The list can not be empty...")
        }
        val tempElement = list[oldPosition]

        if (oldPosition < newPosition) {
            for (i in oldPosition until newPosition) {
                list[i] = list[i + 1]
            }
            list[newPosition] = tempElement
        }
        if (oldPosition > newPosition) {
            for (i in oldPosition downTo newPosition + 1) {
                list[i] = list[i - 1]
            }
            list[newPosition] = tempElement
        }
    }

}
