package org.cnodejs.android.md.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.entity.TabType;
import org.cnodejs.android.md.model.storage.SettingShared;
import org.cnodejs.android.md.model.storage.TopicShared;
import org.cnodejs.android.md.presenter.contract.ICreateTopicPresenter;
import org.cnodejs.android.md.presenter.implement.CreateTopicPresenter;
import org.cnodejs.android.md.ui.base.StatusBarActivity;
import org.cnodejs.android.md.ui.dialog.ProgressDialog;
import org.cnodejs.android.md.ui.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.ui.util.Navigator;
import org.cnodejs.android.md.ui.util.ThemeUtils;
import org.cnodejs.android.md.ui.util.ToastUtils;
import org.cnodejs.android.md.ui.view.ICreateTopicView;
import org.cnodejs.android.md.ui.viewholder.EditorBarViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateTopicActivity extends StatusBarActivity implements Toolbar.OnMenuItemClickListener, ICreateTopicView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.spn_tab)
    Spinner spnTab;

    @BindView(R.id.edt_title)
    EditText edtTitle;

    @BindView(R.id.layout_editor_bar)
    ViewGroup editorBar;

    @BindView(R.id.edt_content)
    EditText edtContent;

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

        progressDialog = ProgressDialog.createWithAutoTheme(this);
        progressDialog.setCancelable(false);

        // 创建EditorBar
        new EditorBarViewHolder(this, editorBar, edtContent);

        // 载入草稿
        if (SettingShared.isEnableTopicDraft(this)) {
            spnTab.setSelection(TopicShared.getDraftTabPosition(this));
            edtContent.setText(TopicShared.getDraftContent(this));
            edtContent.setSelection(edtContent.length());
            edtTitle.setText(TopicShared.getDraftTitle(this));
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
        if (SettingShared.isEnableTopicDraft(this) && saveTopicDraft) {
            TopicShared.setDraftTabPosition(this, spnTab.getSelectedItemPosition());
            TopicShared.setDraftTitle(this, edtTitle.getText().toString());
            TopicShared.setDraftContent(this, edtContent.getText().toString());
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                createTopicPresenter.createTopicAsyncTask(getTabByPosition(spnTab.getSelectedItemPosition()), edtTitle.getText().toString().trim(), edtContent.getText().toString().trim());
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
    public void onTitleError(@NonNull String message) {
        ToastUtils.with(this).show(message);
        edtTitle.requestFocus();
    }

    @Override
    public void onContentError(@NonNull String message) {
        ToastUtils.with(this).show(message);
        edtContent.requestFocus();
    }

    @Override
    public void onCreateTopicOk(@NonNull String topicId) {
        saveTopicDraft = false;
        TopicShared.clear(this);
        ToastUtils.with(this).show(R.string.post_success);
        Navigator.TopicWithAutoCompat.start(this, topicId);
        finish();
    }

    @Override
    public void onCreateTopicStart() {
        progressDialog.show();
    }

    @Override
    public void onCreateTopicFinish() {
        progressDialog.dismiss();
    }

}
