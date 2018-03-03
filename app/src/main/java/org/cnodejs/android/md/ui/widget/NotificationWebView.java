package org.cnodejs.android.md.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;

import org.cnodejs.android.md.model.entity.Message;
import org.cnodejs.android.md.model.util.EntityUtils;
import org.cnodejs.android.md.ui.jsbridge.FormatJavascriptInterface;
import org.cnodejs.android.md.ui.jsbridge.ImageJavascriptInterface;
import org.cnodejs.android.md.ui.jsbridge.NotificationJavascriptInterface;
import org.cnodejs.android.md.ui.view.IBackToContentTopView;

import java.util.List;

public class NotificationWebView extends CNodeWebView implements IBackToContentTopView {

    private static final String LIGHT_THEME_PATH = "file:///android_asset/notification_light.html";
    private static final String DARK_THEME_PATH = "file:///android_asset/notification_dark.html";

    private boolean pageLoaded = false;
    private List<Message> messageList = null;

    public NotificationWebView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public NotificationWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NotificationWebView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NotificationWebView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @SuppressLint("AddJavascriptInterface")
    private void init(@NonNull Context context) {
        addJavascriptInterface(new ImageJavascriptInterface(context), ImageJavascriptInterface.NAME);
        addJavascriptInterface(new FormatJavascriptInterface(), FormatJavascriptInterface.NAME);
        addJavascriptInterface(new NotificationJavascriptInterface(context), NotificationJavascriptInterface.NAME);
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
