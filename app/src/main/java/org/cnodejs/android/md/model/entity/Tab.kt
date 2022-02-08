package org.cnodejs.android.md.model.entity

import androidx.annotation.StringRes
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson
import org.cnodejs.android.md.R

@JsonClass(generateAdapter = false)
enum class Tab(@StringRes val titleResId: Int) {
    GOOD(R.string.tab_good),
    SHARE(R.string.tab_share),
    ASK(R.string.tab_ask),
    JOB(R.string.tab_job),
    DEV(R.string.tab_dev),
    UNKNOWN(R.string.tab_unknown);

    val queryValue: String?
    get() {
        return if (this == UNKNOWN) {
            null
        } else {
            name.lowercase()
        }
    }
}

class TabJsonAdapter {
    @FromJson
    fun fromJson(tabString: String?): Tab {
        return tabString?.let { Tab.valueOf(it) } ?: Tab.UNKNOWN
    }

    @ToJson
    fun toJson(tab: Tab): String? {
        return tab.queryValue
    }
}
