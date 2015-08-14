package org.cnodejs.android.md.storage;

import android.content.Context;

public final class SettingShared {

    private SettingShared() {}

    private static final String TAG = SettingShared.class.getSimpleName();

    private static final String KEY_ENABLE_NOTIFICATION = "notification";
    private static final String KEY_NEW_TOPIC_DRAFT = "new_topic_draft";
    private static final String KEY_ENABLE_TOPIC_SIGN = "topic_sign";
    private static final String KEY_TOPIC_SIGN_CONTENT = "topic_sign_content";
    private static final String KEY_ENABLE_THIRD_PARTY_IMAGE_UPLOAD_API = "third_party_image_upload_api";

    public static final String DEFAULT_TOPIC_SIGN_CONTENT = "来自炫酷的 [CNodeMD](https://github.com/TakWolf/CNode-Material-Design)";

    public static boolean isEnableNotification(Context context) {
        return SharedWrapper.with(context, TAG).getBoolean(KEY_ENABLE_NOTIFICATION, true);
    }

    public static void setEnableNotification(Context context, boolean enable) {
        SharedWrapper.with(context, TAG).setBoolean(KEY_ENABLE_NOTIFICATION, enable);
    }

    public static boolean isEnableNewTopicDraft(Context context) {
        return SharedWrapper.with(context, TAG).getBoolean(KEY_NEW_TOPIC_DRAFT, true);
    }

    public static void setEnableNewTopicDraft(Context context, boolean enable) {
        SharedWrapper.with(context, TAG).setBoolean(KEY_NEW_TOPIC_DRAFT, enable);
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

    public static boolean isEnableThirdPartyImageUploadApi(Context context) {
        return SharedWrapper.with(context, TAG).getBoolean(KEY_ENABLE_THIRD_PARTY_IMAGE_UPLOAD_API, true);
    }

    public static void setEnableThirdPartyImageUploadApi(Context context, boolean enable) {
        SharedWrapper.with(context, TAG).setBoolean(KEY_ENABLE_THIRD_PARTY_IMAGE_UPLOAD_API, enable);
    }

}
