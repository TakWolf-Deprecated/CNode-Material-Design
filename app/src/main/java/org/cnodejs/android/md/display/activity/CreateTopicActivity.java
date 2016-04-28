package org.cnodejs.android.md.display.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.base.StatusBarActivity;
import org.cnodejs.android.md.display.dialog.DialogUtils;
import org.cnodejs.android.md.display.dialog.ProgressDialog;
import org.cnodejs.android.md.display.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.display.view.ICreateTopicView;
import org.cnodejs.android.md.display.widget.EditorBarHandler;
import org.cnodejs.android.md.display.widget.ThemeUtils;
import org.cnodejs.android.md.display.widget.ToastUtils;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.TabType;
import org.cnodejs.android.md.model.storage.SettingShared;
import org.cnodejs.android.md.model.storage.TopicShared;
import org.cnodejs.android.md.presenter.contract.ICreateTopicPresenter;
import org.cnodejs.android.md.presenter.implement.CreateTopicPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateTopicActivity extends StatusBarActivity implements Toolbar.OnMenuItemClickListener, ICreateTopicView {

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

    private ICreateTopicPresenter createTopicPresenter;

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

        createTopicPresenter = new CreateTopicPresenter(this, this);
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
                createTopicPresenter.createTipicAsyncTask(getTabByPosition(spnTab.getSelectedItemPosition()), edtTitle.getText().toString().trim(), edtContent.getText().toString().trim());
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

    @Override
    public void onTitleEmptyError() {
        ToastUtils.with(this).show(R.string.title_empty_error_tip);
        edtTitle.requestFocus();
    }

    @Override
    public void onContentEmptyError() {
        ToastUtils.with(this).show(R.string.content_empty_error_tip);
        edtContent.requestFocus();
    }

    @Override
    public void onCreateTopicStart() {
        progressDialog.show();
    }

    @Override
    public boolean onCreateTopicResultOk(@NonNull Result.CreateTopic result) {
        saveTopicDraft = false;
        TopicShared.clear(this);
        ToastUtils.with(this).show(R.string.post_success);
        TopicActivity.start(this, result.getTopicId());
        finish();
        return false;
    }

    @Override
    public void onCreateTopicFinish() {
        progressDialog.dismiss();
    }

}
