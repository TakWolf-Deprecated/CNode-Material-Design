package org.cnodejs.android.md.util

import java.util.*

object FormatUtils {
    fun isAccessToken(accessToken: String): Boolean {
        return try {
            UUID.fromString(accessToken)
            true
        } catch (_: Exception) {
            false
        }
    }
}
