package com.kronos.sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        simpleAdapterBtn.setOnClickListener {
            intent(SimpleAdapterActivity::class.java)
        }
        bravhAdapterBtn.setOnClickListener {
            intent(ConcatAdapterActivity::class.java)
        }
        stringAdapterBtn.setOnClickListener {
            intent(StringAdapterActivity::class.java)
        }
        //val concatAdapter
    }

    private fun intent(target: Class<out Any>) {
        val intent = Intent(this, target)
        startActivity(intent)
    }
}
