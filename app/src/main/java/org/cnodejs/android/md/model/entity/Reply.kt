package org.cnodejs.android.md.model.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.OffsetDateTime

@JsonClass(generateAdapter = true)
data class Reply(
    val id: String,
    val author: Author,
    val content: String,
    @Json(name = "reply_id") val replyId: String?,
    @Json(name = "is_uped") val isUped: Boolean,
    val ups: List<String>,
    @Json(name = "create_at") val createAt: OffsetDateTime,
)

@JsonClass(generateAdapter = true)
data class ReplyInMessage(
    val id: String,
    val content: String,
    val ups: List<String>,
    @Json(name = "create_at") val createAt: OffsetDateTime,
)
