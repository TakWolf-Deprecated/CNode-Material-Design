package org.cnodejs.android.md.util

import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import org.cnodejs.android.md.model.entity.*
import java.time.OffsetDateTime

object JsonUtils {
    val moshi: Moshi = Moshi.Builder()
        .add(OffsetDateTimeJsonAdapter())
        .add(UrlStringJsonAdapter())
        .add(ContentCompatJsonAdapter())
        .add(TabJsonAdapter())
        .add(UpActionJsonAdapter())
        .add(MessageTypeJsonAdapter())
        .build()
}

private class OffsetDateTimeJsonAdapter {
    @FromJson
    fun fromJson(iso8601: String): OffsetDateTime {
        return OffsetDateTime.parse(iso8601)
    }

    @ToJson
    fun toJson(offsetDateTime: OffsetDateTime): String {
        return offsetDateTime.toString()
    }
}
