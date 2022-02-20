package org.cnodejs.android.md.util

import android.content.res.Resources
import org.cnodejs.android.md.R
import java.time.Duration
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

private const val MINUTE = 60 * 1000L
private const val HOUR = 60 * MINUTE
private const val DAY = 24 * HOUR
private const val WEEK = 7 * DAY
private const val MONTH = 31 * DAY
private const val YEAR = 12 * MONTH

private val displayDateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm")

fun OffsetDateTime.timeSpanStringFromNow(resources: Resources): String {
    val offset = Duration.between(this, OffsetDateTime.now()).toMillis()
    return when {
        offset > YEAR -> {
            format(displayDateTimeFormatter)
        }
        offset > MONTH -> {
            resources.getString(R.string.d_months_ago, offset / MONTH)
        }
        offset > WEEK -> {
            resources.getString(R.string.d_weeks_ago, offset / WEEK)
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
