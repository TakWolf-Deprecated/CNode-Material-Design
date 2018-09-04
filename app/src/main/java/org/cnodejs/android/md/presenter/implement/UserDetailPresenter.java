package org.cnodejs.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.ForegroundCallback;
import org.cnodejs.android.md.model.entity.DataResult;
import org.cnodejs.android.md.model.entity.ErrorResult;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.User;
import org.cnodejs.android.md.presenter.contract.IUserDetailPresenter;
import org.cnodejs.android.md.ui.util.ActivityUtils;
import org.cnodejs.android.md.ui.view.IUserDetailView;
import org.cnodejs.android.md.util.HandlerUtils;

import java.util.List;

import okhttp3.Headers;

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
            ApiClient.service.getUser(loginName).enqueue(new ForegroundCallback<DataResult<User>>(activity) {

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
                public boolean onResultOk(int code, Headers headers, final DataResult<User> result) {
                    HandlerUtils.handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (ActivityUtils.isAlive(activity)) {
                                userDetailView.onGetUserOk(result.getData());
                                onFinish();
                            }
                        }

                    }, getPostTime());
                    return true;
                }

                @Override
                public boolean onResultError(final int code, Headers headers, final ErrorResult errorResult) {
                    HandlerUtils.handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (ActivityUtils.isAlive(activity)) {
                                userDetailView.onGetUserError(code == 404 ? errorResult.getMessage() : activity.getString(R.string.data_load_faild_and_click_avatar_to_reload));
                                onFinish();
                            }
                        }

                    }, getPostTime());
                    return true;
                }

                @Override
                public boolean onCallException(Throwable t, ErrorResult errorResult) {
                    HandlerUtils.handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (ActivityUtils.isAlive(activity)) {
                                userDetailView.onGetUserError(activity.getString(R.string.data_load_faild_and_click_avatar_to_reload));
                                onFinish();
                            }
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
            ApiClient.service.getCollectTopicList(loginName).enqueue(new ForegroundCallback<DataResult<List<Topic>>>(activity) {

                @Override
                public boolean onResultOk(int code, Headers headers, DataResult<List<Topic>> result) {
                    userDetailView.onGetCollectTopicListOk(result.getData());
                    return false;
                }

            });
        }
    }

}
