package org.cnodejs.android.md.app;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;

import net.danlew.android.joda.JodaTimeAndroid;

import org.cnodejs.android.md.BuildConfig;
import org.cnodejs.android.md.ui.activity.CrashLogActivity;

public class AppController extends Application implements Thread.UncaughtExceptionHandler {

    @Override
    public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);

        if (!BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(this);
        }

        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
        MobclickAgent.setCheckDevice(false);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable e) {
        CrashLogActivity.start(this, e);
        System.exit(1);
    }

}
