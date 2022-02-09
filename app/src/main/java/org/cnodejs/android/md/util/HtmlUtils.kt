package org.cnodejs.android.md.util

import org.jsoup.Jsoup

object HtmlUtils {
    fun getText(html: String): String {
        return Jsoup.parseBodyFragment(html).body().text().trim()
    }
}
