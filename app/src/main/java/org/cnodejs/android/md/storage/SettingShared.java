package org.cnodejs.android.md.storage;

import android.content.Context;

public final class SettingShared {

    private SettingShared() {}

    private static final String TAG = SettingShared.class.getSimpleName();

    private static final String KEY_ENABLE_NOTIFICATION = "notification";
    private static final String KEY_ENABLE_TOPIC_SIGN = "topic_sign";
    private static final String KEY_TOPIC_SIGN_CONTENT = "topic_sign_content";

    public static boolean isEnableNotification(Context context) {
        return SharedWrapper.with(context, TAG).getBoolean(KEY_ENABLE_NOTIFICATION, true);
    }

    public static void setEnableNotification(Context context, boolean enable) {
        SharedWrapper.with(context, TAG).setBoolean(KEY_ENABLE_NOTIFICATION, enable);
    }

    public static boolean isEnableTopicSign(Context context) {
        return SharedWrapper.with(context, TAG).getBoolean(KEY_ENABLE_TOPIC_SIGN, true);
    }

    public static void setEnableTopicSign(Context context, boolean enable) {
        SharedWrapper.with(context, TAG).setBoolean(KEY_ENABLE_TOPIC_SIGN, enable);
    }

}
