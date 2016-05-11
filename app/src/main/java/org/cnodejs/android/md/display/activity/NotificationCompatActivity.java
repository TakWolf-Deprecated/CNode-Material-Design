package org.cnodejs.android.md.display.activity;

import android.os.Bundle;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.base.StatusBarActivity;
import org.cnodejs.android.md.display.util.ThemeUtils;

import butterknife.ButterKnife;

public class NotificationCompatActivity extends StatusBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_compat);
        ButterKnife.bind(this);
    }

}
