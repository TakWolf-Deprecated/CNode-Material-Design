package org.cnodejs.android.md.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.umeng.update.UmengUpdateAgent;

import org.cnodejs.android.md.BuildConfig;
import org.cnodejs.android.md.R;
import org.cnodejs.android.md.ui.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.util.ShipUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

    public static final String VERSION_TEXT = BuildConfig.VERSION_NAME + "-build-" + BuildConfig.VERSION_CODE;

    @Bind(R.id.about_toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.about_tv_version)
    protected TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        tvVersion.setText(getString(R.string.current_version_$) + VERSION_TEXT);
    }

    @OnClick(R.id.about_btn_version)
    protected void onBtnVersionClick() {
        UmengUpdateAgent.forceUpdate(this);
    }

    @OnClick(R.id.about_btn_open_source_url)
    protected void onBtnOpenSourceUrlClick() {
        ShipUtils.openInBrowser(this, getString(R.string.open_source_url_content));
    }

    @OnClick(R.id.about_btn_about_third_party_image_upload_api)
    protected void onBtnAboutThirdPartyImageUploadApiClick() {
        ShipUtils.openInBrowser(this, getString(R.string.about_third_party_image_upload_api_content));
    }

    @OnClick(R.id.about_btn_about_cnode)
    protected void onBtnAboutCNodeClick() {
        ShipUtils.openInBrowser(this, getString(R.string.about_cnode_content));
    }

    @OnClick(R.id.about_btn_about_author)
    protected void onBtnAboutAuthorClick() {
        ShipUtils.openInBrowser(this, getString(R.string.about_author_content));
    }

    @OnClick(R.id.about_btn_advice_feedback)
    protected void onBtnAdviceFeedbackClick() {
        ShipUtils.sendEmail(
                this,
                "takwolf@foxmail.com",
                "来自 CNodeMD-" + VERSION_TEXT + " 的客户端反馈",
                "设备信息：Android " + Build.VERSION.RELEASE + " - " + Build.MANUFACTURER + " - " + Build.MODEL + "\n（如果涉及隐私请手动删除这个内容）\n\n");
    }

    @OnClick(R.id.about_fab_open_in_app_store)
    protected void onBtnOpenInAppStoreClick() {
        new MaterialDialog.Builder(this)
                .title(R.string.open_in_app_store)
                .content(R.string.open_in_app_store_content)
                .positiveText(R.string.go_to_app_store)
                .negativeText(R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        ShipUtils.openInAppStore(AboutActivity.this);
                    }

                })
                .show();
    }

    @OnClick(R.id.about_btn_open_source_license)
    protected void onBtnOpenSourceLicenseClick() {
        startActivity(new Intent(this, LicenseActivity.class));
    }

}
