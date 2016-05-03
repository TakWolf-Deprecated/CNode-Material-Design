package org.cnodejs.android.md.display.view;

import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.Result;

import retrofit2.Call;

public interface ILoginView {

    void onAccessTokenFormatError();

    void onLoginStart(@NonNull Call<Result.Login> call);

    boolean onLoginResultOk(@NonNull String accessToken, @NonNull Result.Login loginInfo);

    boolean onLoginResultErrorAuth(@NonNull Result.Error error);

    void onLoginFinish();

}
