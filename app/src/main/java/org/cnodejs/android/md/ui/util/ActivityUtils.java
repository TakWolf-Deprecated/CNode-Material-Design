package org.cnodejs.android.md.ui.util;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.Nullable;

public final class ActivityUtils {

    private ActivityUtils() {}

    public static boolean isAlive(@Nullable Activity activity) {
        return activity != null && !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) && !activity.isFinishing();
    }

}
