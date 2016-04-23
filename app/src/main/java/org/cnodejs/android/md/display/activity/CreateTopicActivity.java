package org.cnodejs.android.md.display.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.DefaultToastCallback;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.TabType;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.model.storage.SettingShared;
import org.cnodejs.android.md.model.storage.TopicShared;
import org.cnodejs.android.md.display.base.StatusBarActivity;
import org.cnodejs.android.md.display.dialog.DialogUtils;
import org.cnodejs.android.md.display.dialog.ProgressDialog;
import org.cnodejs.android.md.display.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.display.widget.EditorBarHandler;
import org.cnodejs.android.md.display.widget.ThemeUtils;
import org.cnodejs.android.md.display.widget.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class CreateTopicActivity extends StatusBarActivity implements Toolbar.OnMenuItemClickListener {

    @Bind(R.id.create_topic_toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.create_topic_spn_tab)
    protected Spinner spnTab;

    @Bind(R.id.create_topic_edt_title)
    protected EditText edtTitle;

    @Bind(R.id.editor_bar_layout_root)
    protected ViewGroup editorBar;

    @Bind(R.id.create_topic_edt_content)
    protected EditText edtContent;

    private ProgressDialog progressDialog;

    private boolean saveTopicDraft = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.inflateMenu(R.menu.create_topic);
        toolbar.setOnMenuItemClickListener(this);

        progressDialog = DialogUtils.createProgressDialog(this);
        progressDialog.setMessage(R.string.posting_$_);
        progressDialog.setCancelable(false);

        // 创建EditorBar
        new EditorBarHandler(this, editorBar, edtContent);

        // 载入草稿
        if (SettingShared.isEnableNewTopicDraft(this)) {
            spnTab.setSelection(TopicShared.getNewTopicTabPosition(this));
            edtContent.setText(TopicShared.getNewTopicContent(this));
            edtContent.setSelection(edtContent.length());
            edtTitle.setText(TopicShared.getNewTopicTitle(this));
            edtTitle.setSelection(edtTitle.length()); // 这个必须最后调用
        }
    }

    /**
     * 实时保存草稿
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (SettingShared.isEnableNewTopicDraft(this) && saveTopicDraft) {
            TopicShared.setNewTopicTabPosition(this, spnTab.getSelectedItemPosition());
            TopicShared.setNewTopicTitle(this, edtTitle.getText().toString());
            TopicShared.setNewTopicContent(this, edtContent.getText().toString());
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                if (edtTitle.length() < 10) {
                    edtTitle.requestFocus();
                    ToastUtils.with(this).show(R.string.title_empty_error_tip);
                } else if (edtContent.length() == 0) {
                    edtContent.requestFocus();
                    ToastUtils.with(this).show(R.string.content_empty_error_tip);
                } else {
                    TabType tab = getTabByPosition(spnTab.getSelectedItemPosition());
                    String title = edtTitle.getText().toString().trim();
                    String content = edtContent.getText().toString();
                    if (SettingShared.isEnableTopicSign(this)) { // 添加小尾巴
                        content += "\n\n" + SettingShared.getTopicSignContent(this);
                    }
                    createTipicAsyncTask(tab, title, content);
                }
                return true;
            default:
                return false;
        }
    }

    private TabType getTabByPosition(int position) {
        switch (position) {
            case 0:
                return TabType.share;
            case 1:
                return TabType.ask;
            case 2:
                return TabType.job;
            default:
                return TabType.share;
        }
    }

    private void createTipicAsyncTask(TabType tab, String title, String content) {
        progressDialog.show();
        Call<Result.CreateTopic> call = ApiClient.service.createTopic(LoginShared.getAccessToken(this), tab, title, content);
        call.enqueue(new DefaultToastCallback<Result.CreateTopic>(this) {

            @Override
            public boolean onResultOk(Response<Result.CreateTopic> response, Result.CreateTopic result) {
                saveTopicDraft = false;
                TopicShared.clear(CreateTopicActivity.this);
                ToastUtils.with(CreateTopicActivity.this).show(R.string.post_success);
                TopicActivity.start(CreateTopicActivity.this, result.getTopicId());
                finish();
                return false;
            }

            @Override
            public void onFinish() {
                progressDialog.dismiss();
            }

        });
    }

}
