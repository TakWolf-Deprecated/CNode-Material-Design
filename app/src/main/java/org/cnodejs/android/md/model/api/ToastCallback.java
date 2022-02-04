package org.cnodejs.android.md.model.api;

import android.app.Activity;
import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.ErrorResult;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.ui.util.ActivityUtils;
import org.cnodejs.android.md.ui.util.ToastUtils;

import okhttp3.Headers;

public class ToastCallback<T extends Result> extends ForegroundCallback<T> {

    public ToastCallback(@NonNull Activity activity) {
        super(activity);
    }

    @Override
    public boolean onResultError(int code, Headers headers, ErrorResult errorResult) {
        return onAnyError(errorResult);
    }

    @Override
    public boolean onCallException(Throwable t, ErrorResult errorResult) {
        return onAnyError(errorResult);
    }

    public boolean onAnyError(ErrorResult errorResult) {
        toastError(errorResult);
        return false;
    }

    protected final void toastError(ErrorResult errorResult) {
        Activity activity = getActivity();
        if (ActivityUtils.isAlive(activity)) {
            ToastUtils.with(activity).show(errorResult.getMessage());
        }
    }

}
