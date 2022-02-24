package org.cnodejs.android.md.util

import android.content.Context
import android.widget.TextView
import androidx.annotation.GuardedBy
import coil.request.Disposable
import coil.request.ImageRequest
import io.noties.markwon.Markwon
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.coil.CoilImagesPlugin

private class MarkdownHandler private constructor(context: Context) {
    companion object {
        private val lock = Any()

        @GuardedBy("lock")
        @Volatile
        private var instance: MarkdownHandler? = null

        fun getInstance(context: Context) = instance ?: synchronized(lock) {
            instance ?: MarkdownHandler(context.applicationContext).also { instance = it }
        }
    }

    val markwon = Markwon.builder(context)
        .usePlugin(CoilImagesPlugin.create(object : CoilImagesPlugin.CoilStore {
            override fun load(drawable: AsyncDrawable): ImageRequest {
                return ImageRequest.Builder(context)
                    .data(getCompatUri(drawable.destination))
                    .build()
            }

            override fun cancel(disposable: Disposable) {
                disposable.dispose()
            }
        }, AppCoil.getInstance(context).imageLoader))
        .build()
}

private val Context.markwon: Markwon get() = MarkdownHandler.getInstance(this).markwon

fun TextView.setMarkdown(markdown: String?) {
    context.markwon.setMarkdown(this, markdown ?: "")
}
