package org.cnodejs.android.md.model.api;

import org.cnodejs.android.md.model.entity.Result;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackgroundCallback<T extends Result> implements Callback<T>, CallbackLifecycle<T> {

    @Override
    public final void onResponse(Call<T> call, Response<T> response) {
        boolean interrupt;
        if (response.isSuccessful()) {
            interrupt = onResultOk(response.code(), response.headers(), response.body());
        } else {
            interrupt = onResultError(response.code(), response.headers(), Result.buildError(response));
        }
        if (!interrupt) {
            onFinish();
        }
    }

    @Override
    public final void onFailure(Call<T> call, Throwable t) {
        boolean interrupt;
        if (call.isCanceled()) {
            interrupt = onCallCancel();
        } else {
            interrupt = onCallException(t, Result.buildError(t));
        }
        if (!interrupt) {
            onFinish();
        }
    }

    @Override
    public boolean onResultOk(int code, Headers headers, T result) {
        return false;
    }

    @Override
    public boolean onResultError(int code, Headers headers, Result.Error error) {
        return false;
    }

    @Override
    public boolean onCallCancel() {
        return false;
    }

    @Override
    public boolean onCallException(Throwable t, Result.Error error) {
        return false;
    }

    @Override
    public void onFinish() {}

}
