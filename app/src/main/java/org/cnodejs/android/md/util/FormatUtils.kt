package org.cnodejs.android.md.util

import android.content.res.Resources
import org.cnodejs.android.md.R
import org.jsoup.Jsoup
import java.time.Duration
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object FormatUtils {
    private const val MINUTE = 60 * 1000L
    private const val HOUR = 60 * MINUTE
    private const val DAY = 24 * HOUR
    private const val WEEK = 7 * DAY

    private val displayDateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm")

    fun getRelativeTimeSpanString(resources: Resources, dateTime: OffsetDateTime): String {
        val offset = Duration.between(dateTime, OffsetDateTime.now()).toMillis()
        return when {
            offset > WEEK -> {
                dateTime.format(displayDateTimeFormatter)
            }
            offset > DAY -> {
                resources.getString(R.string.d_days_ago, offset / DAY)
            }
            offset > HOUR -> {
                resources.getString(R.string.d_hours_ago, offset / HOUR)
            }
            offset > MINUTE -> {
                resources.getString(R.string.d_minutes_ago, offset / MINUTE)
            }
            else -> {
                resources.getString(R.string.just_now)
            }
        }
    }

    fun getCompatAvatarUrl(url: String?): String? {
        return url?.let {
            if (it.startsWith("//gravatar.com/avatar/")) {
                "https:${it}"
            } else {
                it
            }
        }
    }

    fun getHtmlSummary(html: String): String {
        return Jsoup.parseBodyFragment(html).body().text().trim()
    }
}
