package org.cnodejs.android.md.display.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import org.cnodejs.android.md.util.ResUtils;

public final class DisplayUtils {

    private DisplayUtils() {}

    public static void adaptStatusBar(@NonNull Context context, @NonNull View statusBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ResUtils.getStatusBarHeight(context);
            statusBar.setLayoutParams(layoutParams);
        }
    }

}
