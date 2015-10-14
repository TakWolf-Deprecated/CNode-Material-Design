package org.cnodejs.android.md.app;

import android.app.Application;
import android.content.Context;

import com.umeng.update.UpdateConfig;

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

            // 配置全局异常捕获
            if (!BuildConfig.DEBUG) {
                Thread.setDefaultUncaughtExceptionHandler(new AppExceptionHandler(this));
            }

            // 配置友盟更新日志
            UpdateConfig.setDebug(BuildConfig.DEBUG);
        }
    }

}
