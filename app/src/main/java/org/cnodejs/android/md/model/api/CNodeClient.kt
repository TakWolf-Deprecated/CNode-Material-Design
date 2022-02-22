package org.cnodejs.android.md.model.api

import android.content.Context
import androidx.annotation.GuardedBy
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.cnodejs.android.md.BuildConfig
import org.cnodejs.android.md.bus.AuthInvalidEvent
import org.cnodejs.android.md.model.store.AppStoreHolder
import org.cnodejs.android.md.util.HttpUtils
import org.cnodejs.android.md.util.JsonUtils
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class CNodeClient private constructor(context: Context) {
    companion object {
        private val lock = Any()

        @GuardedBy("lock")
        @Volatile
        private var instance: CNodeClient? = null

        fun getInstance(context: Context) = instance ?: synchronized(lock) {
            instance ?: CNodeClient(context.applicationContext).also { instance = it }
        }
    }

    private val accountStore = AppStoreHolder.getInstance(context).accountStore

    val api: CNodeApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(CNodeDefine.API_BASE_URL)
            .client(OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val response = chain.proceed(chain.request().newBuilder()
                        .header(HttpUtils.HEADER_USER_AGENT, CNodeDefine.USER_AGENT)
                        .build())
                    if (response.code == 401 && accountStore.getAccessToken() != null) {
                        accountStore.logout()
                        EventBus.getDefault().post(AuthInvalidEvent)
                    }
                    response
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
