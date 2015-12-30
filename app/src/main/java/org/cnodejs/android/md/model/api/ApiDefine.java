package org.cnodejs.android.md.model.api;

import android.os.Build;

import org.cnodejs.android.md.BuildConfig;

public final class ApiDefine {

    private ApiDefine() {}

    public static final String API_HOST = "https://cnodejs.org/api";

    public static final String USER_AGENT = "CNodeMD/" + BuildConfig.VERSION_NAME + " (Android " + Build.VERSION.RELEASE + "; " + Build.MANUFACTURER + " - " + Build.MODEL + ")";
    public static final String HTTP_ACCEPT = "application/json";

}
