package org.cnodejs.android.md.model.api

import android.os.Build
import org.cnodejs.android.md.BuildConfig

object CNodeDefine {
    const val HOST_BASE_URL = BuildConfig.HOST_BASE_URL

    const val API_BASE_URL = "${HOST_BASE_URL}/api/v1/"

    const val USER_PATH_FRAGMENT = "/user/"
    const val USER_LINK_PREFIX = "${HOST_BASE_URL}${USER_PATH_FRAGMENT}"
    const val TOPIC_PATH_FRAGMENT = "/topic/"
    const val TOPIC_LINK_PREFIX = "${HOST_BASE_URL}${TOPIC_PATH_FRAGMENT}"

    const val GITHUB_LOGIN_URL = "${HOST_BASE_URL}/auth/github"

    val USER_AGENT = "${BuildConfig.USER_AGENT_NAME}/${BuildConfig.VERSION_NAME} (Android ${Build.VERSION.RELEASE}; ${Build.MODEL} Build/${Build.ID})"
}
