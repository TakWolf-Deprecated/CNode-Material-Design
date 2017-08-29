package org.cnodejs.android.md.ui.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import org.cnodejs.android.md.model.storage.SettingShared;
import org.cnodejs.android.md.util.HandlerUtils;

public final class ThemeUtils {

    private ThemeUtils() {}

    public static boolean configThemeBeforeOnCreate(@NonNull Activity activity, @StyleRes int light, @StyleRes int dark) {
        boolean enable = SettingShared.isEnableThemeDark(activity);
        activity.setTheme(enable ? dark : light);
        return enable;
    }

    public static void notifyThemeApply(@NonNull final Activity activity) {
        HandlerUtils.handler.post(new Runnable() {

            @Override
            public void run() {
                activity.recreate();
            }

        });
    }

}
