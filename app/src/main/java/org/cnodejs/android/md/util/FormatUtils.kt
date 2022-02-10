package org.cnodejs.android.md.util

import org.jsoup.Jsoup
import java.util.*

object FormatUtils {
    fun getHtmlSummary(html: String): String {
        return Jsoup.parseBodyFragment(html).body().text().trim()
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
