package org.cnodejs.android.md.util

import org.jsoup.Jsoup
import org.jsoup.nodes.TextNode
import java.util.*

object FormatUtils {
    fun getHtmlSummary(html: String): String {
        val doc = Jsoup.parseBodyFragment(html)
        doc.getElementsByTag("img").forEach { imgNode ->
            imgNode.replaceWith(TextNode("[图片]"))
        }
        return doc.body().text().trim()
    }

    fun getCompatAvatarUrl(url: String?): String? {
        return url?.let {
            if (it.startsWith("//gravatar.com/avatar/")) {
                "https:${it}"
            } else {
                it
            }
        }
    }

    fun isAccessToken(accessToken: String): Boolean {
        return try {
            UUID.fromString(accessToken)
            true
        } catch (_: Exception) {
            false
        }
    }
}
