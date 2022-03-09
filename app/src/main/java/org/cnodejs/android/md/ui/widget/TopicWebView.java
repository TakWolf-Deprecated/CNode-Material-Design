package org.cnodejs.android.md.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import org.cnodejs.android.md.model.entity.TopicDetail;
import org.cnodejs.android.md.util.JsonUtils;

public class TopicWebView extends CNodeWebView {
    private static final String HTML_PATH = "file:///android_asset/topic.html";

    @Nullable private String accountId;
    @Nullable private TopicDetail topicDetail;

    public TopicWebView(@NonNull Context context) {
        super(context);
        init();
    }

    public TopicWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TopicWebView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TopicWebView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        loadUrl(HTML_PATH);
    }

    private void callJsUpdateAccountId(@Nullable String accountId) {
        String json = JsonUtils.INSTANCE.getMoshi().adapter(String.class).toJson(accountId);
        String script = "updateAccountId(" + json + ")";
        evaluateJavascript(script, null);
    }

    public void updateAccountId(@Nullable String accountId) {
        if (isJavascriptReady()) {
            callJsUpdateAccountId(accountId);
        } else {
            this.accountId = accountId;
        }
    }

    private void callJsUpdateTopic(@NonNull TopicDetail topicDetail) {
        String json = JsonUtils.INSTANCE.getMoshi().adapter(TopicDetail.class).toJson(topicDetail);
        String script = "updateTopic(" + json + ")";
        evaluateJavascript(script, null);
    }

    public void updateTopic(@NonNull TopicDetail topicDetail) {
        if (isJavascriptReady()) {
            callJsUpdateTopic(topicDetail);
        } else {
            this.topicDetail = topicDetail;
        }
    }

    @Override
    protected void onJavascriptReady() {
        if (accountId != null) {
            callJsUpdateAccountId(accountId);
            accountId = null;
        }
        if (topicDetail != null) {
            callJsUpdateTopic(topicDetail);
            topicDetail = null;
        }
    }
}
