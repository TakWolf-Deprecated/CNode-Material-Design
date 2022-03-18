package org.cnodejs.android.md.util

import android.content.Context
import android.os.StatFs
import okhttp3.Cache
import java.io.File

object HttpUtils {
    const val HEADER_USER_AGENT = "User-Agent"

    private const val DISK_CACHE_PERCENTAGE = 0.02
    private const val MIN_DISK_CACHE_SIZE_BYTES = 10L * 1024 * 1024
    private const val MAX_DISK_CACHE_SIZE_BYTES = 250L * 1024 * 1024

    fun createCache(context: Context, dirName: String): Cache {
        val cacheDir = File(context.cacheDir, dirName).apply { mkdirs() }
        val cacheSize = try {
            val info = StatFs(cacheDir.absolutePath)
            val size = DISK_CACHE_PERCENTAGE * info.blockCountLong * info.blockSizeLong
            size.toLong().coerceIn(MIN_DISK_CACHE_SIZE_BYTES, MAX_DISK_CACHE_SIZE_BYTES)
        } catch (_: Exception) {
            MIN_DISK_CACHE_SIZE_BYTES
        }
        return Cache(cacheDir, cacheSize)
    }

    fun getCompatUrl(raw: String): String {
        var url = raw
        if (url.startsWith("//")) {
            url = "https:${url}"
        }
        return url
    }
}
