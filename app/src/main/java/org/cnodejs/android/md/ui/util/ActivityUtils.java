package org.cnodejs.android.md.ui.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;

import org.cnodejs.android.md.util.HandlerUtils;

public final class ActivityUtils {

    private ActivityUtils() {}

    public static boolean isAlive(Activity activity) {
        return activity != null && !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) && !activity.isFinishing();
    }

    public static void recreateDelayed(@NonNull final Activity activity) {
        HandlerUtils.handler.post(new Runnable() {

            @Override
            public void run() {
                activity.recreate();
            }

        });
    }

}
