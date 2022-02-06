package org.cnodejs.android.md.model.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResult(
    val success: Boolean,
)

@JsonClass(generateAdapter = true)
data class DataResult<Data>(
    val success: Boolean,
    val data: Data,
)

@JsonClass(generateAdapter = true)
data class TopicIdResult(
    val success: Boolean,
    @Json(name = "topic_id") val topicId: String,
)

@JsonClass(generateAdapter = true)
data class ReplyIdResult(
    val success: Boolean,
    @Json(name = "reply_id") val replyId: String,
)

@JsonClass(generateAdapter = true)
data class UpReplyResult(
    val success: Boolean,
    val action: UpAction,
)

@JsonClass(generateAdapter = true)
data class LoginResult(
    val success: Boolean,
    val id: String,
    @Json(name = "loginname") val loginName: String,
    @Json(name = "avatar_url") val avatarUrl: String,
)

@JsonClass(generateAdapter = true)
data class MessagesMarkedAllResult(
    val success: Boolean,
    @Json(name = "marked_msgs") val markedMessages: List<IdData>,
)

@JsonClass(generateAdapter = true)
data class MessageMarkedResult(
    val success: Boolean,
    @Json(name = "marked_msg_id") val markedMessageId: String,
)
