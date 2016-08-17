package org.cnodejs.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.DefaultCallback;
import org.cnodejs.android.md.model.entity.Reply;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.presenter.contract.IReplyPresenter;
import org.cnodejs.android.md.ui.view.IReplyView;

import okhttp3.Headers;

public class ReplyPresenter implements IReplyPresenter {

    private final Activity activity;
    private final IReplyView replyView;

    public ReplyPresenter(@NonNull Activity activity, @NonNull IReplyView replyView) {
        this.activity = activity;
        this.replyView = replyView;
    }

    @Override
    public void upReplyAsyncTask(@NonNull final Reply reply) {
        ApiClient.service.upReply(reply.getId(), LoginShared.getAccessToken(activity)).enqueue(new DefaultCallback<Result.UpReply>(activity) {

            @Override
            public boolean onResultOk(int code, Headers headers, Result.UpReply result) {
                if (result.getAction() == Reply.UpAction.up) {
                    reply.getUpList().add(LoginShared.getId(getActivity()));
                } else if (result.getAction() == Reply.UpAction.down) {
                    reply.getUpList().remove(LoginShared.getId(getActivity()));
                }
                replyView.onUpReplyOk(reply);
                return false;
            }

        });
    }

}
