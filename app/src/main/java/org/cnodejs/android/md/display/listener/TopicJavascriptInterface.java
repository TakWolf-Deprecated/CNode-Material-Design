package org.cnodejs.android.md.display.listener;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.webkit.JavascriptInterface;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.activity.LoginActivity;
import org.cnodejs.android.md.display.activity.UserDetailActivity;
import org.cnodejs.android.md.display.util.ToastUtils;
import org.cnodejs.android.md.display.view.ITopicReplyView;
import org.cnodejs.android.md.model.entity.Reply;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.model.util.EntityUtils;
import org.cnodejs.android.md.presenter.contract.ITopicHeaderPresenter;
import org.cnodejs.android.md.presenter.contract.ITopicItemReplyPresenter;
import org.cnodejs.android.md.util.HandlerUtils;

public final class TopicJavascriptInterface {

    public static final String NAME = "topicBridge";

    private final Activity activity;
    private final ITopicReplyView topicReplyView;
    private final ITopicHeaderPresenter topicHeaderPresenter;
    private final ITopicItemReplyPresenter topicItemReplyPresenter;

    public TopicJavascriptInterface(@NonNull Activity activity, @NonNull ITopicReplyView topicReplyView, @NonNull ITopicHeaderPresenter topicHeaderPresenter, @NonNull ITopicItemReplyPresenter topicItemReplyPresenter) {
        this.activity = activity;
        this.topicReplyView = topicReplyView;
        this.topicHeaderPresenter = topicHeaderPresenter;
        this.topicItemReplyPresenter = topicItemReplyPresenter;
    }

    @JavascriptInterface
    public void collectTopic(String topicId) {
        if (LoginActivity.startForResultWithAccessTokenCheck(activity)) {
            topicHeaderPresenter.collectTopicAsyncTask(topicId);
        }
    }

    @JavascriptInterface
    public void decollectTopic(String topicId) {
        if (LoginActivity.startForResultWithAccessTokenCheck(activity)) {
            topicHeaderPresenter.decollectTopicAsyncTask(topicId);
        }
    }

    @JavascriptInterface
    public void upReply(String replyJson) {
        if (LoginActivity.startForResultWithAccessTokenCheck(activity)) {
            Reply reply = EntityUtils.gson.fromJson(replyJson, Reply.class);
            if (reply.getAuthor().getLoginName().equals(LoginShared.getLoginName(activity))) {
                ToastUtils.with(activity).show(R.string.can_not_up_yourself_reply);
            } else {
                topicItemReplyPresenter.upReplyAsyncTask(reply);
            }
        }
    }

    @JavascriptInterface
    public void at(final String targetJson, final int targetPosition) {
        if (LoginActivity.startForResultWithAccessTokenCheck(activity)) {
            HandlerUtils.post(new Runnable() {

                @Override
                public void run() {
                    Reply target = EntityUtils.gson.fromJson(targetJson, Reply.class);
                    topicReplyView.onAt(target, targetPosition);
                }

            });
        }
    }

    @JavascriptInterface
    public void openUser(String loginName) {
        UserDetailActivity.start(activity, loginName);
    }

}
