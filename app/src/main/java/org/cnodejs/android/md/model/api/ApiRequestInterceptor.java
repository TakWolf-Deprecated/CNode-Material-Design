package org.cnodejs.android.md.model.api;

import retrofit.RequestInterceptor;

public class ApiRequestInterceptor implements RequestInterceptor {

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("User-Agent", Define.USER_AGENT);
        request.addHeader("Accept", Define.HTTP_ACCEPT);
    }

}
