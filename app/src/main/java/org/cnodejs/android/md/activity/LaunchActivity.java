package org.cnodejs.android.md.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.util.HandlerUtils;

public class LaunchActivity extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        HandlerUtils.postDelayed(this, 2000);
    }

    @Override
    public void run() {
        if (!isFinishing()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

}
