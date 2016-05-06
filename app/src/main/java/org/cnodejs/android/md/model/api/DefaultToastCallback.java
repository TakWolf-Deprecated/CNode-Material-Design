package org.cnodejs.android.md.model.api;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.activity.LoginActivity;
import org.cnodejs.android.md.display.dialog.AlertDialogUtils;
import org.cnodejs.android.md.display.util.ActivityUtils;
import org.cnodejs.android.md.display.util.ToastUtils;
import org.cnodejs.android.md.model.entity.Result;

import retrofit2.Response;

public class DefaultToastCallback<T extends Result> extends CallbackAdapter<T> {

    private final Activity activity;

    public DefaultToastCallback(@NonNull Activity activity) {
        this.activity = activity;
    }

    @Override
    public final boolean onResultError(Response<T> response, Result.Error error) {
        if (response.code() == 401) {
            return onResultErrorAuth(response, error);
        } else {
            return onResultErrorOther(response, error);
        }
    }

    public boolean onResultErrorAuth(Response<T> response, Result.Error error) {
        if (ActivityUtils.isAlive(activity)) {
            AlertDialogUtils.createBuilderWithAutoTheme(activity)
                    .setMessage(R.string.access_token_out_of_date)
                    .setPositiveButton(R.string.relogin, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LoginActivity.startForResult(activity);
                        }

                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        }
        return false;
    }

    public boolean onResultErrorOther(Response<T> response, Result.Error error) {
        if (ActivityUtils.isAlive(activity)) {
            ToastUtils.with(activity).show(error.getErrorMessage());
        }
        return false;
    }

    @Override
    public boolean onCallException(Throwable t, Result.Error error) {
        if (ActivityUtils.isAlive(activity)) {
            ToastUtils.with(activity).show(error.getErrorMessage());
        }
        return false;
    }

}
