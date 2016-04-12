package org.cnodejs.android.md.model.api;

import org.cnodejs.android.md.model.util.HttpUtils;
import org.cnodejs.android.md.model.util.EntityUtils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiClient {

    private ApiClient() {}

    public static final String API_BASE_URL = "https://cnodejs.org/api/v1/";

    public static final ApiService service = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(HttpUtils.client)
            .addConverterFactory(GsonConverterFactory.create(EntityUtils.gson))
            .build()
            .create(ApiService.class);

}
