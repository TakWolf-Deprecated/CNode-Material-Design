package org.cnodejs.android.md.display.listener;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;

public class NavigationFinishClickListener implements View.OnClickListener {

    private final Activity activity;

    public NavigationFinishClickListener(@NonNull Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        ActivityCompat.finishAfterTransition(activity);
    }

}
