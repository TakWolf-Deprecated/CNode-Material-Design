package org.cnodejs.android.md.model.api;

import org.cnodejs.android.md.BuildConfig;
import org.cnodejs.android.md.util.gson.GsonWrapper;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public final class ApiClient {

    private ApiClient() {}

    private static final String API_HOST = "https://cnodejs.org/api";

    public static final ApiService service = new RestAdapter.Builder()
            .setEndpoint(API_HOST)
            .setConverter(new GsonConverter(GsonWrapper.gson))
            .setRequestInterceptor(new ApiRequestInterceptor())
            .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
            .build()
            .create(ApiService.class);

}
