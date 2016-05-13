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
                "(function () {\n" +
                "    $('body').scrollTop(0);\n" +
                "})();"
        );
    }

    public void updateTopic(@NonNull TopicWithReply topic) {

        // TODO

    }

    public void updateTopicCollect(boolean isCollect) {

        // TODO

    }

    public void updateReply(@NonNull Reply reply) {

        // TODO

    }

    public void appendReply(@NonNull Reply reply) {

        // TODO

    }

}
