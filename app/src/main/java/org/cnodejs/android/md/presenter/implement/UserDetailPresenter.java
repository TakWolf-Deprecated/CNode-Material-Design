package org.cnodejs.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.CallbackAdapter;
import org.cnodejs.android.md.model.api.DefaultCallbackAdapter;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.User;
import org.cnodejs.android.md.presenter.contract.IUserDetailPresenter;
import org.cnodejs.android.md.ui.view.IUserDetailView;
import org.cnodejs.android.md.util.HandlerUtils;

import java.util.List;

import retrofit2.Response;

public class UserDetailPresenter implements IUserDetailPresenter {

    private final Activity activity;
    private final IUserDetailView userDetailView;

    private boolean loading = false;

    public UserDetailPresenter(@NonNull Activity activity, @NonNull IUserDetailView userDetailView) {
        this.activity = activity;
        this.userDetailView = userDetailView;
    }

    @Override
    public void getUserAsyncTask(@NonNull String loginName) {
        if (!loading) {
            loading = true;
            userDetailView.onGetUserStart();
            ApiClient.service.getUser(loginName).enqueue(new CallbackAdapter<Result.Data<User>>() {

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
                public boolean onResultOk(final Response<Result.Data<User>> response, final Result.Data<User> result) {
                    HandlerUtils.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            userDetailView.onGetUserOk(result.getData());
                            onFinish();
                        }

                    }, getPostTime());
                    return true;
                }

                @Override
                public boolean onResultError(final Response<Result.Data<User>> response, final Result.Error error) {
                    HandlerUtils.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            userDetailView.onGetUserError(response.code() == 404 ? error.getErrorMessage() : activity.getString(R.string.data_load_faild_and_click_avatar_to_reload));
                            onFinish();
                        }

                    }, getPostTime());
                    return true;
                }

                @Override
                public boolean onCallException(Throwable t, Result.Error error) {
                    HandlerUtils.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            userDetailView.onGetUserError(activity.getString(R.string.data_load_faild_and_click_avatar_to_reload));
                            onFinish();
                        }

                    }, getPostTime());
                    return true;
                }

                @Override
                public void onFinish() {
                    userDetailView.onGetUserFinish();
                    loading = false;
                }

            });
            ApiClient.service.getCollectTopicList(loginName).enqueue(new DefaultCallbackAdapter<Result.Data<List<Topic>>>(activity) {

                @Override
                public boolean onResultOk(Response<Result.Data<List<Topic>>> response, Result.Data<List<Topic>> result) {
                    userDetailView.onGetCollectTopicListOk(result.getData());
                    return false;
                }

            });
        }
    }

}
