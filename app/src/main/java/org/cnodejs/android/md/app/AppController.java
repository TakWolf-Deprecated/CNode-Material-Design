package org.cnodejs.android.md.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
        if (context == null) {
            context = this;

            if (!BuildConfig.DEBUG) {
                Thread.setDefaultUncaughtExceptionHandler(this);
            }
        }
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable e) {
        Intent intent = new Intent(this, CrashLogActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putSerializable("e", e);
        intent.putExtras(bundle);
        startActivity(intent);
        System.exit(1);
    }

}
