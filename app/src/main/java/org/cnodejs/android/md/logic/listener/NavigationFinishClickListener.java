package org.cnodejs.android.md.logic.listener;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.view.View;

public class NavigationFinishClickListener implements View.OnClickListener {

    private Activity activity;

    public NavigationFinishClickListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        ActivityCompat.finishAfterTransition(activity);
    }

}
