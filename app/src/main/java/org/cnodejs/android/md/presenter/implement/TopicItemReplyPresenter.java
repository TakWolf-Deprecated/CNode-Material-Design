package org.cnodejs.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.DefaultCallback;
import org.cnodejs.android.md.model.entity.Reply;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.presenter.contract.ITopicItemReplyPresenter;
import org.cnodejs.android.md.ui.view.ITopicItemReplyView;

import retrofit2.Response;

public class TopicItemReplyPresenter implements ITopicItemReplyPresenter {

    private final Activity activity;
    private final ITopicItemReplyView topicItemReplyView;

    public TopicItemReplyPresenter(@NonNull Activity activity, @NonNull ITopicItemReplyView topicItemReplyView) {
        this.activity = activity;
        this.topicItemReplyView = topicItemReplyView;
    }

    @Override
    public void upReplyAsyncTask(@NonNull final Reply reply) {
        ApiClient.service.upReply(reply.getId(), LoginShared.getAccessToken(activity)).enqueue(new DefaultCallback<Result.UpReply>(activity) {

            @Override
            public boolean onResultOk(Response<Result.UpReply> response, Result.UpReply result) {
                if (result.getAction() == Reply.UpAction.up) {
                    reply.getUpList().add(LoginShared.getId(activity));
                } else if (result.getAction() == Reply.UpAction.down) {
                    reply.getUpList().remove(LoginShared.getId(activity));
                }
                topicItemReplyView.onUpReplyOk(reply);
                return false;
            }

        });
    }

}
