package com.kronos.diffutil

import android.os.Handler
import android.os.Looper
import android.os.Parcel
import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class DiffHelper<T> {

    private var itemsCursor: MutableList<T>? = null
    private var mData: CopyOnWriteArrayList<T>? = null
    var diffDetectMoves = true
    var callBack: ListUpdateCallback? = null


    private val mMainThreadExecutor: Executor = MainThreadExecutor()

    private val mBackgroundThreadExecutor: Executor = Executors.newFixedThreadPool(2)

    private class MainThreadExecutor internal constructor() : Executor {
        val mHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mHandler.post(command)
        }
    }

    fun setData(itemsCursor: MutableList<T>?, ignore: Boolean = false) {
        this.itemsCursor = itemsCursor
        itemsCursor?.apply {
            mBackgroundThreadExecutor.execute {
                if (mData == null) {
                    copyData()
                }
                if (!ignore) {
                    mMainThreadExecutor.execute {
                        callBack?.onInserted(0, itemsCursor.size)
                    }
                }
            }
        }
    }

    private fun copyData() {
        try {
            itemsCursor?.apply {
                if (isNotEmpty()) {
                    if (this[0] is Parcelable) {
                        mData = CopyOnWriteArrayList()
                    } else {
                        mData = CopyOnWriteArrayList()
                        for (entity in this) {
                            mData?.add(entity)
                        }
                        return
                    }
                } else {
                    mData = CopyOnWriteArrayList()
                    return

                }
                for (entity in this) {
                    val parcel = Parcel.obtain()
                    (entity as Parcelable).writeToParcel(parcel, 0)
                    parcel.setDataPosition(0)
                    val constructor = entity.javaClass.getDeclaredConstructor(Parcel::class.java)
                    constructor.isAccessible = true
                    val dateEntity = constructor.newInstance(parcel) as T
                    mData?.add(dateEntity)
                    parcel.recycle()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun notifyItemChanged() {
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
                DiffUtil.calculateDiff(BaseDiffCallBack(mData, itemsCursor), diffDetectMoves)
        copyData()
        return diffResult

    }

    fun getItemSize(): Int {
        return itemsCursor?.size ?: 0
    }

    fun <T> getEntity(pos: Int): T? {
        return if (itemsCursor?.size ?: 0 <= pos || pos < 0) null else itemsCursor?.get(pos) as T
    }

}

