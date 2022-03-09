package org.cnodejs.android.md.util

import org.commonmark.ext.autolink.AutolinkExtension
import org.commonmark.ext.front.matter.YamlFrontMatterExtension
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension
import org.commonmark.ext.gfm.tables.TablesExtension
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension
import org.commonmark.ext.image.attributes.ImageAttributesExtension
import org.commonmark.ext.ins.InsExtension
import org.commonmark.ext.task.list.items.TaskListItemsExtension
import org.commonmark.node.AbstractVisitor
import org.commonmark.node.Image
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

object MarkdownUtils {
    private val parser: Parser
    private val htmlRenderer: HtmlRenderer
    private val handleVisitor: AbstractVisitor

    init {
        val extensions = listOf(
            AutolinkExtension.create(),
            StrikethroughExtension.create(),
            TablesExtension.create(),
            HeadingAnchorExtension.create(),
            InsExtension.create(),
            YamlFrontMatterExtension.create(),
            ImageAttributesExtension.create(),
            TaskListItemsExtension.create(),
        )
        parser = Parser.builder()
            .extensions(extensions)
            .build()
        htmlRenderer = HtmlRenderer.builder()
            .extensions(extensions)
            .build()
        handleVisitor = object : AbstractVisitor() {
            override fun visit(image: Image) {
                image.destination = getCompatUri(image.destination)
            }
        }
    }

    fun renderHtml(markdown: String): String {
        val doc = parser.parse(markdown)
        doc.accept(handleVisitor)
        return htmlRenderer.render(doc)
    }
}
