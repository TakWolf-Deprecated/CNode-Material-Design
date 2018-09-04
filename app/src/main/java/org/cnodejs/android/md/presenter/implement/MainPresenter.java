package org.cnodejs.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.ApiDefine;
import org.cnodejs.android.md.model.api.ForegroundCallback;
import org.cnodejs.android.md.model.api.ToastCallback;
import org.cnodejs.android.md.model.entity.DataResult;
import org.cnodejs.android.md.model.entity.ErrorResult;
import org.cnodejs.android.md.model.entity.Tab;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.User;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.presenter.contract.IMainPresenter;
import org.cnodejs.android.md.ui.view.IMainView;
import org.cnodejs.android.md.util.HandlerUtils;

import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;

public class MainPresenter implements IMainPresenter {

    private static final int PAGE_LIMIT = 20;

    private final Activity activity;
    private final IMainView mainView;

    private Tab tab = Tab.all;
    private Call<DataResult<List<Topic>>> refreshCall = null;
    private Call<DataResult<List<Topic>>> loadMoreCall = null;

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
    public void switchTab(@NonNull Tab tab) {
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
            final Call<DataResult<List<Topic>>> call = ApiClient.service.getTopicList(tab, 1, PAGE_LIMIT, ApiDefine.MD_RENDER);
            refreshCall = call;
            refreshCall.enqueue(new ToastCallback<DataResult<List<Topic>>>(activity) {

                @Override
                public boolean onResultOk(int code, Headers headers, DataResult<List<Topic>> result) {
                    handleTopicList(result.getData(), new OnHandleTopicListFinishListener() {

                        @Override
                        public void onHandleTopicListFinishListener(@NonNull List<Topic> topicList) {
                            if (call == refreshCall) {
                                cancelLoadMoreCall();
                                mainView.onRefreshTopicListOk(topicList);
                                onFinish();
                            }
                        }

                    });
                    return true;
                }

                @Override
                public boolean onAnyError(ErrorResult errorResult) {
                    mainView.onRefreshTopicListError();
                    return super.onAnyError(errorResult);
                }

                @Override
                public boolean onCallCancel() {
                    return true;
                }

                @Override
                public void onFinish() {
                    refreshCall = null;
                }

            });
        }
    }

    @Override
    public void loadMoreTopicListAsyncTask(int page) {
        if (loadMoreCall == null) {
            final Call<DataResult<List<Topic>>> call = ApiClient.service.getTopicList(tab, page, PAGE_LIMIT, ApiDefine.MD_RENDER);
            loadMoreCall = call;
            loadMoreCall.enqueue(new ToastCallback<DataResult<List<Topic>>>(activity) {

                @Override
                public boolean onResultOk(int code, Headers headers, DataResult<List<Topic>> result) {
                    handleTopicList(result.getData(), new OnHandleTopicListFinishListener() {

                        @Override
                        public void onHandleTopicListFinishListener(@NonNull List<Topic> topicList) {
                            if (call == loadMoreCall) {
                                mainView.onLoadMoreTopicListOk(topicList);
                                onFinish();
                            }
                        }

                    });
                    return true;
                }

                @Override
                public boolean onAnyError(ErrorResult errorResult) {
                    mainView.onLoadMoreTopicListError();
                    return super.onAnyError(errorResult);
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
            ApiClient.service.getUser(LoginShared.getLoginName(activity)).enqueue(new ForegroundCallback<DataResult<User>>(activity) {

                @Override
                public boolean onResultOk(int code, Headers headers, DataResult<User> result) {
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
            ApiClient.service.getMessageCount(accessToken).enqueue(new ForegroundCallback<DataResult<Integer>>(activity) {

                @Override
                public boolean onResultOk(int code, Headers headers, DataResult<Integer> result) {
                    if (TextUtils.equals(accessToken, LoginShared.getAccessToken(activity))) {
                        mainView.updateMessageCountViews(result.getData());
                    }
                    return false;
                }

            });
        }
    }

    private void handleTopicList(@NonNull final List<Topic> topicList, @NonNull final OnHandleTopicListFinishListener listener) {
        if (topicList.isEmpty()) {
            listener.onHandleTopicListFinishListener(topicList);
        } else {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    for (Topic topic : topicList) {
                        topic.markSureHandleContent();
                    }
                    HandlerUtils.handler.post(new Runnable() {

                        @Override
                        public void run() {
                            listener.onHandleTopicListFinishListener(topicList);
                        }

                    });
                }

            }).start();
        }
    }

    private interface OnHandleTopicListFinishListener {

        void onHandleTopicListFinishListener(@NonNull List<Topic> topicList);

    }

}
