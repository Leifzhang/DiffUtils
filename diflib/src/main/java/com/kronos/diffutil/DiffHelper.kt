package com.kronos.diffutil

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import java.util.*


class DiffHelper {
    private var itemsCursor: MutableList<*>? = null
    private var mData: MutableList<Any>? = null
    private var diffDetectMoves = false
    var callBack: ListUpdateCallback? = null

    fun setData(itemsCursor: MutableList<*>?) {
        this.itemsCursor = itemsCursor
        if (mData == null) {
            mData = mutableListOf()
            copyData()
        }
        diffUtils()
    }

    private fun copyData() {
        try {
            if (itemsCursor != null && itemsCursor!!.isNotEmpty()) {
                if (itemsCursor!![0] is Parcelable) {
                    mData = mutableListOf()
                } else {
                    mData = mutableListOf()
                    for (entity in itemsCursor!!) {
                        entity?.let { mData!!.add(it) }
                    }
                    return
                }
            } else {
                mData = mutableListOf()
                return
            }
            for (entity in itemsCursor!!) {
                val parcel = Parcel.obtain()
                (entity as Parcelable).writeToParcel(parcel, 0)
                parcel.setDataPosition(0)
                val constructor = entity.javaClass.getDeclaredConstructor(Parcel::class.java)
                constructor.isAccessible = true
                val dateEntity = constructor.newInstance(parcel)
                mData!!.add(dateEntity)
                parcel.recycle()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addData(pos: Int) {
        // try {
        if (itemsCursor?.get(pos) is Parcelable) {
            val parcelable = itemsCursor?.get(pos) as Parcelable
            val parcel = Parcel.obtain()
            parcelable.writeToParcel(parcel, 0)
            parcel.setDataPosition(0)
            val constructor = parcelable.javaClass.getDeclaredConstructor(Parcel::class.java)
            constructor.isAccessible = true
            val dateEntity = constructor.newInstance(parcel)
            mData?.let {
                synchronized(it) {
                    it.add(pos, dateEntity)
                }
            }
            parcel.recycle()
        } else {
            itemsCursor?.get(pos)?.let { mData?.add(pos, it) }
        }
        //    } catch (e: Exception) {
        //       e.printStackTrace()
        //  }
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
            val dateEntity = constructor.newInstance(parcel)
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
            itemsCursor = mutableListOf<Any>()
        }
        val diffResult = DiffUtil.calculateDiff(BaseDiffCallBack(mData, itemsCursor), diffDetectMoves)
        diffResult.dispatchUpdatesTo(object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {
                Log.i("DiffHelper", "onInserted position:$position  count:$count")
                for (i in 0 until count) {
                    addData(position + i)
                }
                callBack?.onInserted(position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                Log.i("DiffHelper", "onRemoved position:$position   count:$count")
                for (i in 0 until count) {
                    removeData(position)
                }
                callBack?.onRemoved(position, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                swap(fromPosition, toPosition)
                Log.i("DiffHelper", "onMoved fromPosition:$fromPosition  toPosition:$toPosition")
                callBack?.onMoved(fromPosition, toPosition)
            }

            override fun onChanged(position: Int, count: Int, payload: Any?) {
                for (i in 0 until count) {
                    val oldPosition = position + i
                    changeData(oldPosition, diffResult.convertOldPositionToNew(oldPosition))
                }
                Log.i("DiffHelper", "onChanged  position:$position count:$count")
                callBack?.onChanged(position, count, payload)
            }
        })
    }

    fun swap(oldPosition: Int, newPosition: Int) {
        if (oldPosition < newPosition) {
            for (i in oldPosition until newPosition) {
                Collections.swap(mData, i, i + 1)
            }
        }
        if (oldPosition > newPosition) {
            for (i in oldPosition downTo newPosition + 1) {
                Collections.swap(mData, i, i - 1)
            }
        }
    }

    fun getItemSize(): Int {
        return itemsCursor?.size ?: 0
    }

    fun <T : Parcelable> getEntity(pos: Int): T? {
        return if (itemsCursor?.size!! <= pos || pos < 0) null else itemsCursor?.get(pos) as T
    }


}

