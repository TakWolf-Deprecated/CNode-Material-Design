package org.cnodejs.android.md.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.Switch;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.storage.SettingShared;
import org.cnodejs.android.md.ui.listener.NavigationFinishClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity implements Switch.OnCheckedChangeListener {

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

    @Bind(R.id.setting_switch_third_party_image_upload_api)
    protected Switch switchThirdPartyImageUploadApi;

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
        switchThirdPartyImageUploadApi.setChecked(SettingShared.isEnableThirdPartyImageUploadApi(this));

        switchNotification.setOnCheckedChangeListener(this);
        switchNewTopicDraft.setOnCheckedChangeListener(this);
        switchTopicSign.setOnCheckedChangeListener(this);
        switchThirdPartyImageUploadApi.setOnCheckedChangeListener(this);
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

    @OnClick(R.id.setting_btn_third_party_image_upload_api)
    protected void onBtnThirdPartyImageUploadApiClick() {
        switchThirdPartyImageUploadApi.toggle();
    }

    @Override
    public void onCheckedChanged(Switch switchView, boolean isChecked) {
        switch (switchView.getId()) {
            case R.id.setting_switch_notification:
                SettingShared.setEnableNotification(this, isChecked);
                break;
            case R.id.setting_switch_new_topic_draft:
                SettingShared.setEnableNewTopicDraft(this, isChecked);
                break;
            case R.id.setting_switch_topic_sign:
                SettingShared.setEnableTopicSign(this, isChecked);
                btnModifyTopicSign.setEnabled(isChecked);
                break;
            case R.id.setting_switch_third_party_image_upload_api:
                SettingShared.setEnableThirdPartyImageUploadApi(this, isChecked);

                // TODO
                Toast.makeText(this, "该功能暂未实现", Toast.LENGTH_SHORT).show();
                // TODO

                break;
        }
    }

    @OnClick(R.id.setting_btn_modify_topic_sign)
    protected void onBtnModifyTopicSignClick() {
        startActivity(new Intent(this, ModifyTopicSignActivity.class));
    }

}
