package com.kronos.sample

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kronos.diffutil.DiffHelper
import kotlinx.android.synthetic.main.activity_main.*

import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private var items: ArrayList<TestEntity>? = null
    private val diffHelper: DiffHelper = DiffHelper()
    var count = 0
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
    }

    private fun mockEntity() {
        if (items == null) {
            items = ArrayList()
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
}
