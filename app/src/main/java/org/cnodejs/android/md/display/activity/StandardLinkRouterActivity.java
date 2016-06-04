package org.cnodejs.android.md.display.activity;

import android.os.Bundle;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.base.BaseActivity;
import org.cnodejs.android.md.display.util.Navigator;
import org.cnodejs.android.md.display.util.ToastUtils;

public class StandardLinkRouterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Navigator.openStandardLink(this, getIntent().getDataString())) {
            ToastUtils.with(this).show(R.string.invalid_link);
        }
        finish();
    }

}
