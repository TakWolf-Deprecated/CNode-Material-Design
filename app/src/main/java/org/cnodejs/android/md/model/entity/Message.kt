package org.cnodejs.android.md.model.entity

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

@JsonClass(generateAdapter = false)
enum class MessageType {
    REPLY,
    AT,
}

@JsonClass(generateAdapter = true)
data class Message(
    val id: String,
    val type: MessageType,
    @Json(name = "has_read") val hasRead: Boolean,
    val author: Author,
    val topic: TopicInMessage,
    val reply: ReplyInMessage,
)

@JsonClass(generateAdapter = true)
data class MessageData(
    @Json(name = "has_read_messages") val hasReadMessages: List<Message>,
    @Json(name = "hasnot_read_messages") val hasNotReadMessages: List<Message>,
)

class MessageTypeJsonAdapter {
    @FromJson
    fun fromJson(string: String): MessageType {
        return MessageType.valueOf(string.uppercase())
    }

    @ToJson
    fun toJson(type: MessageType): String {
        return type.name.lowercase()
    }
}
