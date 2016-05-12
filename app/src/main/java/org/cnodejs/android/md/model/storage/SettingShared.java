package org.cnodejs.android.md.model.storage;

import android.content.Context;
import android.os.Build;

public final class SettingShared {

    private SettingShared() {}

    private static final String TAG = "SettingShared";

    private static final String KEY_ENABLE_NOTIFICATION = "enable_notification";
    private static final String KEY_ENABLE_THEME_DARK = "enable_theme_dark";
    private static final String KEY_ENABLE_TOPIC_DRAFT = "enable_topic_draft";
    private static final String KEY_ENABLE_TOPIC_SIGN = "enable_topic_sign";
    private static final String KEY_TOPIC_SIGN_CONTENT = "topic_sign_content";
    private static final String KEY_ENABLE_TOPIC_RENDER_COMPAT = "enable_topic_render_compat";

    public static final String DEFAULT_TOPIC_SIGN_CONTENT = "来自酷炫的 [CNodeMD](https://github.com/TakWolf/CNode-Material-Design)";

    public static boolean isEnableNotification(Context context) {
        return SharedWrapper.with(context, TAG).getBoolean(KEY_ENABLE_NOTIFICATION, true);
    }

    public static void setEnableNotification(Context context, boolean enable) {
        SharedWrapper.with(context, TAG).setBoolean(KEY_ENABLE_NOTIFICATION, enable);
    }

    public static boolean isEnableThemeDark(Context context) {
        return SharedWrapper.with(context, TAG).getBoolean(KEY_ENABLE_THEME_DARK, false);
    }

    public static void setEnableThemeDark(Context context, boolean enable) {
        SharedWrapper.with(context, TAG).setBoolean(KEY_ENABLE_THEME_DARK, enable);
    }

    public static boolean isEnableTopicDraft(Context context) {
        return SharedWrapper.with(context, TAG).getBoolean(KEY_ENABLE_TOPIC_DRAFT, true);
    }

    public static void setEnableTopicDraft(Context context, boolean enable) {
        SharedWrapper.with(context, TAG).setBoolean(KEY_ENABLE_TOPIC_DRAFT, enable);
    }

    public static boolean isEnableTopicSign(Context context) {
        return SharedWrapper.with(context, TAG).getBoolean(KEY_ENABLE_TOPIC_SIGN, true);
    }

    public static void setEnableTopicSign(Context context, boolean enable) {
        SharedWrapper.with(context, TAG).setBoolean(KEY_ENABLE_TOPIC_SIGN, enable);
    }

    public static String getTopicSignContent(Context context) {
        return SharedWrapper.with(context, TAG).getString(KEY_TOPIC_SIGN_CONTENT, DEFAULT_TOPIC_SIGN_CONTENT);
    }

    public static void setTopicSignContent(Context context, String content) {
        SharedWrapper.with(context, TAG).setString(KEY_TOPIC_SIGN_CONTENT, content);
    }

    public static boolean isEnableTopicRenderCompat(Context context) {
        return SharedWrapper.with(context, TAG).getBoolean(KEY_ENABLE_TOPIC_RENDER_COMPAT, Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP);
    }

    public static void setEnableTopicRenderCompat(Context context, boolean enable) {
        SharedWrapper.with(context, TAG).setBoolean(KEY_ENABLE_TOPIC_RENDER_COMPAT, enable);
    }

    /**
     * 检测是否真正启用兼容模式
     */
    public static boolean isReallyEnableTopicRenderCompat(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && isEnableTopicRenderCompat(context);
    }

}
