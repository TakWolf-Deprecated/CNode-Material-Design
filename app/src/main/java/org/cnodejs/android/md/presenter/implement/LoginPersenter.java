package org.cnodejs.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import org.cnodejs.android.md.display.view.ILoginView;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.DefaultToastCallback;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.presenter.contract.ILoginPresenter;

import retrofit2.Call;
import retrofit2.Response;

public class LoginPersenter implements ILoginPresenter {

    private final Activity activity;
    private final ILoginView loginView;

    public LoginPersenter(@NonNull Activity activity, @NonNull ILoginView loginView) {
        this.activity = activity;
        this.loginView = loginView;
    }

    @Override
    public Call<Result.Login> loginAsyncTask(@NonNull final String accessToken) {
        Call<Result.Login> call = ApiClient.service.accessToken(accessToken);
        loginView.onLoginStart(call);
        call.enqueue(new DefaultToastCallback<Result.Login>(activity) {

            @Override
            public boolean onResultOk(Response<Result.Login> response, Result.Login loginInfo) {
                return loginView.onLoginResultOk(accessToken, loginInfo);
            }

            @Override
            public boolean onResultErrorAuth(Response<Result.Login> response, Result.Error error) {
                return loginView.onLoginResultErrorAuth(error);
            }

            @Override
            public void onFinish() {
                loginView.onLoginFinish();
            }

        });
        return call;
    }

}
