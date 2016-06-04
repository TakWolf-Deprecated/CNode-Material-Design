package org.cnodejs.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import org.cnodejs.android.md.display.view.ITopicView;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.ApiDefine;
import org.cnodejs.android.md.model.api.DefaultToastCallback;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.TopicWithReply;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.presenter.contract.ITopicPresenter;

import retrofit2.Call;
import retrofit2.Response;

public class TopicPresenter implements ITopicPresenter {

    private final Activity activity;
    private final ITopicView topicView;

    public TopicPresenter(@NonNull Activity activity, @NonNull ITopicView topicView) {
        this.activity = activity;
        this.topicView = topicView;
    }

    @Override
    public void getTopicAsyncTask(@NonNull String topicId) {
        Call<Result.Data<TopicWithReply>> call = ApiClient.service.getTopic(topicId, LoginShared.getAccessToken(activity), ApiDefine.MD_RENDER);
        call.enqueue(new DefaultToastCallback<Result.Data<TopicWithReply>>(activity) {

            @Override
            public boolean onResultOk(Response<Result.Data<TopicWithReply>> response, Result.Data<TopicWithReply> result) {
                return topicView.onGetTopicResultOk(result);
            }

            @Override
            public void onFinish() {
                topicView.onGetTopicFinish();
            }

        });
    }

}
