# DiffUtils

## 背景以及原理

谷歌在封装RecyclerView时候建议我们最好不要去调用`notifyDataSetChanged`，而是去调用其他的局部刷新的方法（增删改）。但是由于要计算游标等等所以感觉其实非常不好用，而且可能会导致一些奇奇怪怪的crash问题。

而DiffUtils的使用，首先要有两个List，一个代表旧数据，一个代表新数据，内部旧涉及到数据copy的问题，如果浅拷贝的话，其equals比较是没有任何意义的，所以项目使用了深拷贝的形式。

原理基于Parcel，用安卓原生的Parcelable进行数据模型拷贝。

当前项目简单的diffutil封装以及配合recyclerview adapter使用，可以实现数据动态增删移动等等操作同时配合，recyclerview adapter的局部数据调整刷新结合在一起。

## 使用

代码已经加入了线程池，以及主线程调度逻辑，所以可以直接子线程使用。不上传maven的原因是觉得可能还不够完善，可以直接考虑代码复制。

1. 对代码进行了重定义封装，当你需要使用深拷贝的时候，切记实现Parcelable接口，数据model最好实现IDifference接口，根据这个进行数据ID逻辑判断。如果要做内容比较的情况下实现IEqualsAdapter，同时使用插件生成hashcode，equals方法。
   

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

2. 初始化并传入数据，并设置数据刷新回掉，如果你有header或者别的话自己定义一个,提供了深拷贝和浅拷贝的两种实现。

```kotlin
     // 深拷贝
     val diffHelper: ParcelDiffHelper<TestEntity> = ParcelDiffHelper()
     diffHelper.callBack = SimpleAdapterCallBack(this)
     // lifecyclerowner 
     diffHelper.bindLifeCycle(this)
     diffHelper.setData(items)
```

```kotlin
    //浅拷贝版本
     val diffHelper: SimpleDiffHelper<String> = SimpleDiffHelper()
     diffHelper.callBack = SimpleAdapterCallBack(this)
     // lifecyclerowner 
     diffHelper.bindLifeCycle(this)
     diffHelper.setData(items)
```

3. 当items发生变化(任意变化增删改都行)，调用数据刷新。

```kotlin
   diffHelper.notifyItemChanged()
```

## 其他

代码可以结合任意的Adapter使用，包括BRVH等等。Demo中有简单的接入方式。