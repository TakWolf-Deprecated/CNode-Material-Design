package org.cnodejs.android.md.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.cnodejs.android.md.ui.activity.CrashLogActivity;

public class AppExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Context context;

    public AppExceptionHandler(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable e) {
        Intent intent = new Intent(context, CrashLogActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putSerializable("e", e);
        intent.putExtras(bundle);
        context.startActivity(intent);
        System.exit(1);
    }

}
