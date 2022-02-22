package org.cnodejs.android.md.util

import android.content.Context
import android.widget.TextView
import androidx.annotation.GuardedBy
import io.noties.markwon.Markwon

class MarkdownHandler private constructor(context: Context) {
    companion object {
        private val lock = Any()

        @GuardedBy("lock")
        @Volatile
        private var instance: MarkdownHandler? = null

        fun getInstance(context: Context) = instance ?: synchronized(lock) {
            instance ?: MarkdownHandler(context.applicationContext).also { instance = it }
        }
    }

    internal val markwon = Markwon.create(context)
}

val Context.markdownHandler: MarkdownHandler get() = MarkdownHandler.getInstance(this)

fun TextView.setMarkdown(markdown: String?) {
    context.markdownHandler.markwon.setMarkdown(this, markdown ?: "")
}
