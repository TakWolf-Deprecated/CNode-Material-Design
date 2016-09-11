package org.cnodejs.android.md.app;

import android.app.Application;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import net.danlew.android.joda.JodaTimeAndroid;

import org.cnodejs.android.md.BuildConfig;
import org.cnodejs.android.md.ui.activity.CrashLogActivity;

public class AppController extends Application implements Thread.UncaughtExceptionHandler {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        JodaTimeAndroid.init(this);

        if (!BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(this);
        }

        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable e) {
        CrashLogActivity.start(this, e);
        System.exit(1);
    }

}
