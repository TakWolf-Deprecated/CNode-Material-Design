package org.cnodejs.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.DefaultCallback;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.presenter.contract.ILoginPresenter;
import org.cnodejs.android.md.ui.view.ILoginView;
import org.cnodejs.android.md.util.FormatUtils;

import okhttp3.Headers;
import retrofit2.Call;

public class LoginPresenter implements ILoginPresenter {

    private final Activity activity;
    private final ILoginView loginView;

    public LoginPresenter(@NonNull Activity activity, @NonNull ILoginView loginView) {
        this.activity = activity;
        this.loginView = loginView;
    }

    @Override
    public void loginAsyncTask(final String accessToken) {
        if (!FormatUtils.isAccessToken(accessToken)) {
            loginView.onAccessTokenError(activity.getString(R.string.access_token_format_error));
        } else {
            Call<Result.Login> call = ApiClient.service.accessToken(accessToken);
            loginView.onLoginStart(call);
            call.enqueue(new DefaultCallback<Result.Login>(activity) {

                @Override
                public boolean onResultOk(int code, Headers headers, Result.Login loginInfo) {
                    loginView.onLoginOk(accessToken, loginInfo);
                    return false;
                }

                @Override
                public boolean onResultAuthError(int code, Headers headers, Result.Error error) {
                    loginView.onAccessTokenError(getActivity().getString(R.string.access_token_auth_error));
                    return false;
                }

                @Override
                public void onFinish() {
                    loginView.onLoginFinish();
                }

            });
        }
    }

}
