package com.kronos.diffutil

import android.os.Parcel
import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback

class DiffHelper {
    private var itemsCursor: MutableList<*>? = null
    private var mData: MutableList<Any>? = null
    private var diffDetectMoves = false
    var callBack: ListUpdateCallback? = null

    fun setData(itemsCursor: MutableList<*>?) {
        this.itemsCursor = itemsCursor
        if (mData == null) {
            mData = mutableListOf()
        }
        // notifyItemChanged()
        //  copyData()
        notifyItemChanged()
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
        try {
            if (itemsCursor?.get(pos) is Parcelable) {
                val parcelable = itemsCursor?.get(pos) as Parcelable
                val parcel = Parcel.obtain()
                parcelable.writeToParcel(parcel, 0)
                parcel.setDataPosition(0)
                val constructor = parcelable.javaClass.getDeclaredConstructor(Parcel::class.java)
                constructor.isAccessible = true
                val dateEntity = constructor.newInstance(parcel)
                mData?.add(pos, dateEntity)
                parcel.recycle()
            } else {
                itemsCursor?.get(pos)?.let { mData?.add(pos, it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun removeData(pos: Int) {
        mData?.removeAt(pos)
    }

    private fun changeData(pos: Int) {
        try {
            if (itemsCursor?.get(pos) is Parcelable) {
                val parcelable = itemsCursor?.get(pos) as Parcelable
                val parcel = Parcel.obtain()
                parcelable.writeToParcel(parcel, 0)
                parcel.setDataPosition(0)
                val constructor = parcelable.javaClass.getDeclaredConstructor(Parcel::class.java)
                constructor.isAccessible = true
                val dateEntity = constructor.newInstance(parcel)
                mData?.removeAt(pos)
                mData?.add(pos, dateEntity)
                parcel.recycle()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun notifyItemChanged() {
        diffUtils()
    }

    private fun diffUtils() {
        if (itemsCursor == null) {
            itemsCursor = mutableListOf<Any>()
        }
        val diffResult = DiffUtil.calculateDiff(BaseDiffCallBack(mData, itemsCursor), diffDetectMoves)
        diffResult.dispatchUpdatesTo(object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {
                for (i in 0 until count) {
                    addData(position + i)
                }
                callBack?.onInserted(position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                for (i in 0 until count) {
                    removeData(position)
                }
                callBack?.onRemoved(position, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                //   copyData()
                callBack?.onMoved(fromPosition, toPosition)
                //   mAdapter.notifyItemMoved(fromPosition + mAdapter.getHeadViewSize(), toPosition + mAdapter.getHeadViewSize())
            }

            override fun onChanged(position: Int, count: Int, payload: Any?) {
                for (i in 0 until count) {
                    changeData(position + i)
                }
                callBack?.onChanged(position, count, payload)
            }
        })
    }

    fun getItemSize(): Int {
        return itemsCursor?.size ?: 0
    }

    fun <T : Parcelable> getEntity(pos: Int): T? {
        return if (itemsCursor?.size!! <= pos || pos < 0) null else itemsCursor?.get(pos) as T
    }


}

