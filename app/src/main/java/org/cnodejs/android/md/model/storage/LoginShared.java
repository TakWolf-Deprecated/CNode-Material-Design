package org.cnodejs.android.md.model.storage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.User;

public final class LoginShared {

    private LoginShared() {}

    private static final String TAG = "LoginShared";

    private static final String KEY_ACCESS_TOKEN = "accessToken";
    private static final String KEY_ID = "id";
    private static final String KEY_LOGIN_NAME = "loginName";
    private static final String KEY_AVATAR_URL = "avatarUrl";
    private static final String KEY_SCORE = "score";

    private static String accessToken;
    private static String id;
    private static String loginName;
    private static String avatarUrl;
    private static Integer score;

    public static void login(@NonNull Context context, @NonNull String accessToken, @NonNull Result.Login loginInfo) {
        SharedWrapper sharedWrapper = SharedWrapper.with(context, TAG);
        sharedWrapper.setString(KEY_ACCESS_TOKEN, accessToken);
        sharedWrapper.setString(KEY_ID, loginInfo.getId());
        sharedWrapper.setString(KEY_LOGIN_NAME, loginInfo.getLoginName());
        sharedWrapper.setString(KEY_AVATAR_URL, loginInfo.getAvatarUrl());
        LoginShared.accessToken = accessToken;
        id = loginInfo.getId();
        loginName = loginInfo.getLoginName();
        avatarUrl = loginInfo.getAvatarUrl();
    }

    public static void update(@NonNull Context context, @NonNull User user) {
        SharedWrapper sharedWrapper = SharedWrapper.with(context, TAG);
        sharedWrapper.setString(KEY_LOGIN_NAME, user.getLoginName());
        sharedWrapper.setString(KEY_AVATAR_URL, user.getAvatarUrl());
        sharedWrapper.setInt(KEY_SCORE, user.getScore());
        loginName = user.getLoginName();
        avatarUrl = user.getAvatarUrl();
        score = user.getScore();
    }

    public static void logout(@NonNull Context context) {
        SharedWrapper.with(context, TAG).clear();
        accessToken = null;
        id = null;
        loginName = null;
        avatarUrl = null;
        score = null;
    }

    public static String getAccessToken(@NonNull Context context) {
        if (TextUtils.isEmpty(accessToken)) {
            accessToken = SharedWrapper.with(context, TAG).getString(KEY_ACCESS_TOKEN, null);
        }
        return accessToken;
    }

    public static String getId(@NonNull Context context) {
        if (TextUtils.isEmpty(id)) {
            id = SharedWrapper.with(context, TAG).getString(KEY_ID, null);
        }
        return id;
    }

    public static String getLoginName(@NonNull Context context) {
        if (TextUtils.isEmpty(loginName)) {
            loginName = SharedWrapper.with(context, TAG).getString(KEY_LOGIN_NAME, null);
        }
        return loginName;
    }

    public static String getAvatarUrl(@NonNull Context context) {
        if (TextUtils.isEmpty(avatarUrl)) {
            avatarUrl = SharedWrapper.with(context, TAG).getString(KEY_AVATAR_URL, null);
        }
        return avatarUrl;
    }

    public static int getScore(@NonNull Context context) {
        if (score == null) {
            score = SharedWrapper.with(context, TAG).getInt(KEY_SCORE, 0);
        }
        return score;
    }

}
