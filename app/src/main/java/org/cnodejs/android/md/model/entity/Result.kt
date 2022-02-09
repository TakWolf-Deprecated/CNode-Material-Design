package org.cnodejs.android.md.model.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResult(
    @Json(name = "success") val isSuccessful: Boolean,
)

@JsonClass(generateAdapter = true)
data class DataResult<Data>(
    @Json(name = "success") val isSuccessful: Boolean,
    val data: Data,
)

@JsonClass(generateAdapter = true)
data class TopicIdResult(
    @Json(name = "success") val isSuccessful: Boolean,
    @Json(name = "topic_id") val topicId: String,
)

@JsonClass(generateAdapter = true)
data class ReplyIdResult(
    @Json(name = "success") val isSuccessful: Boolean,
    @Json(name = "reply_id") val replyId: String,
)

@JsonClass(generateAdapter = true)
data class UpReplyResult(
    @Json(name = "success") val isSuccessful: Boolean,
    val action: UpAction,
)

@JsonClass(generateAdapter = true)
data class LoginResult(
    @Json(name = "success") val isSuccessful: Boolean,
    val id: String,
    @Json(name = "loginname") val loginName: String,
    @Json(name = "avatar_url") val avatarUrl: String,
)

@JsonClass(generateAdapter = true)
data class MessagesMarkedAllResult(
    @Json(name = "success") val isSuccessful: Boolean,
    @Json(name = "marked_msgs") val markedMessages: List<IdData>,
)

@JsonClass(generateAdapter = true)
data class MessageMarkedResult(
    @Json(name = "success") val isSuccessful: Boolean,
    @Json(name = "marked_msg_id") val markedMessageId: String,
)
