package org.cnodejs.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.ApiDefine;
import org.cnodejs.android.md.model.api.CallbackAdapter;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.TabType;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.User;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.presenter.contract.IMainPresenter;
import org.cnodejs.android.md.ui.view.IMainView;

import java.util.List;

import retrofit2.Response;

public class MainPresenter implements IMainPresenter {

    private final Activity activity;
    private final IMainView mainView;

    public MainPresenter(@NonNull Activity activity, @NonNull IMainView mainView) {
        this.activity = activity;
        this.mainView = mainView;
    }

    @Override
    public void refreshTopicListAsyncTask(@NonNull final TabType tab, @NonNull Integer limit) {
        ApiClient.service.getTopicList(tab, 1, limit, ApiDefine.MD_RENDER).enqueue(new CallbackAdapter<Result.Data<List<Topic>>>() {

            @Override
            public boolean onResultOk(Response<Result.Data<List<Topic>>> response, Result.Data<List<Topic>> result) {
                return mainView.onRefreshTopicListOk(tab, result.getData());
            }

            @Override
            public boolean onResultError(Response<Result.Data<List<Topic>>> response, Result.Error error) {
                return mainView.onRefreshTopicListError(tab, error.getErrorMessage());
            }

            @Override
            public boolean onCallException(Throwable t, Result.Error error) {
                return mainView.onRefreshTopicListError(tab, error.getErrorMessage());
            }

            @Override
            public void onFinish() {
                mainView.onRefreshTopicListFinish();
            }

        });
    }

    @Override
    public void loadMoreTopicListAsyncTask(@NonNull final TabType tab, @NonNull final Integer page, @NonNull Integer limit) {
        ApiClient.service.getTopicList(tab, page + 1, limit, ApiDefine.MD_RENDER).enqueue(new CallbackAdapter<Result.Data<List<Topic>>>() {

            @Override
            public boolean onResultOk(Response<Result.Data<List<Topic>>> response, Result.Data<List<Topic>> result) {
                return mainView.onLoadMoreTopicListOk(tab, page, result.getData());
            }

            @Override
            public boolean onResultError(Response<Result.Data<List<Topic>>> response, Result.Error error) {
                return mainView.onLoadMoreTopicListError(tab, page, error.getErrorMessage());
            }

            @Override
            public boolean onCallException(Throwable t, Result.Error error) {
                return mainView.onLoadMoreTopicListError(tab, page, error.getErrorMessage());
            }

            @Override
            public void onFinish() {
                mainView.onLoadMoreTopicListFinish();
            }

        });
    }

    @Override
    public void getUserAsyncTask() {
        final String accessToken = LoginShared.getAccessToken(activity);
        if (!TextUtils.isEmpty(accessToken)) {
            ApiClient.service.getUser(LoginShared.getLoginName(activity)).enqueue(new CallbackAdapter<Result.Data<User>>() {

                @Override
                public boolean onResultOk(Response<Result.Data<User>> response, Result.Data<User> result) {
                    if (TextUtils.equals(accessToken, LoginShared.getAccessToken(activity))) {
                        LoginShared.update(activity, result.getData());
                        mainView.updateUserInfoViews();
                    }
                    return false;
                }

            });
        }
    }

    @Override
    public void getMessageCountAsyncTask() {
        final String accessToken = LoginShared.getAccessToken(activity);
        if (!TextUtils.isEmpty(accessToken)) {
            ApiClient.service.getMessageCount(accessToken).enqueue(new CallbackAdapter<Result.Data<Integer>>() {

                @Override
                public boolean onResultOk(Response<Result.Data<Integer>> response, Result.Data<Integer> result) {
                    if (TextUtils.equals(accessToken, LoginShared.getAccessToken(activity))) {
                        mainView.updateMessageCountViews(result.getData());
                    }
                    return false;
                }

            });
        }
    }

}
