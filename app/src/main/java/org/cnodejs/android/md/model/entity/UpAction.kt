package org.cnodejs.android.md.model.entity

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

@JsonClass(generateAdapter = false)
enum class UpAction {
    UP,
    DOWN,
}

object UpActionJsonAdapter {
    @FromJson
    fun fromJson(string: String): UpAction {
        return UpAction.valueOf(string.uppercase())
    }

    @ToJson
    fun toJson(upAction: UpAction): String {
        return upAction.name.lowercase()
    }
}
