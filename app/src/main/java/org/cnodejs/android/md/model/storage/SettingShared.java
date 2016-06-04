package org.cnodejs.android.md.model.storage;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class SettingShared {

    private SettingShared() {}

    private static final String TAG = "SettingShared";

    private static final String KEY_ENABLE_NOTIFICATION = "enableNotification";
    private static final String KEY_ENABLE_THEME_DARK = "enableThemeDark";
    private static final String KEY_ENABLE_TOPIC_DRAFT = "enableTopicDraft";
    private static final String KEY_ENABLE_TOPIC_SIGN = "enableTopicSign";
    private static final String KEY_TOPIC_SIGN_CONTENT = "topicSignContent";
    private static final String KEY_ENABLE_TOPIC_RENDER_COMPAT = "enableTopicRenderCompat";
    private static final String KEY_SHOW_TOPIC_RENDER_COMPAT_TIP = "showTopicRenderCompatTip";

    public static final String DEFAULT_TOPIC_SIGN_CONTENT = "来自酷炫的 [CNodeMD](https://github.com/TakWolf/CNode-Material-Design)";

    public static boolean isEnableNotification(@NonNull Context context) {
        return SharedWrapper.with(context, TAG).getBoolean(KEY_ENABLE_NOTIFICATION, true);
    }

    public static void setEnableNotification(@NonNull Context context, boolean enable) {
        SharedWrapper.with(context, TAG).setBoolean(KEY_ENABLE_NOTIFICATION, enable);
    }

    public static boolean isEnableThemeDark(@NonNull Context context) {
        return SharedWrapper.with(context, TAG).getBoolean(KEY_ENABLE_THEME_DARK, false);
    }

    public static void setEnableThemeDark(@NonNull Context context, boolean enable) {
        SharedWrapper.with(context, TAG).setBoolean(KEY_ENABLE_THEME_DARK, enable);
    }

    public static boolean isEnableTopicDraft(@NonNull Context context) {
        return SharedWrapper.with(context, TAG).getBoolean(KEY_ENABLE_TOPIC_DRAFT, true);
    }

    public static void setEnableTopicDraft(@NonNull Context context, boolean enable) {
        SharedWrapper.with(context, TAG).setBoolean(KEY_ENABLE_TOPIC_DRAFT, enable);
    }

    public static boolean isEnableTopicSign(@NonNull Context context) {
        return SharedWrapper.with(context, TAG).getBoolean(KEY_ENABLE_TOPIC_SIGN, true);
    }

    public static void setEnableTopicSign(@NonNull Context context, boolean enable) {
        SharedWrapper.with(context, TAG).setBoolean(KEY_ENABLE_TOPIC_SIGN, enable);
    }

    public static String getTopicSignContent(@NonNull Context context) {
        return SharedWrapper.with(context, TAG).getString(KEY_TOPIC_SIGN_CONTENT, DEFAULT_TOPIC_SIGN_CONTENT);
    }

    public static void setTopicSignContent(@NonNull Context context, @Nullable String content) {
        SharedWrapper.with(context, TAG).setString(KEY_TOPIC_SIGN_CONTENT, content);
    }

    public static boolean isEnableTopicRenderCompat(@NonNull Context context) {
        return SharedWrapper.with(context, TAG).getBoolean(KEY_ENABLE_TOPIC_RENDER_COMPAT, Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP);
    }

    public static void setEnableTopicRenderCompat(@NonNull Context context, boolean enable) {
        SharedWrapper.with(context, TAG).setBoolean(KEY_ENABLE_TOPIC_RENDER_COMPAT, enable);
    }

    /**
     * 检测是否真正启用兼容模式
     */
    public static boolean isReallyEnableTopicRenderCompat(@NonNull Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && isEnableTopicRenderCompat(context);
    }

    public static boolean isShowTopicRenderCompatTip(@NonNull Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && SharedWrapper.with(context, TAG).getBoolean(KEY_SHOW_TOPIC_RENDER_COMPAT_TIP, true);
    }

    public static void markShowTopicRenderCompatTip(@NonNull Context context) {
        SharedWrapper.with(context, TAG).setBoolean(KEY_SHOW_TOPIC_RENDER_COMPAT_TIP, false);
    }

}
