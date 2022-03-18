package org.cnodejs.android.md.util

import android.content.Context
import android.os.Build
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.GuardedBy
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.request.Disposable
import coil.request.ImageRequest
import coil.util.CoilUtils
import org.cnodejs.android.md.R
import org.cnodejs.android.md.model.entity.UrlString

private class AppCoil private constructor(context: Context) {
    companion object {
        private val lock = Any()

        @GuardedBy("lock")
        @Volatile
        private var instance: AppCoil? = null

        fun getInstance(context: Context) = instance ?: synchronized(lock) {
            instance ?: AppCoil(context.applicationContext).also { instance = it }
        }
    }

    val imageLoader = ImageLoader.Builder(context)
        .componentRegistry {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                add(ImageDecoderDecoder(context))
            } else {
                add(GifDecoder())
            }
            add(SvgDecoder(context))
        }
        .build()
}

private fun ImageView.loadAny(
    data: Any?,
    builder: ImageRequest.Builder.() -> Unit = {},
): Disposable {
    val request = ImageRequest.Builder(context)
        .data(data)
        .target(this)
        .apply(builder)
        .build()
    return AppCoil.getInstance(context).imageLoader.enqueue(request)
}

fun ImageView.loadAvatar(@DrawableRes drawableResId: Int): Disposable {
    return loadAny(drawableResId) {
        crossfade(true)
    }
}

fun ImageView.loadAvatar(url: UrlString?): Disposable {
    return loadAny(url?.value) {
        placeholder(R.drawable.image_placeholder)
        error(R.drawable.image_placeholder)
        crossfade(true)
    }
}

fun ImageView.loadThumb(url: UrlString?): Disposable {
    return loadAny(url?.value) {
        crossfade(true)
    }
}

fun ImageView.clear() {
    CoilUtils.clear(this)
}
