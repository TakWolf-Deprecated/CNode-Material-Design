package org.cnodejs.android.md.model.entity

data class Account(
    val id: String,
    val loginName: String,
    val avatarUrl: String?,
    val score: Int,
)
