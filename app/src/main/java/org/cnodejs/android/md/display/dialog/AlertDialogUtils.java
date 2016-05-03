package org.cnodejs.android.md.display.dialog;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.storage.SettingShared;

public final class AlertDialogUtils {

    private AlertDialogUtils() {}

    public static AlertDialog.Builder createBuilderWithAutoTheme(@NonNull Activity activity) {
        return new AlertDialog.Builder(activity, SettingShared.isEnableThemeDark(activity) ? R.style.AppDialogDark_Alert : R.style.AppDialogLight_Alert);
    }

}
