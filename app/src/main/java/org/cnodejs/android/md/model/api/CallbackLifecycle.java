package org.cnodejs.android.md.model.api;

import org.cnodejs.android.md.model.entity.Result;

import retrofit2.Response;

public interface CallbackLifecycle<T> {

    boolean onResultOk(Response<T> response, T result);

    boolean onResultError(Response<T> response, Result.Error error);

    boolean onCallCancel();

    boolean onCallException(Throwable t, Result.Error error);

    void onFinish();

}
