package com.kronos.diffutil

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.kronos.diffutil.utils.DiffThreadFactory
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

abstract class BaseDiffHelper<T> : AbstractDiffHelper<T>, LifecycleObserver {

    internal var itemsCursor: MutableList<T>? = null
    internal var snapshot: MutableList<T>? = null
    var diffDetectMoves = true
    var callBack: ListUpdateCallback? = null
    private val mMainThreadExecutor: Executor = MainThreadExecutor()
    private val mBackgroundThreadExecutor: ExecutorService =
        Executors.newFixedThreadPool(2, DiffThreadFactory())


    override fun setData(itemsCursor: MutableList<T>?, ignore: Boolean) {
        this.itemsCursor = itemsCursor
        itemsCursor?.apply {
            mBackgroundThreadExecutor.execute {
                if (snapshot == null) {
                    clone()
                }
                if (!ignore) {
                    mMainThreadExecutor.execute {
                        callBack?.onInserted(0, itemsCursor.size)
                    }
                }
            }
        }
    }

    override fun notifyItemChanged() {
        mBackgroundThreadExecutor.execute {
            val diffResult = diffUtils()
            mMainThreadExecutor.execute {
                diffResult.dispatchUpdatesTo(object : ListUpdateCallback {
                    override fun onInserted(position: Int, count: Int) {
                        callBack?.onInserted(position, count)
                    }

                    override fun onRemoved(position: Int, count: Int) {
                        callBack?.onRemoved(position, count)
                    }

                    override fun onMoved(fromPosition: Int, toPosition: Int) {
                        callBack?.onMoved(fromPosition, toPosition)
                    }

                    override fun onChanged(position: Int, count: Int, payload: Any?) {
                        callBack?.onChanged(position, count, payload)
                    }
                })
            }
        }
    }

    private fun diffUtils(): DiffUtil.DiffResult {
        val diffResult =
            DiffUtil.calculateDiff(BaseDiffCallBack(snapshot, itemsCursor), diffDetectMoves)
        clone()
        return diffResult
    }


    override fun getItemSize(): Int {
        return itemsCursor?.size ?: 0
    }

    override fun <T> getEntity(pos: Int): T? {
        return if (itemsCursor?.size ?: 0 <= pos || pos < 0) null else itemsCursor?.get(pos) as T
    }


    override fun bindLifeCycle(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mBackgroundThreadExecutor.shutdown()
    }
}

private class MainThreadExecutor internal constructor() : Executor {
    val mHandler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable) {
        mHandler.post(command)
    }

}