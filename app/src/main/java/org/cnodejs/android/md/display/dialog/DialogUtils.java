package org.cnodejs.android.md.display.dialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import org.cnodejs.android.md.display.widget.ThemeUtils;

public final class DialogUtils {

    private DialogUtils() {}

    public static ProgressDialog createProgressDialog(Context context) {
        return new ProgressDialog(context, ThemeUtils.getDialogThemeRes(context));
    }

    public static AlertDialog.Builder createAlertDialogBuilder(Context context) {
        return new AlertDialog.Builder(context, ThemeUtils.getDialogThemeRes(context));
    }

}
