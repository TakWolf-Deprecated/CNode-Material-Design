package org.cnodejs.android.md.ui.listener;

import android.app.Activity;
import android.view.View;

public class NavigationFinishClickListener implements View.OnClickListener {

    private Activity activity;

    public NavigationFinishClickListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        activity.finish();
    }

}
