package org.cnodejs.android.md.model.entity

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import org.cnodejs.android.md.model.api.CNodeDefine
import org.cnodejs.android.md.util.MarkdownUtils
import org.jsoup.Jsoup

@JsonClass(generateAdapter = true)
data class Content(
    val markdown: String,
    val html: String,
    val summary: String,
    val images: List<String>,
) {
    companion object {
        fun fromMarkdown(markdown: String): Content {
            val mdDoc = MarkdownUtils.parser.parse(markdown)
            val html = MarkdownUtils.htmlRenderer.render(mdDoc)
            val htmlDoc = Jsoup.parseBodyFragment(html, CNodeDefine.HOST_BASE_URL)
            val summary = htmlDoc.body().text().trim()
            val images = htmlDoc.getElementsByTag("img").map { imgNode -> imgNode.attr("src") }
            return Content(markdown, html, summary, images)
        }
    }
}

class ContentCompatJsonAdapter {
    @FromJson
    fun fromJson(markdown: String): Content {
        return Content.fromMarkdown(markdown)
    }
}
