package org.cnodejs.android.md.model.api;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CallbackAdapter<T> implements Callback<T> {

    @Override
    public void success(T t, Response response) {}

    @Override
    public void failure(RetrofitError error) {}

}
