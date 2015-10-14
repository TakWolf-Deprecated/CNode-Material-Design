package org.cnodejs.android.md.model.api;

import android.os.Build;

import org.cnodejs.android.md.BuildConfig;

import retrofit.RequestInterceptor;

public class ApiRequestInterceptor implements RequestInterceptor {

    private static final String APPLICATION_JSON = "application/json";
    private static final String USER_AGENT = "CNodeMD/" + BuildConfig.VERSION_NAME + " (Android " + Build.VERSION.RELEASE + "; " + Build.MANUFACTURER + " - " + Build.MODEL + ")";

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("Accept", APPLICATION_JSON);
        request.addHeader("User-Agent", USER_AGENT);
    }

}
