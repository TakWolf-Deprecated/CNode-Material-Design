package org.cnodejs.android.md.model.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.OffsetDateTime

@JsonClass(generateAdapter = true)
data class Topic(
    val id: String,
    @Json(name = "author_id") val authorId: String,
    val author: Author,
    val title: String,
    val tab: Tab,
    val good: Boolean,
    val top: Boolean,
    val content: String,
    @Json(name = "visit_count") val visitCount: Int,
    @Json(name = "reply_count") val replyCount: Int,
    @Json(name = "create_at") val createAt: OffsetDateTime,
    @Json(name = "last_reply_at") val lastReplyAt: OffsetDateTime,
)

@JsonClass(generateAdapter = true)
data class TopicWithReply(
    val id: String,
    @Json(name = "author_id") val authorId: String,
    val author: Author,
    val title: String,
    val tab: String,
    val good: Boolean,
    val top: Boolean,
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
