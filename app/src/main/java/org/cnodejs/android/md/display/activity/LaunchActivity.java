package org.cnodejs.android.md.display.activity;

import android.content.Intent;
import android.os.Bundle;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.base.BaseActivity;
import org.cnodejs.android.md.display.util.ActivityUtils;
import org.cnodejs.android.md.util.HandlerUtils;

public class LaunchActivity extends BaseActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        HandlerUtils.postDelayed(this, 2000);
    }

    @Override
    public void run() {
        if (ActivityUtils.isAlive(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

}
