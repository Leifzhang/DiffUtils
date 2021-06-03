package com.kronos.diffutil.by

import androidx.annotation.MainThread
import com.kronos.diffutil.KotlinDataDiffHelper

/**
 * @Author LiABao
 * @Since 2021/6/3
 */


@MainThread
inline fun <D> dataDiff(
    noinline copy: (D) -> D
): Lazy<KotlinDataDiffHelper<D>> = createDataDiff(copy)


@MainThread
fun <D> createDataDiff(
    copy: (D) -> D
): Lazy<KotlinDataDiffHelper<D>> {
    return DataDiffLazy(copy)
}

class DataDiffLazy<D>(private val copy: (D) -> D) : Lazy<KotlinDataDiffHelper<D>> {

    private var cached: KotlinDataDiffHelper<D>? = null

    override val value: KotlinDataDiffHelper<D>
        get() {
            if (cached == null) {
                cached = KotlinDataDiffHelper(copy)
            } else {
                cached
            }
            return requireNotNull(cached)
        }

    override fun isInitialized(): Boolean = cached != null

}