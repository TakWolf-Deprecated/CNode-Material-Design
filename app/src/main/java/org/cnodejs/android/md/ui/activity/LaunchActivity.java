package org.cnodejs.android.md.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.ui.base.BaseActivity;
import org.cnodejs.android.md.ui.util.ActivityUtils;
import org.cnodejs.android.md.util.HandlerUtils;

public class LaunchActivity extends BaseActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        HandlerUtils.handler.postDelayed(this, 1000);
    }

    @Override
    public void run() {
        if (ActivityUtils.isAlive(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

}
