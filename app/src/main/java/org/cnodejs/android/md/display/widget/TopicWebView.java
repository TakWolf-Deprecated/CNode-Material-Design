package org.cnodejs.android.md.display.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.melnykov.fab.FloatingActionButton;

import org.cnodejs.android.md.display.listener.FormatJavascriptInterface;
import org.cnodejs.android.md.display.listener.ImageJavascriptInterface;
import org.cnodejs.android.md.display.listener.TopicJavascriptInterface;
import org.cnodejs.android.md.display.view.IBackToContentTopView;
import org.cnodejs.android.md.model.entity.Reply;
import org.cnodejs.android.md.model.entity.TopicWithReply;
import org.cnodejs.android.md.model.util.EntityUtils;

public class TopicWebView extends CNodeWebView implements IBackToContentTopView {

    private static final String LIGHT_THEME_PATH = "file:///android_asset/topic_light.html";
    private static final String DARK_THEME_PATH = "file:///android_asset/topic_dark.html";

    private FloatingActionButton fabReply;

    private boolean pageLoaded = false;
    private TopicWithReply topic = null;
    private String userId = null;

    public TopicWebView(Context context) {
        super(context);
    }

    public TopicWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopicWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TopicWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setFabReply(FloatingActionButton fabReply) {
        this.fabReply = fabReply;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (fabReply != null) {
            if (t - oldt > 4) {
                fabReply.hide(true);
            } else if (t - oldt < -4) {
                fabReply.show(true);
            }
        }
    }

    @SuppressLint("AddJavascriptInterface")
    public void setBridgeAndLoadPage(@NonNull TopicJavascriptInterface topicBridge) {
        addJavascriptInterface(ImageJavascriptInterface.with(getContext()), ImageJavascriptInterface.NAME);
        addJavascriptInterface(FormatJavascriptInterface.instance, FormatJavascriptInterface.NAME);
        addJavascriptInterface(topicBridge, TopicJavascriptInterface.NAME);
        loadUrl(isDarkTheme() ? DARK_THEME_PATH : LIGHT_THEME_PATH);
    }

    @Override
    protected void onPageFinished(String url) {
        pageLoaded = true;
        if (topic != null) {
            updateTopicAndUserId(topic, userId);
            topic = null;
            userId = null;
        }
    }

    public void updateTopicAndUserId(@NonNull TopicWithReply topic, String userId) {
        if (pageLoaded) {
            topic.getContentHtml(); // 确保Html渲染
            for (Reply reply : topic.getReplyList()) {
                reply.getContentHtml(); // 确保Html渲染
            }
            loadUrl("" +
                    "javascript:\n" +
                    "updateTopicAndUserId(" + EntityUtils.gson.toJson(topic) + ", '" + userId + "');"
            );
        } else {
            this.topic = topic;
            this.userId = userId;
        }
    }

    public void updateTopicCollect(boolean isCollect) {
        if (pageLoaded) {
            loadUrl("" +
                    "javascript:\n" +
                    "updateTopicCollect(" + isCollect + ");"
            );
        }
    }

    public void updateReply(@NonNull Reply reply) {
        if (pageLoaded) {
            reply.getContentHtml(); // 确保Html渲染
            loadUrl("" +
                    "javascript:\n" +
                    "updateReply(" + EntityUtils.gson.toJson(reply) + ");"
            );
        }
    }

    public void appendReply(@NonNull Reply reply) {
        if (pageLoaded) {
            reply.getContentHtml(); // 确保Html渲染
            loadUrl("" +
                    "javascript:\n" +
                    "appendReply(" + EntityUtils.gson.toJson(reply) + ");\n" +
                    "setTimeout(function () {\n" +
                    "    window.scrollTo(0, document.body.clientHeight);\n" +
                    "}, 100);"
            );
        }
    }

    @Override
    public void backToContentTop() {
        scrollTo(0, 0);
    }

}
