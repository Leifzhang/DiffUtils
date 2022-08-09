package com.kronos.sample.glide

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import okhttp3.Call
import okhttp3.OkHttpClient
import java.io.InputStream

/**
 *
 *  @Author LiABao
 *  @Since 2022/8/9
 *
 */
class KronosUrlLoader(private val client: Call.Factory) : ModelLoader<GlideUrl, InputStream> {

    override fun handles(url: GlideUrl): Boolean {
        return true
    }

    override fun buildLoadData(
        model: GlideUrl,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<InputStream> {
        val newModel: GlideUrl
        if (model is CustomGlideUrl) {
            newModel = model
        } else {
            newModel = CustomGlideUrl(model)
        }
        return ModelLoader.LoadData(newModel, OkHttpStreamFetcher(client, newModel))
    }


}

class Factory constructor(private val client: Call.Factory = internalClient) :
    ModelLoaderFactory<GlideUrl, InputStream> {

    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<GlideUrl, InputStream> {
        return  KronosUrlLoader(client)
    }

    override fun teardown() {}

    companion object {

        val internalClient by lazy {
            OkHttpClient()
        }

    }
}