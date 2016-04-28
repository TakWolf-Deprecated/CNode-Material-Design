package org.cnodejs.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import org.cnodejs.android.md.display.view.ITopicHeaderView;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.DefaultToastCallback;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.presenter.contract.ITopicHeaderPresenter;

import retrofit2.Call;
import retrofit2.Response;

public class TopicHeaderPresenter implements ITopicHeaderPresenter {

    private final Activity activity;
    private final ITopicHeaderView topicHeaderView;

    public TopicHeaderPresenter(@NonNull Activity activity, @NonNull ITopicHeaderView topicHeaderView) {
        this.activity = activity;
        this.topicHeaderView = topicHeaderView;
    }

    @Override
    public void collectTopicAsyncTask(@NonNull String topicId) {
        Call<Result> call = ApiClient.service.collectTopic(LoginShared.getAccessToken(activity), topicId);
        call.enqueue(new DefaultToastCallback<Result>(activity) {

            @Override
            public boolean onResultOk(Response<Result> response, Result result) {
                return topicHeaderView.onCollectTopicResultOk(result);
            }

        });
    }

    @Override
    public void decollectTopicAsyncTask(@NonNull String topicId) {
        Call<Result> call = ApiClient.service.decollectTopic(LoginShared.getAccessToken(activity), topicId);
        call.enqueue(new DefaultToastCallback<Result>(activity) {

            @Override
            public boolean onResultOk(Response<Result> response, Result result) {
                return topicHeaderView.onDecollectTopicResultOk(result);
            }

        });
    }

}
