package org.cnodejs.android.md.model.api;

import android.content.Context;

import org.cnodejs.android.md.BuildConfig;
import org.cnodejs.android.md.util.GsonWrapper;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public final class ApiClient {

    public static final String ROOT_HOST = "https://cnodejs.org";
    public static final String API_HOST = ROOT_HOST + "/api";

    public static final ApiService service = new RestAdapter.Builder()
                .setEndpoint(API_HOST)
                .setConverter(new GsonConverter(GsonWrapper.gson))
                .setRequestInterceptor(new ApiRequestInterceptor(BuildConfig.VERSION_NAME))
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .build()
                .create(ApiService.class);

}
