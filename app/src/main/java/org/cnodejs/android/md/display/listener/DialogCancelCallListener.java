package org.cnodejs.android.md.display.listener;

import android.content.DialogInterface;
import android.support.annotation.NonNull;

import retrofit2.Call;

public class DialogCancelCallListener implements DialogInterface.OnCancelListener {

    private final Call call;

    public DialogCancelCallListener(@NonNull Call call) {
        this.call = call;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (!call.isCanceled()) {
            call.cancel();
        }
    }

}
