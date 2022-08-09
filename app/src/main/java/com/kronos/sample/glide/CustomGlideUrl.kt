package com.kronos.sample.glide

import android.net.Uri
import android.text.TextUtils
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.Headers
import com.bumptech.glide.load.model.LazyHeaders

/**
 *
 *  @Author LiABao
 *  @Since 2022/8/4
 *
 */
class CustomGlideUrl(glideUrl: GlideUrl) :
    GlideUrl(glideUrl.toStringUrl(), getHeadersUtils(glideUrl.headers)) {
    private val cacheKeyString: String by lazy {
        val url = Uri.parse(toStringUrl())
        val cacheKeyBuilder = Uri.Builder()
        cacheKeyBuilder.scheme(url.scheme).authority(url.authority).path(url.path)
        for (key in url.queryParameterNames) {
            if (TextUtils.equals("Expires", key) || TextUtils.equals("Signature", key)
                || TextUtils.equals("OSSAccessKeyId", key)
            ) {
                continue
            }
            cacheKeyBuilder.appendQueryParameter(key, url.getQueryParameter(key))
        }
        return@lazy cacheKeyBuilder.build().toString()
    }

    override fun getCacheKey(): String {
        return cacheKeyString
    }

    companion object {

        internal fun getHeadersUtils(head: Map<String, String>): Headers {
            val builder = LazyHeaders.Builder()
            for ((key, value) in head) {
                builder.addHeader(key, value)
            }
            return builder.build()
        }
    }

}