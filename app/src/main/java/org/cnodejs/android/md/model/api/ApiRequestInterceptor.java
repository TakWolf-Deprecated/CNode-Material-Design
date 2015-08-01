package org.cnodejs.android.md.model.api;

import android.os.Build;

import retrofit.RequestInterceptor;

public class ApiRequestInterceptor implements RequestInterceptor {

    private static final String APPLICATION_JSON = "application/json";
    private final String USER_AGENT;

    public ApiRequestInterceptor(String versionName) {
        USER_AGENT = "CNodeMD/" + versionName + " (Android " + Build.VERSION.RELEASE + "; " + Build.MANUFACTURER + " " + Build.MODEL + ")";
    }

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("User-Agent", USER_AGENT);
        request.addHeader("Accept", APPLICATION_JSON);
    }

}
