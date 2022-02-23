package org.cnodejs.android.md.model.entity

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson
import java.time.OffsetDateTime

@JsonClass(generateAdapter = false)
enum class MessageType {
    REPLY,
    AT,
}

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

@JsonClass(generateAdapter = true)
data class Message(
    val id: String,
    val type: MessageType,
    @Json(name = "has_read") val hasRead: Boolean,
    val author: Author,
    val topic: TopicInMessage,
    val reply: ReplyInMessage,
    @Json(name = "create_at") val createAt: OffsetDateTime,
)

data class MessageWithSummary(val message: Message) {
    val replySummary = message.reply.content?.let { html -> Summary.from(html) } ?: Summary.EMPTY
}

class MessageWithSummaryJsonAdapter {
    @FromJson
    fun fromJson(message: Message): MessageWithSummary {
        return MessageWithSummary(message)
    }

    @ToJson
    fun toJson(messageWithSummary: MessageWithSummary): Message {
        return messageWithSummary.message
    }
}

@JsonClass(generateAdapter = true)
data class MessageData(
    @Json(name = "has_read_messages") val hasReadMessages: List<MessageWithSummary>,
    @Json(name = "hasnot_read_messages") val hasNotReadMessages: List<MessageWithSummary>,
)
