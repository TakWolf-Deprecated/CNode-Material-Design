package org.cnodejs.android.md.app;

import android.app.Application;
import android.content.Context;

import org.cnodejs.android.md.BuildConfig;

public class AppController extends Application {

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
                Thread.setDefaultUncaughtExceptionHandler(new AppExceptionHandler(this));
            }
        }
    }

}
