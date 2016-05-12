package org.cnodejs.android.md.display.listener;

import android.content.Context;
import android.support.annotation.NonNull;
import android.webkit.JavascriptInterface;

import org.cnodejs.android.md.display.activity.UserDetailActivity;
import org.cnodejs.android.md.display.util.Navigator;

public final class NotificationJavascriptInterface {

    private volatile static NotificationJavascriptInterface singleton;

    public static NotificationJavascriptInterface with(@NonNull Context context) {
        if (singleton == null) {
            synchronized (NotificationJavascriptInterface.class) {
                if (singleton == null) {
                    singleton = new NotificationJavascriptInterface(context);
                }
            }
        }
        return singleton;
    }

    public static final String NAME = "notificationBridge";

    private final Context context;

    private NotificationJavascriptInterface(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    @JavascriptInterface
    public void openTopic(String topicId) {
        Navigator.TopicWithAutoCompat.start(context, topicId);
    }

    @JavascriptInterface
    public void openUser(String loginName) {
        UserDetailActivity.start(context, loginName);
    }

}
