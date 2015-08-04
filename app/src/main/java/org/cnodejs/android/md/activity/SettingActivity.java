package org.cnodejs.android.md.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.rey.material.widget.Switch;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.listener.NavigationFinishClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    @Bind(R.id.setting_toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.setting_switch_notification)
    protected Switch switchNotification;

    @Bind(R.id.setting_switch_topic_sign)
    protected Switch switchTopicSign;

    @Bind(R.id.setting_btn_modify_topic_sign)
    protected TextView btnModifyTopicSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
    }

    @OnClick(R.id.setting_btn_notification)
    protected void onBtnNotificationClick() {
        switchNotification.toggle();
    }

    @OnClick(R.id.setting_btn_topic_sign)
    protected void onBtnTopicSignClick() {
        switchTopicSign.toggle();
        btnModifyTopicSign.setEnabled(switchTopicSign.isChecked());
    }

    @OnClick(R.id.setting_btn_modify_topic_sign)
    protected void onBtnModifyTopicSignClick() {

    }

}
