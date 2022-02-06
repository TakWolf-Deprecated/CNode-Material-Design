package org.cnodejs.android.md.model.api

import android.os.Build
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.cnodejs.android.md.BuildConfig
import org.cnodejs.android.md.util.HttpUtils
import org.cnodejs.android.md.util.JsonUtils
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object CNodeClient {
    private val USER_AGENT = "${BuildConfig.USER_AGENT_NAME}/${BuildConfig.VERSION_NAME} (Android ${Build.VERSION.RELEASE}; ${Build.MODEL} Build/${Build.ID})"

    val api: CNodeApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(OkHttpClient.Builder()
                .addInterceptor { chain ->
                    chain.proceed(chain.request().newBuilder()
                        .header(HttpUtils.HEADER_USER_AGENT, USER_AGENT)
                        .build())
                }
                .apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        })
                    }
                }
                .build())
            .addConverterFactory(MoshiConverterFactory.create(JsonUtils.moshi))
            .build()
        api = retrofit.create(CNodeApi::class.java)
    }
}
