package org.cnodejs.android.md.model.entity

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson
import org.cnodejs.android.md.model.api.CNodeDefine
import org.cnodejs.android.md.util.HttpUtils
import org.cnodejs.android.md.util.MarkdownUtils
import org.jsoup.Jsoup

@JsonClass(generateAdapter = true)
data class Content(
    val markdown: String,
    val html: String,
    val summary: String,
    val images: List<UrlString>,
) {
    companion object {
        fun fromMarkdown(markdown: String): Content {
            val html = MarkdownUtils.renderHtml(markdown)
            val doc = Jsoup.parseBodyFragment(html, CNodeDefine.HOST_BASE_URL)
            val summary = doc.body().text().trim()
            val images = doc.getElementsByTag("img").map { imgNode -> UrlString(imgNode.attr("src")) }
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

class UrlString(raw: String) {
    val value = HttpUtils.getCompatUrl(raw)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UrlString

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return value
    }
}

class UrlStringJsonAdapter {
    @FromJson
    fun fromJson(string: String): UrlString {
        return UrlString(string)
    }

    @ToJson
    fun toJson(urlString: UrlString): String {
        return urlString.value
    }
}
