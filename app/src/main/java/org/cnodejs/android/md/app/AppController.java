package org.cnodejs.android.md.app;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.umeng.analytics.MobclickAgent;

import net.danlew.android.joda.JodaTimeAndroid;

import org.cnodejs.android.md.BuildConfig;
import org.cnodejs.android.md.storage.LoginShared;

public class AppController extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        // 初始化JodaTimeAndroid
        JodaTimeAndroid.init(this);

        // 配置全局异常捕获
        if (!BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(new AppExceptionHandler(this));
        }

        // 友盟账号统计
        if (!TextUtils.isEmpty(LoginShared.getAccessToken(this))) {
            MobclickAgent.onProfileSignIn(LoginShared.getLoginName(this));
        } else {
            MobclickAgent.onProfileSignOff();
        }
    }

}
