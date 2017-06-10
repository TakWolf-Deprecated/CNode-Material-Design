package org.cnodejs.android.md.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.storage.SettingShared;
import org.cnodejs.android.md.ui.base.StatusBarActivity;
import org.cnodejs.android.md.ui.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.ui.util.ThemeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends StatusBarActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.switch_notification)
    SwitchCompat switchNotification;

    @BindView(R.id.switch_theme_dark)
    SwitchCompat switchThemeDark;

    @BindView(R.id.switch_topic_draft)
    SwitchCompat switchTopicDraft;

    @BindView(R.id.switch_topic_sign)
    SwitchCompat switchTopicSign;

    @BindView(R.id.btn_modify_topic_sign)
    TextView btnModifyTopicSign;

    @BindView(R.id.switch_topic_render_compat)
    SwitchCompat switchTopicRenderCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        switchNotification.setChecked(SettingShared.isEnableNotification(this));
        switchThemeDark.setChecked(SettingShared.isEnableThemeDark(this));
        switchTopicDraft.setChecked(SettingShared.isEnableTopicDraft(this));
        switchTopicSign.setChecked(SettingShared.isEnableTopicSign(this));
        btnModifyTopicSign.setEnabled(SettingShared.isEnableTopicSign(this));
        switchTopicRenderCompat.setChecked(SettingShared.isEnableTopicRenderCompat(this));
    }

    @OnClick(R.id.btn_notification)
    void onBtnNotificationClick() {
        switchNotification.toggle();
        SettingShared.setEnableNotification(this, switchNotification.isChecked());
    }

    @OnClick(R.id.btn_theme_dark)
    void onBtnThemeDarkClick() {
        switchThemeDark.toggle();
        SettingShared.setEnableThemeDark(this, switchThemeDark.isChecked());
        ThemeUtils.notifyThemeApply(this);
    }

    @OnClick(R.id.btn_topic_draft)
    void onBtnTopicDraftClick() {
        switchTopicDraft.toggle();
        SettingShared.setEnableTopicDraft(this, switchTopicDraft.isChecked());
    }

    @OnClick(R.id.btn_topic_sign)
    void onBtnTopicSignClick() {
        switchTopicSign.toggle();
        SettingShared.setEnableTopicSign(this, switchTopicSign.isChecked());
        btnModifyTopicSign.setEnabled(switchTopicSign.isChecked());
    }

    @OnClick(R.id.btn_modify_topic_sign)
    void onBtnModifyTopicSignClick() {
        startActivity(new Intent(this, ModifyTopicSignActivity.class));
    }

    @OnClick(R.id.btn_topic_render_compat)
    void onBtnTopicRenderCompatClick() {
        switchTopicRenderCompat.toggle();
        SettingShared.setEnableTopicRenderCompat(this, switchTopicRenderCompat.isChecked());
    }

}
