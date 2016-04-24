package org.cnodejs.android.md.model.api;

import org.cnodejs.android.md.model.entity.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallbackAdapter<T extends Result> implements Callback<T> {

    @Override
    public final void onResponse(Call<T> call, Response<T> response) {
        boolean interrupt;
        if (response.isSuccessful()) {
            interrupt = onResultOk(response, response.body());
        } else {
            interrupt = onResultError(response, Result.buildError(response));
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

    public boolean onResultOk(Response<T> response, T result) {
        return false;
    }

    public boolean onResultError(Response<T> response, Result.Error error) {
        return false;
    }

    public boolean onCallCancel() {
        return false;
    }

    public boolean onCallException(Throwable t, Result.Error error) {
        return false;
    }

    public void onFinish() {}

}
