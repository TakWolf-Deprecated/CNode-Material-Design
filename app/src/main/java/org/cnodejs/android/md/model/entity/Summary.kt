package org.cnodejs.android.md.model.entity

import org.jsoup.Jsoup

data class Summary(
    val text: String,
    val images: List<String>,
) {
    companion object {
        val EMPTY = Summary("", emptyList())

        fun from(html: String): Summary {
            val doc = Jsoup.parseBodyFragment(html)
            val text = doc.body().text().trim()
            val images = doc.getElementsByTag("img").map { imgNode -> imgNode.attr("src") }
            return Summary(text, images)
        }
    }
}
