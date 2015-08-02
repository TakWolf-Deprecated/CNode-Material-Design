package org.cnodejs.android.md.storage;

import android.content.Context;
import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.LoginInfo;

public final class LoginShared {

    private LoginShared() {}

    private static final String TAG = LoginShared.class.getSimpleName();

    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_ID = "id";
    private static final String KEY_LOGIN_NAME = "login_name";
    private static final String KEY_AVATAR_URL = "avatar_url";

    public static void login(Context context, String accessToken, @NonNull LoginInfo loginInfo) {
        SharedWrapper.with(context, TAG).setString(KEY_ACCESS_TOKEN, accessToken);
        SharedWrapper.with(context, TAG).setString(KEY_ID, loginInfo.getId());
        SharedWrapper.with(context, TAG).setString(KEY_LOGIN_NAME, loginInfo.getLoginName());
        SharedWrapper.with(context, TAG).setString(KEY_AVATAR_URL, loginInfo.getAvatarUrl());
    }

    public static void logout(Context context) {
        SharedWrapper.with(context, TAG).clear();
    }

    public static String getAccessToken(Context context) {
        return SharedWrapper.with(context, TAG).getString(KEY_ACCESS_TOKEN, "");
    }

    public static String getId(Context context) {
        return SharedWrapper.with(context, TAG).getString(KEY_ID, null);
    }

    public static String getLoginName(Context context) {
        return SharedWrapper.with(context, TAG).getString(KEY_LOGIN_NAME, null);
    }

    public static String getAvatarUrl(Context context) {
        return SharedWrapper.with(context, TAG).getString(KEY_AVATAR_URL, null);
    }

}
