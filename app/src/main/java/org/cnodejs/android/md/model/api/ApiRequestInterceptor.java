package org.cnodejs.android.md.model.api;

import retrofit.RequestInterceptor;

public class ApiRequestInterceptor implements RequestInterceptor {

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("User-Agent", ApiDefine.USER_AGENT);
        request.addHeader("Accept", ApiDefine.HTTP_ACCEPT);
    }

}
