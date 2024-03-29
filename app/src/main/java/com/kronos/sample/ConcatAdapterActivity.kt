package com.kronos.sample

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kronos.diffutil.ParcelDiffHelper
import com.kronos.diffutil.SimpleDiffHelper
import com.kronos.sample.adapter.StringAdapter
import com.kronos.sample.adapter.TestAdapter
import com.kronos.sample.concat.adapter
import com.kronos.sample.concat.builderConcatAdapter
import com.kronos.sample.concat.builderSlideInRightAnimationAdapter
import com.kronos.sample.databinding.ActivityRecyclerviewBinding
import com.kronos.sample.entity.TestEntity
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
 *
 *  @Author LiABao
 *  @Since 2021/5/11
 *
 */
class ConcatAdapterActivity : AppCompatActivity() {
    private val items by lazy {
        mutableListOf<TestEntity>()
    }

    private val stringParcelDiffHelper: SimpleDiffHelper<String> = SimpleDiffHelper()

    private val parcelDiffHelper: ParcelDiffHelper<TestEntity> = ParcelDiffHelper()
    private val viewBind by viewBinding {
        ActivityRecyclerviewBinding.inflate(it.layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBind.root)
        GlobalScope.launch {
            delay(1000)
            mockEntity()
            parcelDiffHelper.setData(items)
            parcelDiffHelper.notifyItemChanged()
            delay(1000)
            stringParcelDiffHelper.setData(mutableListOf<String>().apply {
                for (index in 0 until 20) {
                    add(index.toString())
                }
            })
        }

        viewBind.recyclerView.layoutManager = LinearLayoutManager(this)
        val concatAdapter = builderConcatAdapter {
            adapter {
                builderSlideInRightAnimationAdapter {
                    TestAdapter(parcelDiffHelper).apply {
                        addHeaderView(
                            LayoutInflater.from(this@ConcatAdapterActivity).inflate(
                                R.layout.recycler_item_header,
                                viewBind.recyclerView, false
                            )
                        )
                    }
                }
            }
            adapter {
                builderSlideInRightAnimationAdapter {
                    StringAdapter(stringParcelDiffHelper).apply {
                        addHeaderView(
                            LayoutInflater.from(this@ConcatAdapterActivity).inflate(
                                R.layout.recycler_item_header,
                                viewBind.recyclerView, false
                            )
                        )
                    }
                }
            }
        }
        viewBind.recyclerView.adapter = concatAdapter
        parcelDiffHelper.bindLifeCycle(this)
        viewBind.recyclerView.itemAnimator = SlideInRightAnimator()
        viewBind.addTv.setOnClickListener {
            mockEntity()
            parcelDiffHelper.notifyItemChanged()
        }
        viewBind.removeTv.setOnClickListener {
            items.removeAt(0)
            mockEntity(2)
            parcelDiffHelper.notifyItemChanged()
        }
        viewBind.swapTv.setOnClickListener {
            swap(items, 1, 4)
            parcelDiffHelper.notifyItemChanged()
        }
        viewBind.refreshTv.setOnClickListener {
            items.clear()
            mockEntity()
            parcelDiffHelper.notifyItemChanged()
        }

    }

    private fun mockEntity() {
        var count = 0
        val itemSize = if (items.isEmpty()) 0 else items[items.size - 1].id + 1
        while (count < 20) {
            val entity = TestEntity(itemSize.plus(count))
            count++
            items.add(entity)
        }
    }

    private fun mockEntity(index: Int) {
        var count = 0
        val itemSize = if (items.isEmpty()) 0 else items.size + 1
        while (count < 20) {
            val entity = TestEntity(itemSize.plus(count))
            count++
            items.add(index, entity)
        }
    }

    private fun swap(list: MutableList<TestEntity>?, oldPosition: Int, newPosition: Int) {
        Collections.swap(list, oldPosition, newPosition)
    }
}