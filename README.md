# DiffUtils

## 背景以及原理

谷歌在封装RecyclerView，最好不要去调用NotifyDataChange，而在调用其他的局部刷新的方法，如果游标出现问题，可能会导致一些奇奇怪怪的crash问题。

DiffUtils的使用，首先要有两个List，一个代表旧数据，一个代表新数据，内部旧涉及到数据copy的问题，如果浅拷贝的话，其equals比较是没有任何意义的，所以项目使用了深拷贝的形式。

原理基于Parcel，用安卓原生的Parcelable进行数据模型拷贝。

当前项目简单的diffutils封装以及配合recyclerview adapter使用，可以实现数据动态增删移动等等操作同时配合，recyclerview adapter的局部数据调整刷新结合在一起。

## 使用

1. 数据模型的定义，必须使用Parcelable,必须实现IDifference接口，主要来辨别数据主体是否发生变更。

    可以实现IEqualsAdapter,实现了当数据内容发生变更，也会通知Adapter刷新。

```kotlin
data class TestEntity(var id: Int = 0,
                      var displayTime: Long = 0,
                      var text: String? = Random().nextInt(10000).toString()) : Parcelable, IDifference, IEqualsAdapter {

    override val uniqueId: String
        get() = id.toString()

    fun update() {
        displayTime = System.currentTimeMillis()
        text = "更新数据"
    }


    constructor(source: Parcel) : this(
            source.readInt(),
            source.readLong(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeLong(displayTime)
        writeString(text)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TestEntity> = object : Parcelable.Creator<TestEntity> {
            override fun createFromParcel(source: Parcel): TestEntity = TestEntity(source)
            override fun newArray(size: Int): Array<TestEntity?> = arrayOfNulls(size)
        }
    }
}
```

2. 初始化并传入数据，并设置数据刷新回掉，如果你有header或者别的话自己定义一个。

```kotlin
     val diffHelper: DiffHelper<Any> = DiffHelper()
     diffHelper.callBack = SimpleAdapterCallBack(this)
     diffHelper.setData(items)
```

3. 当items发生变化(任意变化增删改都行)，调用数据刷新。

```kotlin
   diffHelper.notifyItemChanged()
```

## 其他

代码可以结合任意的Adapter使用，包括BRVH等等。