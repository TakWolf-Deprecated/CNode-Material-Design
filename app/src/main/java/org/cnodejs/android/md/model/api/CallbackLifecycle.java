package org.cnodejs.android.md.model.api;

import org.cnodejs.android.md.model.entity.ErrorResult;
import org.cnodejs.android.md.model.entity.Result;

import okhttp3.Headers;

public interface CallbackLifecycle<T extends Result> {

    boolean onResultOk(int code, Headers headers, T result);

    boolean onResultError(int code, Headers headers, ErrorResult errorResult);

    boolean onCallCancel();

    boolean onCallException(Throwable t, ErrorResult errorResult);

    void onFinish();

}
