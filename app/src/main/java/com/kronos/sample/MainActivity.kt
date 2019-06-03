package com.kronos.sample

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private var items: ArrayList<TestEntity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mockEntity()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TestAdapter(items)
    }

    private fun mockEntity() {
        if (items == null) {
            items = ArrayList()
        }
        var count = 0
        while (count < 20) {
            val entity = TestEntity()
            entity.id = items?.size?.plus(count) ?: 0
            count++
            items?.add(entity)
        }
    }
}
