package org.cnodejs.android.md.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.storage.SettingShared;
import org.cnodejs.android.md.ui.listener.NavigationFinishClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ModifyTopicSignActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    @Bind(R.id.modify_topic_sign_toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.modify_topic_sign_edt_content)
    protected EditText edtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_topic_sign);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.inflateMenu(R.menu.modify_topic_sign);
        toolbar.setOnMenuItemClickListener(this);

        edtContent.setText(SettingShared.getTopicSignContent(this));
        edtContent.setSelection(edtContent.length());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_default:
                edtContent.setText(SettingShared.DEFAULT_TOPIC_SIGN_CONTENT);
                edtContent.setSelection(edtContent.length());
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SettingShared.setTopicSignContent(this, edtContent.getText().toString());
    }

}
