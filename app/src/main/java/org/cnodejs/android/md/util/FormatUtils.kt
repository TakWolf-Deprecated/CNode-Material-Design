package org.cnodejs.android.md.util

import java.util.*

fun String.isAccessToken(): Boolean {
    return try {
        UUID.fromString(this)
        true
    } catch (_: Exception) {
        false
    }
}
