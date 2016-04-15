package org.cnodejs.android.md.display.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.base.StatusBarActivity;
import org.cnodejs.android.md.display.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.display.widget.ThemeUtils;
import org.cnodejs.android.md.util.ResRawUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LicenseActivity extends StatusBarActivity {

    @Bind(R.id.license_toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.license_tv_license)
    protected TextView tvLicense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        tvLicense.setText(ResRawUtils.getString(this, R.raw.open_source));
    }

}
