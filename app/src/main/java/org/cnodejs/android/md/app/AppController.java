package org.cnodejs.android.md.app;

import android.app.Application;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import net.danlew.android.joda.JodaTimeAndroid;

import org.cnodejs.android.md.BuildConfig;
import org.cnodejs.android.md.display.activity.CrashLogActivity;

public class AppController extends Application implements Thread.UncaughtExceptionHandler {

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
            Thread.setDefaultUncaughtExceptionHandler(this);
        }

        // 友盟设置调试模式
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable e) {
        CrashLogActivity.start(this, e);
        System.exit(1);
    }

}
