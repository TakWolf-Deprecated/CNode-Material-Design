package org.cnodejs.android.md.ui.base;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.ui.widget.ThemeUtils;

public class StatusBarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            LinearLayout rootView = new LinearLayout(this);
            rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            rootView.setOrientation(LinearLayout.VERTICAL);

            View statusBarView = new View(this);
            statusBarView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ThemeUtils.getStatusBarHeight(this)));
            statusBarView.setBackgroundColor(ThemeUtils.getThemeAttrColor(this, R.attr.colorPrimaryDark));
            rootView.addView(statusBarView);

            View contentView = LayoutInflater.from(this).inflate(layoutResID, rootView, false);
            rootView.addView(contentView);

            super.setContentView(rootView);
        } else {
            super.setContentView(layoutResID);
        }
    }

}
