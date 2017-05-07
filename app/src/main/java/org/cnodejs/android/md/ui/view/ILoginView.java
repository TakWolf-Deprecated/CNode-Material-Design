package org.cnodejs.android.md.ui.view;

import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.Result;

import retrofit2.Call;

public interface ILoginView {

    void onAccessTokenError(@NonNull String message);

    void onLoginOk(@NonNull String accessToken, @NonNull Result.Login loginInfo);

    void onLoginStart(@NonNull Call<Result.Login> call);

    void onLoginFinish();

}
