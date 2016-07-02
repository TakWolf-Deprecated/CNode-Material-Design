package org.cnodejs.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.ApiDefine;
import org.cnodejs.android.md.model.api.ForegroundCallback;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.TabType;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.User;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.presenter.contract.IMainPresenter;
import org.cnodejs.android.md.ui.util.ToastUtils;
import org.cnodejs.android.md.ui.view.IMainView;
import org.cnodejs.android.md.ui.viewholder.LoadMoreFooter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MainPresenter implements IMainPresenter {

    private static final int PAGE_LIMIT = 20;

    private final Activity activity;
    private final IMainView mainView;

    private TabType tab = TabType.all;
    private Call<Result.Data<List<Topic>>> refreshCall = null;
    private Call<Result.Data<List<Topic>>> loadMoreCall = null;

    public MainPresenter(@NonNull Activity activity, @NonNull IMainView mainView) {
        this.activity = activity;
        this.mainView = mainView;
    }

    private void cancelRefreshCall() {
        if (refreshCall != null) {
            if (!refreshCall.isCanceled()) {
                refreshCall.cancel();
            }
            refreshCall = null;
        }
    }

    private void cancelLoadMoreCall() {
        if (loadMoreCall != null) {
            if (!loadMoreCall.isCanceled()) {
                loadMoreCall.cancel();
            }
            loadMoreCall = null;
        }
    }

    @Override
    public void switchTab(@NonNull TabType tab) {
        if (this.tab != tab) {
            this.tab = tab;
            cancelRefreshCall();
            cancelLoadMoreCall();
            mainView.onSwitchTabOk(tab);
        }
    }

    @Override
    public void refreshTopicListAsyncTask() {
        if (refreshCall == null) {
            refreshCall = ApiClient.service.getTopicList(tab, 1, PAGE_LIMIT, ApiDefine.MD_RENDER);
            refreshCall.enqueue(new ForegroundCallback<Result.Data<List<Topic>>>(activity) {

                @Override
                public boolean onResultOk(Response<Result.Data<List<Topic>>> response, Result.Data<List<Topic>> result) {
                    cancelLoadMoreCall();
                    if (result.getData().size() > 0) {
                        mainView.onRefreshTopicListOk(result.getData());
                    }
                    return false;
                }

                @Override
                public boolean onResultError(Response<Result.Data<List<Topic>>> response, Result.Error error) {
                    ToastUtils.with(getActivity()).show(error.getErrorMessage());
                    return false;
                }

                @Override
                public boolean onCallException(Throwable t, Result.Error error) {
                    ToastUtils.with(getActivity()).show(error.getErrorMessage());
                    return false;
                }

                @Override
                public boolean onCallCancel() {
                    return true;
                }

                @Override
                public void onFinish() {
                    refreshCall = null;
                    mainView.onRefreshTopicListFinish();
                }

            });
        }
    }

    @Override
    public void loadMoreTopicListAsyncTask(int page) {
        if (loadMoreCall == null) {
            loadMoreCall = ApiClient.service.getTopicList(tab, page, PAGE_LIMIT, ApiDefine.MD_RENDER);
            loadMoreCall.enqueue(new ForegroundCallback<Result.Data<List<Topic>>>(activity) {

                @Override
                public boolean onResultOk(Response<Result.Data<List<Topic>>> response, Result.Data<List<Topic>> result) {
                    if (result.getData().size() > 0) {
                        mainView.onLoadMoreTopicListOk(result.getData());
                        mainView.onLoadMoreTopicListFinish(LoadMoreFooter.State.endless);
                    } else {
                        mainView.onLoadMoreTopicListFinish(LoadMoreFooter.State.nomore);
                    }
                    return false;
                }

                @Override
                public boolean onResultError(Response<Result.Data<List<Topic>>> response, Result.Error error) {
                    mainView.onLoadMoreTopicListFinish(LoadMoreFooter.State.fail);
                    ToastUtils.with(getActivity()).show(error.getErrorMessage());
                    return false;
                }

                @Override
                public boolean onCallException(Throwable t, Result.Error error) {
                    mainView.onLoadMoreTopicListFinish(LoadMoreFooter.State.fail);
                    ToastUtils.with(getActivity()).show(error.getErrorMessage());
                    return false;
                }

                @Override
                public boolean onCallCancel() {
                    return true;
                }

                @Override
                public void onFinish() {
                    loadMoreCall = null;
                }

            });
        }
    }

    @Override
    public void getUserAsyncTask() {
        final String accessToken = LoginShared.getAccessToken(activity);
        if (!TextUtils.isEmpty(accessToken)) {
            ApiClient.service.getUser(LoginShared.getLoginName(activity)).enqueue(new ForegroundCallback<Result.Data<User>>(activity) {

                @Override
                public boolean onResultOk(Response<Result.Data<User>> response, Result.Data<User> result) {
                    if (TextUtils.equals(accessToken, LoginShared.getAccessToken(getActivity()))) {
                        LoginShared.update(getActivity(), result.getData());
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
            ApiClient.service.getMessageCount(accessToken).enqueue(new ForegroundCallback<Result.Data<Integer>>(activity) {

                @Override
                public boolean onResultOk(Response<Result.Data<Integer>> response, Result.Data<Integer> result) {
                    if (TextUtils.equals(accessToken, LoginShared.getAccessToken(getActivity()))) {
                        mainView.updateMessageCountViews(result.getData());
                    }
                    return false;
                }

            });
        }
    }

}
