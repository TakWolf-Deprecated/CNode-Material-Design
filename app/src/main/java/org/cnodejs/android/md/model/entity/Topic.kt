package org.cnodejs.android.md.model.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.OffsetDateTime

interface ITopic {
    val id: String
    val author: Author
    val title: String
}

interface ITopicSimple : ITopic {
    val lastReplyAt: OffsetDateTime
}

@JsonClass(generateAdapter = true)
data class Topic(
    override val id: String,
    @Json(name = "author_id") val authorId: String,
    override val author: Author,
    override val title: String,
    val tab: Tab = Tab.UNKNOWN,
    @Json(name = "good") val isGood: Boolean,
    @Json(name = "top") val isTop: Boolean,
    val content: Content,
    @Json(name = "visit_count") val visitCount: Int,
    @Json(name = "reply_count") val replyCount: Int,
    @Json(name = "create_at") val createAt: OffsetDateTime,
    @Json(name = "last_reply_at") override val lastReplyAt: OffsetDateTime,
) : ITopicSimple

@JsonClass(generateAdapter = true)
data class TopicDetail(
    override val id: String,
    @Json(name = "author_id") val authorId: String,
    override val author: Author,
    override val title: String,
    val tab: Tab = Tab.UNKNOWN,
    @Json(name = "good") val isGood: Boolean,
    @Json(name = "top") val isTop: Boolean,
    val content: Content,
    @Json(name = "visit_count") val visitCount: Int,
    @Json(name = "reply_count") val replyCount: Int,
    @Json(name = "create_at") val createAt: OffsetDateTime,
    @Json(name = "last_reply_at") val lastReplyAt: OffsetDateTime,
    @Json(name = "is_collect") val isCollect: Boolean,
    val replies: List<Reply>,
) : ITopic

@JsonClass(generateAdapter = true)
data class TopicInUser(
    override val id: String,
    override val author: Author,
    override val title: String,
    @Json(name = "last_reply_at") override val lastReplyAt: OffsetDateTime,
) : ITopicSimple

@JsonClass(generateAdapter = true)
data class TopicInMessage(
    val id: String,
    val title: String,
    @Json(name = "last_reply_at") val lastReplyAt: OffsetDateTime,
)
