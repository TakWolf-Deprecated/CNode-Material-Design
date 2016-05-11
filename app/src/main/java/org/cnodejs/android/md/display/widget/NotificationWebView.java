package org.cnodejs.android.md.display.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import org.cnodejs.android.md.display.listener.ImageJavascriptInterface;
import org.cnodejs.android.md.display.listener.NotificationJavascriptInterface;
import org.cnodejs.android.md.model.entity.Message;

import java.util.List;

public class NotificationWebView extends CNodeWebView {

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
        addJavascriptInterface(NotificationJavascriptInterface.with(context), NotificationJavascriptInterface.NAME);
    }

    public void updateMessageList(@NonNull List<Message> messageList) {

        // TODO
        loadUrl("http://blog.takwolf.com");

    }

    public void markAllMessageRead() {

        // TODO
        loadUrl("http://takwolf.com");

    }

}
