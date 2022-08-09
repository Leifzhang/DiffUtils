package com.kronos.sample.glide

import android.app.Application
import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.DiskCache
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import okhttp3.OkHttpClient
import java.io.InputStream

/**
 *
 *  @Author LiABao
 *  @Since 2022/8/9
 *
 */
@GlideModule
open class KronosGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val requestOptions = RequestOptions()
        requestOptions.format(DecodeFormat.PREFER_ARGB_8888)
        builder.setDefaultRequestOptions(requestOptions)
        val calculator = MemorySizeCalculator.Builder(context)
            .setMaxSizeMultiplier(0.5f).setMemoryCacheScreens(1f)
            .build()
        builder.setMemoryCache(LruResourceCache(calculator.memoryCacheSize.toLong()))
            .setBitmapPool(LruBitmapPool(calculator.bitmapPoolSize.toLong()))
        val diskCacheSizeBytes = 1024 * 1024 * 100L
        val newFileName = DiskCache.Factory.DEFAULT_DISK_CACHE_DIR
        builder.setDiskCache(
            InternalCacheDiskCacheFactory(context, newFileName, diskCacheSizeBytes)
        )
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            Factory(OkHttpClient())
        )
    }
}