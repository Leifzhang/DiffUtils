package com.kronos.sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.kronos.diffutil.DiffHelper
import com.kronos.sample.adapter.BRAVHAdapter
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_recyclerview.*
import java.util.*
import kotlin.reflect.KClass


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        simpleAdapterBtn.setOnClickListener {
            intent(SimpleAdapterActivity::class.java)
        }
        bravhAdapterBtn.setOnClickListener {
            intent(BRAVHAdapterActivity::class.java)
        }
    }

    private fun intent(target: Class<out Any>) {
        val intent = Intent(this, target)
        startActivity(intent)
    }
}
