package org.cnodejs.android.md.display.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import org.cnodejs.android.md.display.listener.ImageJavascriptInterface;
import org.cnodejs.android.md.display.listener.TopicJavascriptInterface;
import org.cnodejs.android.md.display.view.IBackToContentTopView;
import org.cnodejs.android.md.model.entity.Reply;
import org.cnodejs.android.md.model.entity.TopicWithReply;
import org.cnodejs.android.md.model.util.EntityUtils;

public class TopicWebView extends CNodeWebView implements IBackToContentTopView {

    private static final String LIGHT_THEME_PATH = "file:///android_asset/topic_light.html";
    private static final String DARK_THEME_PATH = "file:///android_asset/topic_dark.html";

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

    @SuppressLint("AddJavascriptInterface")
    public void setBridgeAndLoadPage(@NonNull TopicJavascriptInterface topicBridge) {
        addJavascriptInterface(ImageJavascriptInterface.with(getContext()), ImageJavascriptInterface.NAME);
        addJavascriptInterface(topicBridge, TopicJavascriptInterface.NAME);
        loadUrl(isDarkTheme() ? DARK_THEME_PATH : LIGHT_THEME_PATH);
    }

    @Override
    public void backToContentTop() {
        loadUrl("" +
                "javascript:\n" +
                "$('body').scrollTop(0);"
        );
    }

    public void updateTopicAndUserId(@NonNull TopicWithReply topic, String userId) {
        topic.getRenderedContent(); // 确保Html渲染
        for (Reply reply : topic.getReplyList()) {
            reply.getRenderedContent();
        }
        loadUrl("" +
                "javascript:\n" +
                "updateTopicAndUserId(" + EntityUtils.gson.toJson(topic) + ", '" + userId + "');"
        );
    }

    public void updateTopicCollect(boolean isCollect) {
        loadUrl("" +
                "javascript:\n" +
                "updateTopicCollect(" + isCollect + ");"
        );
    }

    public void updateReply(@NonNull Reply reply) {
        reply.getRenderedContent(); // 确保Html渲染
        loadUrl("" +
                "javascript:\n" +
                "updateReply(" + EntityUtils.gson.toJson(reply) + ");"
        );
    }

    public void appendReply(@NonNull Reply reply) {
        reply.getRenderedContent(); // 确保Html渲染
        loadUrl("" +
                "javascript:\n" +
                "appendReply(" + EntityUtils.gson.toJson(reply) + ");\n" +
                "$('body').animate({scrollTop: $('body')[0].scrollHeight}, 400);"
        );
    }

}
