package com.kronos.sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kronos.sample.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val viewBind by viewBinding {
        ActivityMainBinding.inflate(it.layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBind.root)
        viewBind.simpleAdapterBtn.setOnClickListener {
            intent(SimpleAdapterActivity::class.java)
        }
        viewBind.bravhAdapterBtn.setOnClickListener {
            intent(ConcatAdapterActivity::class.java)
        }
        viewBind.stringAdapterBtn.setOnClickListener {
            intent(StringAdapterActivity::class.java)
        }
        //val concatAdapter
    }

    private fun intent(target: Class<out Any>) {
        val intent = Intent(this, target)
        startActivity(intent)
    }
}
