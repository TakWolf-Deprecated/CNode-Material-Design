package org.cnodejs.android.md.util

import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

object MarkdownUtils {
    val parser: Parser = Parser.builder().build()
    val htmlRenderer: HtmlRenderer = HtmlRenderer.builder().build()
}
