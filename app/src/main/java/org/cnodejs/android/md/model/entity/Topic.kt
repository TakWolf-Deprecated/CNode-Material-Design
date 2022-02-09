package org.cnodejs.android.md.model.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.cnodejs.android.md.util.FormatUtils
import java.time.OffsetDateTime
import java.util.stream.Collectors

@JsonClass(generateAdapter = true)
data class Topic(
    val id: String,
    @Json(name = "author_id") val authorId: String,
    val author: Author,
    val title: String,
    val tab: Tab = Tab.UNKNOWN,
    @Json(name = "good") val isGood: Boolean,
    @Json(name = "top") val isTop: Boolean,
    val content: String,
    @Json(name = "visit_count") val visitCount: Int,
    @Json(name = "reply_count") val replyCount: Int,
    @Json(name = "create_at") val createAt: OffsetDateTime,
    @Json(name = "last_reply_at") val lastReplyAt: OffsetDateTime,
)

data class TopicWithSummary(val topic: Topic) {
    companion object {
        fun fromList(topics: List<Topic>): List<TopicWithSummary> {
            return topics.stream()
                .map { topic->
                    TopicWithSummary(topic)
                }
                .collect(Collectors.toList())
        }
    }

    val summary = FormatUtils.getHtmlSummary(topic.content)
}

@JsonClass(generateAdapter = true)
data class TopicWithReply(
    val id: String,
    @Json(name = "author_id") val authorId: String,
    val author: Author,
    val title: String,
    val tab: Tab = Tab.UNKNOWN,
    @Json(name = "good") val isGood: Boolean,
    @Json(name = "top") val isTop: Boolean,
    val content: String,
    @Json(name = "visit_count") val visitCount: Int,
    @Json(name = "reply_count") val replyCount: Int,
    @Json(name = "create_at") val createAt: OffsetDateTime,
    @Json(name = "last_reply_at") val lastReplyAt: OffsetDateTime,
    @Json(name = "is_collect") val isCollect: Boolean,
    val replies: List<Reply>,
)

@JsonClass(generateAdapter = true)
data class TopicInUser(
    val id: String,
    val author: Author,
    val title: String,
    @Json(name = "last_reply_at") val lastReplyAt: OffsetDateTime,
)

@JsonClass(generateAdapter = true)
data class TopicInMessage(
    val id: String,
    val title: String,
    @Json(name = "last_reply_at") val lastReplyAt: OffsetDateTime,
)
