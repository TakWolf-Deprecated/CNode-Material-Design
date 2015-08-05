package org.cnodejs.android.md.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.rey.material.widget.Switch;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.storage.SettingShared;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity implements Switch.OnCheckedChangeListener {

    @Bind(R.id.setting_toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.setting_switch_notification)
    protected Switch switchNotification;

    @Bind(R.id.setting_switch_new_topic_draft)
    protected Switch switchNewTopicDraft;

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

        switchNotification.setChecked(SettingShared.isEnableNotification(this));
        switchNewTopicDraft.setChecked(SettingShared.isEnableNewTopicDraft(this));
        switchTopicSign.setChecked(SettingShared.isEnableTopicSign(this));
        btnModifyTopicSign.setEnabled(SettingShared.isEnableTopicSign(this));

        switchNotification.setOnCheckedChangeListener(this);
        switchNewTopicDraft.setOnCheckedChangeListener(this);
        switchTopicSign.setOnCheckedChangeListener(this);
    }

    @OnClick(R.id.setting_btn_notification)
    protected void onBtnNotificationClick() {
        switchNotification.toggle();
    }

    @OnClick(R.id.setting_btn_new_topic_draft)
    protected void onBtnNewTopicDraftClick() {
        switchNewTopicDraft.toggle();
    }

    @OnClick(R.id.setting_btn_topic_sign)
    protected void onBtnTopicSignClick() {
        switchTopicSign.toggle();
    }

    @Override
    public void onCheckedChanged(Switch switchView, boolean b) {
        switch (switchView.getId()) {
            case R.id.setting_switch_notification:
                SettingShared.setEnableNotification(this, b);
                break;
            case R.id.setting_switch_new_topic_draft:
                SettingShared.setEnableNewTopicDraft(this, b);
                break;
            case R.id.setting_switch_topic_sign:
                SettingShared.setEnableTopicSign(this, b);
                btnModifyTopicSign.setEnabled(b);
                break;
        }
    }

    @OnClick(R.id.setting_btn_modify_topic_sign)
    protected void onBtnModifyTopicSignClick() {
        startActivity(new Intent(this, ModifyTopicSignActivity.class));
    }

}
