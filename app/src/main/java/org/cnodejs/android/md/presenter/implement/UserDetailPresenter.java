package org.cnodejs.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import org.cnodejs.android.md.display.view.IUserDetailView;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.CallbackAdapter;
import org.cnodejs.android.md.model.api.DefaultToastCallback;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.User;
import org.cnodejs.android.md.presenter.contract.IUserDetailPresenter;
import org.cnodejs.android.md.util.HandlerUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class UserDetailPresenter implements IUserDetailPresenter {

    private final Activity activity;
    private final IUserDetailView userDetailView;

    public UserDetailPresenter(@NonNull Activity activity, @NonNull IUserDetailView userDetailView) {
        this.activity = activity;
        this.userDetailView = userDetailView;
    }

    @Override
    public void getUserAsyncTask(@NonNull String loginName) {
        userDetailView.onGetUserStart();
        Call<Result.Data<User>> call = ApiClient.service.getUser(loginName);
        call.enqueue(new CallbackAdapter<Result.Data<User>>() {

            private long startLoadingTime = System.currentTimeMillis();

            private long getPostTime() {
                long postTime = 1000 - (System.currentTimeMillis() - startLoadingTime);
                if (postTime > 0) {
                    return postTime;
                } else {
                    return 0;
                }
            }

            @Override
            public boolean onResultOk(Response<Result.Data<User>> response, final Result.Data<User> result) {
                HandlerUtils.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (!userDetailView.onGetUserResultOk(result)) {
                            onFinish();
                        }
                    }

                }, getPostTime());
                return true;
            }

            @Override
            public boolean onResultError(final Response<Result.Data<User>> response, final Result.Error error) {
                HandlerUtils.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        boolean interrupt;
                        if (response.code() == 404) {
                            interrupt = userDetailView.onGetUserResultError(error);
                        } else {
                            interrupt = userDetailView.onGetUserLoadError();
                        }
                        if (!interrupt) {
                            onFinish();
                        }
                    }

                }, getPostTime());
                return true;
            }

            @Override
            public boolean onCallException(Throwable t, Result.Error error) {
                HandlerUtils.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (!userDetailView.onGetUserLoadError()) {
                            onFinish();
                        }
                    }

                }, getPostTime());
                return true;
            }

            @Override
            public void onFinish() {
                userDetailView.onGetUserFinish();
            }

        });
    }

    @Override
    public void getCollectTopicListAsyncTask(@NonNull String loginName) {
        Call<Result.Data<List<Topic>>> call = ApiClient.service.getCollectTopicList(loginName);
        call.enqueue(new DefaultToastCallback<Result.Data<List<Topic>>>(activity) {

            @Override
            public boolean onResultOk(Response<Result.Data<List<Topic>>> response, Result.Data<List<Topic>> result) {
                return userDetailView.onGetCollectTopicListResultOk(result);
            }

        });
    }

}
