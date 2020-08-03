package com.kronos.diffutil

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList


class DiffHelper<T> {
    private var itemsCursor: MutableList<T>? = null
    private var mData: CopyOnWriteArrayList<T>? = null
    var diffDetectMoves = false
    var callBack: ListUpdateCallback? = null

    fun setData(itemsCursor: MutableList<T>?) {
        this.itemsCursor = itemsCursor
        if (mData == null) {
            mData = CopyOnWriteArrayList()
            copyData()
        }
        diffUtils()
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

    private fun addData(oldPosition: Int) {
        if (itemsCursor?.get(oldPosition) is Parcelable) {
            val parcelable = itemsCursor?.get(oldPosition) as Parcelable
            val parcel = Parcel.obtain()
            parcelable.writeToParcel(parcel, 0)
            parcel.setDataPosition(0)
            val constructor = parcelable.javaClass.getDeclaredConstructor(Parcel::class.java)
            constructor.isAccessible = true
            val dateEntity = constructor.newInstance(parcel) as T
            mData?.let {
                it.add(oldPosition, dateEntity)
            }
            parcel.recycle()
        } else {
            itemsCursor?.get(oldPosition)?.let { mData?.add(oldPosition, it) }
        }
    }

    private fun removeData(pos: Int) {
        mData?.let {
            synchronized(it) {
                it.removeAt(pos)
            }
        }
    }

    private fun changeData(oldPosition: Int, newPosition: Int) {
        if (itemsCursor?.get(newPosition) is Parcelable) {
            val parcelable = itemsCursor?.get(newPosition) as Parcelable
            val parcel = Parcel.obtain()
            parcelable.writeToParcel(parcel, 0)
            parcel.setDataPosition(0)
            val constructor = parcelable.javaClass.getDeclaredConstructor(Parcel::class.java)
            constructor.isAccessible = true
            val dateEntity = constructor.newInstance(parcel) as T
            mData?.let {
                synchronized(it) {
                    it.removeAt(oldPosition)
                    it.add(oldPosition, dateEntity)
                }
            }
            parcel.recycle()
        } else {
            mData?.let {
                synchronized(it) {
                    it.removeAt(oldPosition)
                    itemsCursor?.get(oldPosition)?.let { it1 -> it.add(newPosition, it1) }
                }
            }
        }
    }


    fun notifyItemChanged() {
        diffUtils()
    }

    @Synchronized
    private fun diffUtils() {
        if (itemsCursor == null) {
            itemsCursor = mutableListOf()
        }
        val diffResult = DiffUtil.calculateDiff(BaseDiffCallBack(mData, itemsCursor), diffDetectMoves)
        diffResult.dispatchUpdatesTo(object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {
                // Log.i("DiffHelper", "onInserted position:$position  count:$count")
                for (i in 0 until count) {
                    val oldPosition = position + i
                    val newPosition = diffResult.convertNewPositionToOld(oldPosition)
                    //   Log.i("DiffHelper", "onInserted newPosition:$newPosition  oldPosition:$oldPosition")
                    addData(position + i)
                }
                callBack?.onInserted(position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                //  Log.i("DiffHelper", "onRemoved position:$position   count:$count")
                for (i in 0 until count) {
                    removeData(position)
                }
                callBack?.onRemoved(position, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                swap(fromPosition, toPosition)
                //  Log.i("DiffHelper", "onMoved fromPosition:$fromPosition  toPosition:$toPosition")
                callBack?.onMoved(fromPosition, toPosition)
            }

            override fun onChanged(position: Int, count: Int, payload: Any?) {
                for (i in 0 until count) {
                    val oldPosition = position + i
                    changeData(oldPosition, diffResult.convertOldPositionToNew(oldPosition))
                }
                //  Log.i("DiffHelper", "onChanged  position:$position count:$count")
                callBack?.onChanged(position, count, payload)
            }
        })
    }

    fun swap(oldPosition: Int, newPosition: Int) {
        Collections.swap(mData, oldPosition, newPosition)

    }

    fun getItemSize(): Int {
        return itemsCursor?.size ?: 0
    }

    fun <T> getEntity(pos: Int): T? {
        return if (itemsCursor?.size ?: 0 <= pos || pos < 0) null else itemsCursor?.get(pos) as T
    }

}

