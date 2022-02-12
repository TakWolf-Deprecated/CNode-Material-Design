package org.cnodejs.android.md.model.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.cnodejs.android.md.util.FormatUtils
import java.time.OffsetDateTime

interface IUser {
    val loginName: String?
    val avatarUrl: String?
    val avatarUrlCompat: String? get() = FormatUtils.getCompatAvatarUrl(avatarUrl)
}

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "loginname") override val loginName: String,
    @Json(name = "avatar_url") override val avatarUrl: String,
    val githubUsername: String,
    @Json(name = "create_at") val createAt: OffsetDateTime,
    val score: Int,
    @Json(name = "recent_topics") val recentTopics: List<TopicInUser>,
    @Json(name = "recent_replies") val recentReplies: List<TopicInUser>,
) : IUser

@JsonClass(generateAdapter = true)
data class Author(
    @Json(name = "loginname") override val loginName: String?,
    @Json(name = "avatar_url") override val avatarUrl: String?,
) : IUser

data class Account(
    val id: String,
    override val loginName: String,
    override val avatarUrl: String?,
    val score: Int,
) : IUser
