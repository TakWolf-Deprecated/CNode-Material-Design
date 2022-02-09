package org.cnodejs.android.md.model.api

import android.os.Build
import org.cnodejs.android.md.BuildConfig

object CNodeDefine {
    const val API_BASE_URL = "${BuildConfig.HOST_BASE_URL}/api/v1/"

    val USER_AGENT = "${BuildConfig.USER_AGENT_NAME}/${BuildConfig.VERSION_NAME} (Android ${Build.VERSION.RELEASE}; ${Build.MODEL} Build/${Build.ID})"
}
