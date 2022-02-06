package org.cnodejs.android.md.model.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IdData(
    val id: String,
)
