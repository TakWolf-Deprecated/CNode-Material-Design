package org.cnodejs.android.md.display.base;

import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
