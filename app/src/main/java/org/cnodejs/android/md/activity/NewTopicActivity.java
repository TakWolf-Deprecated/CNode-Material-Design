package org.cnodejs.android.md.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.model.entity.TabType;
import org.cnodejs.android.md.storage.LoginShared;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewTopicActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    @Bind(R.id.new_topic_toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.new_topic_spn_tab)
    protected Spinner spnTab;

    @Bind(R.id.new_topic_edt_title)
    protected EditText edtTitle;

    @Bind(R.id.new_topic_edt_content)
    protected EditText edtContent;

    @Bind(R.id.new_topic_btn_tool_format_list_bulleted)
    protected CheckedTextView ctvToolFormatListBulleted;

    @Bind(R.id.new_topic_btn_tool_format_list_numbered)
    protected CheckedTextView ctvToolFormatListNumbered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_topic);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.inflateMenu(R.menu.new_topic);
        toolbar.setOnMenuItemClickListener(this);

        // 载入草稿
        spnTab.setSelection(LoginShared.getNewTopicTabPosition(this));
        edtTitle.setText(LoginShared.getNewTopicTitle(this));
        edtContent.setText(LoginShared.getNewTopicContent(this));
    }

    /**
     * 实时保存草稿
     */
    @Override
    protected void onPause() {
        super.onPause();
        LoginShared.setNewTopicTabPosition(this, spnTab.getSelectedItemPosition());
        LoginShared.setNewTopicTitle(this, edtTitle.getText().toString());
        LoginShared.setNewTopicContent(this, edtContent.getText().toString());
    }

    //===========
    // 工具条逻辑
    //===========

    /**
     * 加粗
     */
    @OnClick(R.id.new_topic_btn_tool_format_bold)
    protected void onBtnToolFormatBoldClick() {
        edtContent.requestFocus();
        edtContent.getText().insert(edtContent.getSelectionEnd(), "****");
        edtContent.setSelection(edtContent.getSelectionEnd() - 2);
    }

    /**
     * 倾斜
     */
    @OnClick(R.id.new_topic_btn_tool_format_italic)
    protected void onBtnToolFormatItalicClick() {
        edtContent.requestFocus();
        edtContent.getText().insert(edtContent.getSelectionEnd(), "**");
        edtContent.setSelection(edtContent.getSelectionEnd() - 1);
    }








    /**
     * 插入链接
     */
    @OnClick(R.id.new_topic_btn_tool_insert_link)
    protected void onBtnToolInsertLinkClick() {
        new MaterialDialog.Builder(this)
                .iconRes(R.drawable.ic_insert_link_grey600_24dp)
                .title(R.string.add_link)
                .customView(R.layout.dialog_tool_insert_link, false)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        View view = dialog.getCustomView();
                        EditText edtTitle = ButterKnife.findById(view, R.id.dialog_tool_insert_link_edt_title);
                        EditText edtLink = ButterKnife.findById(view, R.id.dialog_tool_insert_link_edt_link);

                        String insertText = " [" + edtTitle.getText() + "](" + edtLink.getText() + ") ";
                        edtContent.requestFocus();
                        edtContent.getText().insert(edtContent.getSelectionEnd(), insertText);
                    }

                })
                .show();
    }

    /**
     * 插入图片 TODO 目前没有图片上传接口
     */
    @OnClick(R.id.new_topic_btn_tool_insert_photo)
    protected void onBtnToolInsertPhotoClick() {
        edtContent.requestFocus();
        edtContent.getText().insert(edtContent.getSelectionEnd(), " ![](http://) ");
        edtContent.setSelection(edtContent.getSelectionEnd() - 11);
        Toast.makeText(this, "暂时不支持图片上传", Toast.LENGTH_SHORT).show();
    }

    /**
     * 预览 TODO
     */
    @OnClick(R.id.new_topic_btn_tool_preview)
    protected void onBtnToolPreviewClick() {
        Toast.makeText(this, "暂时不支持预览", Toast.LENGTH_SHORT).show();
    }

    //================
    // 工具条逻辑-END-
    //================

    /**
     * 发送逻辑
     */

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                if (edtTitle.length() < 10) {
                    edtTitle.requestFocus();
                    Toast.makeText(this, "标题要求10字以上", Toast.LENGTH_SHORT).show();
                } else if (edtContent.length() == 0) {
                    edtContent.requestFocus();
                    Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    newTipicAsyncTask(getTabByPosition(spnTab.getSelectedItemPosition()), edtTitle.getText().toString(), edtContent.getText().toString());
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

    private void newTipicAsyncTask(TabType tab, String title, String content) {

        // TODO

    }

}
