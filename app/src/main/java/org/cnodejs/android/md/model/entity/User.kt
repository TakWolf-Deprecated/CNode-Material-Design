package org.cnodejs.android.md.model.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.OffsetDateTime

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "loginname") val loginName: String,
    @Json(name = "avatar_url") val avatarUrl: String,
    val githubUsername: String,
    @Json(name = "create_at") val createAt: OffsetDateTime,
    val score: Int,
    @Json(name = "recent_topics") val recentTopics: List<TopicInUser>,
    @Json(name = "recent_replies") val recentReplies: List<TopicInUser>,
)
