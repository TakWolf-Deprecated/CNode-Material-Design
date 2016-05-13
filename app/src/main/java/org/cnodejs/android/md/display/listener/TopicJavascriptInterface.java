package org.cnodejs.android.md.display.listener;

import android.content.Context;
import android.support.annotation.NonNull;
import android.webkit.JavascriptInterface;

import com.melnykov.fab.FloatingActionButton;

import org.cnodejs.android.md.display.activity.UserDetailActivity;
import org.cnodejs.android.md.display.view.ITopicReplyView;
import org.cnodejs.android.md.model.entity.Reply;
import org.cnodejs.android.md.model.util.EntityUtils;
import org.cnodejs.android.md.presenter.contract.ITopicHeaderPresenter;
import org.cnodejs.android.md.presenter.contract.ITopicItemReplyPresenter;

public final class TopicJavascriptInterface {

    public static final String NAME = "topicBridge";

    private final Context context;
    private final FloatingActionButton fabReply;
    private final ITopicReplyView topicReplyView;
    private final ITopicHeaderPresenter topicHeaderPresenter;
    private final ITopicItemReplyPresenter topicItemReplyPresenter;

    public TopicJavascriptInterface(@NonNull Context context, @NonNull FloatingActionButton fabReply, @NonNull ITopicReplyView topicReplyView, @NonNull ITopicHeaderPresenter topicHeaderPresenter, @NonNull ITopicItemReplyPresenter topicItemReplyPresenter) {
        this.context = context;
        this.fabReply = fabReply;
        this.topicReplyView = topicReplyView;
        this.topicHeaderPresenter = topicHeaderPresenter;
        this.topicItemReplyPresenter = topicItemReplyPresenter;
    }

    @JavascriptInterface
    public void onScrollUp() {
        fabReply.show(true);
    }

    @JavascriptInterface
    public void onScrollDown() {
        fabReply.hide(true);
    }

    @JavascriptInterface
    public void collectTopic(String topicId) {
        topicHeaderPresenter.collectTopicAsyncTask(topicId);
    }

    @JavascriptInterface
    public void decollectTopic(String topicId) {
        topicHeaderPresenter.decollectTopicAsyncTask(topicId);
    }

    @JavascriptInterface
    public void upReply(String replyJson) {
        Reply reply = EntityUtils.gson.fromJson(replyJson, Reply.class);
        topicItemReplyPresenter.upReplyAsyncTask(reply);
    }

    @JavascriptInterface
    public void at(String targetJson, int targetPosition) {
        Reply target = EntityUtils.gson.fromJson(targetJson, Reply.class);
        topicReplyView.onAt(target, targetPosition);
    }

    @JavascriptInterface
    public void openUser(String loginName) {
        UserDetailActivity.start(context, loginName);
    }

}
