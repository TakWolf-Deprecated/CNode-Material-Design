package org.cnodejs.android.md.model.entity

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson
import org.cnodejs.android.md.R

@JsonClass(generateAdapter = false)
enum class Tab(@StringRes val titleId: Int, @IdRes val tabId: Int) {
    ALL(R.string.app_name, R.id.tab_all),
    GOOD(R.string.tab_good, R.id.tab_good),
    SHARE(R.string.tab_share, R.id.tab_share),
    ASK(R.string.tab_ask, R.id.tab_ask),
    JOB(R.string.tab_job, R.id.tab_job),
    DEV(R.string.tab_dev, R.id.tab_dev),
    UNKNOWN(R.string.tab_unknown, 0);

    val queryValue: String? get() {
        return if (this == ALL || this == UNKNOWN) {
            null
        } else {
            name.lowercase()
        }
    }

    companion object {
        fun fromTabId(@IdRes tabId: Int): Tab {
            for (tab in values()) {
                if (tab.tabId == tabId) {
                    return tab
                }
            }
            error("Unknown tabId: $tabId")
        }
    }
}

class TabJsonAdapter {
    @FromJson
    fun fromJson(tabString: String): Tab {
        return Tab.valueOf(tabString.uppercase())
    }

    @ToJson
    fun toJson(tab: Tab): String? {
        return tab.queryValue
    }
}
