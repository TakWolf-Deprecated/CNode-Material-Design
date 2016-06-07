package org.cnodejs.android.md.display.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import org.cnodejs.android.md.display.listener.FormatJavascriptInterface;
import org.cnodejs.android.md.display.listener.ImageJavascriptInterface;
import org.cnodejs.android.md.display.listener.NotificationJavascriptInterface;
import org.cnodejs.android.md.display.view.IBackToContentTopView;
import org.cnodejs.android.md.model.entity.Message;
import org.cnodejs.android.md.model.util.EntityUtils;

import java.util.List;

public class NotificationWebView extends CNodeWebView implements IBackToContentTopView {

    private static final String LIGHT_THEME_PATH = "file:///android_asset/notification_light.html";
    private static final String DARK_THEME_PATH = "file:///android_asset/notification_dark.html";

    private boolean pageLoaded = false;
    private List<Message> messageList = null;

    public NotificationWebView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public NotificationWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public NotificationWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NotificationWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint("AddJavascriptInterface")
    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        addJavascriptInterface(ImageJavascriptInterface.with(context), ImageJavascriptInterface.NAME);
        addJavascriptInterface(FormatJavascriptInterface.instance, FormatJavascriptInterface.NAME);
        addJavascriptInterface(NotificationJavascriptInterface.with(context), NotificationJavascriptInterface.NAME);
        loadUrl(isDarkTheme() ? DARK_THEME_PATH : LIGHT_THEME_PATH);
    }

    @Override
    protected void onPageFinished(String url) {
        pageLoaded = true;
        if (messageList != null) {
            updateMessageList(messageList);
            messageList = null;
        }
    }

    public void updateMessageList(@NonNull List<Message> messageList) {
        if (pageLoaded) {
            for (Message message : messageList) {
                message.getReply().getContentHtml(); // 确保Html渲染
            }
            loadUrl("" +
                    "javascript:\n" +
                    "updateMessages(" + EntityUtils.gson.toJson(messageList) + ");"
            );
        } else {
            this.messageList = messageList;
        }
    }

    public void markAllMessageRead() {
        if (pageLoaded) {
            loadUrl("" +
                    "javascript:\n" +
                    "markAllMessageRead();"
            );
        }
    }

    @Override
    public void backToContentTop() {
        scrollTo(0, 0);
    }

}
