package org.cnodejs.android.md.app;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import org.cnodejs.android.md.activity.CrashLogActivity;

public class AppController extends Application implements Thread.UncaughtExceptionHandler {

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(this);
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
