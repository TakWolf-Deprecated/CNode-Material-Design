package org.cnodejs.android.md.util;

import android.content.Context;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateConfig;

import org.cnodejs.android.md.BuildConfig;

public final class UpdateUtils {

    public static final boolean FOR_GOOGLE_PLAY = false; // GooglePlay政策不允许应用存在内更新

    static {
        UpdateConfig.setDebug(BuildConfig.DEBUG);
    }

    private UpdateUtils() {}

    public static void update(Context context) {
        UmengUpdateAgent.update(context);
    }

    public static void forceUpdate(Context context) {
        UmengUpdateAgent.forceUpdate(context);
    }

}
