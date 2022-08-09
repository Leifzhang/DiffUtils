package com.kronos.sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kronos.sample.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val viewBind by viewBinding {
        ActivityMainBinding.inflate(it.layoutInflater)
    }

    private val message = "[bapi] branch target=feature/develop"
    private val regex = Regex("branch target=(\\D\\S*)").toPattern()

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
        val matcher = regex.matcher(message)
        if (matcher.find()) {
            val group = matcher.group(1)
            Log.i("regex", "group:$group")
        }
        //val concatAdapter
    }

    private fun intent(target: Class<out Any>) {
        val intent = Intent(this, target)
        startActivity(intent)
    }
}
