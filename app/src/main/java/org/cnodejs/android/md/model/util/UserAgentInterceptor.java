package org.cnodejs.android.md.model.util;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class UserAgentInterceptor implements Interceptor {

    private final String userAgent;

    public UserAgentInterceptor(String userAgent) {
        if (TextUtils.isEmpty(userAgent)) {
            throw new IllegalArgumentException("userAgent is null.");
        }
        this.userAgent = userAgent;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .header("User-Agent", userAgent)
                .build();
        return chain.proceed(request);
    }

}
