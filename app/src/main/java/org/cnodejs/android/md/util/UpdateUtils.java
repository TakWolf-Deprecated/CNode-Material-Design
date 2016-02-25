package org.cnodejs.android.md.util;

import android.content.Context;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateConfig;

import org.cnodejs.android.md.BuildConfig;
import org.cnodejs.android.md.ui.widget.ToastUtils;

public final class UpdateUtils {

    private static final boolean FOR_GOOGLE_PLAY = false; // GooglePlay政策不允许应用存在内更新

    static {
        if (!FOR_GOOGLE_PLAY) {
            UpdateConfig.setDebug(BuildConfig.DEBUG);
        }
    }

    private UpdateUtils() {}

    public static void update(Context context) {
        if (!FOR_GOOGLE_PLAY) {
            UmengUpdateAgent.update(context);
        }
    }

    public static void forceUpdate(Context context) {
        if (!FOR_GOOGLE_PLAY) {
            UmengUpdateAgent.forceUpdate(context);
        } else {
            ToastUtils.with(context).show("Google Play");
        }
    }

}
