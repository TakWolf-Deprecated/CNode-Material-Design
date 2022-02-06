package org.cnodejs.android.md.model.entity

import androidx.annotation.StringRes
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson
import org.cnodejs.android.md.R

@JsonClass(generateAdapter = false)
enum class Tab(
    val queryValue: String?,
    @StringRes val titleResId: Int,
) {
    GOOD("good", R.string.tab_good),
    SHARE("share", R.string.tab_share),
    ASK("ask", R.string.tab_ask),
    JOB("job", R.string.tab_job),
    DEV("dev", R.string.tab_dev),
    UNKNOWN(null, R.string.tab_unknown),
}

class TabJsonAdapter {
    @FromJson
    fun fromJson(tabString: String?): Tab {
        for (tab in Tab.values()) {
            if (tabString == tab.queryValue) {
                return tab
            }
        }
        return Tab.UNKNOWN
    }

    @ToJson
    fun toJson(tab: Tab): String? {
        return tab.queryValue
    }
}
