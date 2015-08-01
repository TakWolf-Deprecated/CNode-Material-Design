package org.cnodejs.android.md.util;

import android.os.Handler;

public final class HandlerUtils {

    private HandlerUtils() {}

    private static final Handler handler = new Handler();

    public static boolean post(Runnable r) {
        return handler.post(r);
    }

    public static boolean postDelayed(Runnable r, long delayMillis) {
        return handler.postDelayed(r, delayMillis);
    }

}
