package org.cnodejs.android.md.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import org.cnodejs.android.md.storage.SettingShared;

public final class ThemeUtils {

    private ThemeUtils() {}

    public static int getThemeAttrColor(@NonNull Context context, @AttrRes int attr) {
        TypedArray a = context.obtainStyledAttributes(null, new int[]{attr});
        try {
            return a.getColor(0, 0);
        } finally {
            a.recycle();
        }
    }

    public static void configThemeBeforeOnCreate(Activity activity, @StyleRes int light, @StyleRes int dark) {
        activity.setTheme(SettingShared.isEnableThemeDark(activity) ? dark : light);
    }

    public static void recreateActivity(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            activity.recreate();
        } else {
            activity.startActivity(new Intent(activity, activity.getClass()));
            activity.finish();
            activity.overridePendingTransition(0, 0);
        }
    }

}
