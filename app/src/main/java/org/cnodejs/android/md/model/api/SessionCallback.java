package org.cnodejs.android.md.model.api;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.entity.ErrorResult;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.ui.activity.LoginActivity;
import org.cnodejs.android.md.ui.dialog.AlertDialogUtils;
import org.cnodejs.android.md.ui.util.ActivityUtils;

import okhttp3.Headers;

public class SessionCallback<T extends Result> extends ToastCallback<T> {

    public SessionCallback(@NonNull Activity activity) {
        super(activity);
    }

    @Override
    public final boolean onResultError(int code, Headers headers, ErrorResult errorResult) {
        if (code == 401) {
            return onResultAuthError(code, headers, errorResult);
        } else {
            return onResultOtherError(code, headers, errorResult);
        }
    }

    public boolean onResultAuthError(int code, Headers headers, ErrorResult errorResult) {
        Activity activity = getActivity();
        if (ActivityUtils.isAlive(activity)) {
            AlertDialogUtils.createBuilderWithAutoTheme(activity)
                    .setMessage(R.string.access_token_out_of_date)
                    .setPositiveButton(R.string.relogin, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Activity activity = getActivity();
                            if (ActivityUtils.isAlive(activity)) {
                                LoginActivity.startForResult(activity);
                            }
                        }

                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        }
        return false;
    }

    public boolean onResultOtherError(int code, Headers headers, ErrorResult errorResult) {
        return onAnyError(errorResult);
    }

}
