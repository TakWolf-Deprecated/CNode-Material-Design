package org.cnodejs.android.md.ui.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.storage.LoginShared;
import org.cnodejs.android.md.storage.SettingShared;
import org.cnodejs.android.md.ui.activity.MarkdownPreviewActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditorBarHandler {

    private Context context;
    private EditText edtContent;
    private InputMethodManager imm;

    public EditorBarHandler(Context context, View editorBar, EditText edtContent) {
        this.context = context;
        this.edtContent = edtContent;
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        ButterKnife.bind(this, editorBar);
    }

    /**
     * 加粗
     */
    @OnClick(R.id.editor_bar_btn_format_bold)
    protected void onBtnFormatBoldClick() {
        edtContent.requestFocus();
        edtContent.getText().insert(edtContent.getSelectionEnd(), "**string**");
        edtContent.setSelection(edtContent.getSelectionEnd() - 8, edtContent.getSelectionEnd() - 2);
        imm.showSoftInput(edtContent, 0);
    }

    /**
     * 倾斜
     */
    @OnClick(R.id.editor_bar_btn_format_italic)
    protected void onBtnFormatItalicClick() {
        edtContent.requestFocus();
        edtContent.getText().insert(edtContent.getSelectionEnd(), "*string*");
        edtContent.setSelection(edtContent.getSelectionEnd() - 7, edtContent.getSelectionEnd() - 1);
        imm.showSoftInput(edtContent, 0);
    }

    /**
     * 引用
     */
    @OnClick(R.id.editor_bar_btn_format_quote)
    protected void onBtnFormatQuoteClick() {
        edtContent.requestFocus();
        edtContent.getText().insert(edtContent.getSelectionEnd(), "\n\n> ");
        edtContent.setSelection(edtContent.getSelectionEnd());
    }

    /**
     * 无序列表
     */
    @OnClick(R.id.editor_bar_btn_format_list_bulleted)
    protected void onBtnFormatListBulletedClick() {
        edtContent.requestFocus();
        edtContent.getText().insert(edtContent.getSelectionEnd(), "\n\n- ");
        edtContent.setSelection(edtContent.getSelectionEnd());
    }

    /**
     * 有序列表 TODO 这里算法需要优化
     */
    @OnClick(R.id.editor_bar_btn_format_list_numbered)
    protected void onBtnFormatListNumberedClick() {
        edtContent.requestFocus();
        // 查找向上最近一个\n
        for (int n = edtContent.getSelectionEnd() - 1; n >= 0; n--) {
            char c = edtContent.getText().charAt(n);
            if (c == '\n') {
                try {
                    int index = Integer.parseInt(edtContent.getText().charAt(n + 1) + "");
                    if (edtContent.getText().charAt(n + 2) == '.' && edtContent.getText().charAt(n + 3) == ' ') {
                        edtContent.getText().insert(edtContent.getSelectionEnd(), "\n\n" + (index + 1) + ". ");
                        return;
                    }
                } catch (Exception e) {
                    // TODO 这里有问题是如果数字超过10，则无法检测，未来逐渐优化
                }
            }
        }
        // 没找到
        edtContent.getText().insert(edtContent.getSelectionEnd(), "\n\n1. ");
        edtContent.setSelection(edtContent.getSelectionEnd());
    }

    /**
     * 插入代码
     */
    @OnClick(R.id.editor_bar_btn_insert_code)
    protected void onBtnInsertCodeClick() {
        edtContent.requestFocus();
        edtContent.getText().insert(edtContent.getSelectionEnd(), "\n\n```\n\n```\n ");
        edtContent.setSelection(edtContent.getSelectionEnd() - 6);
    }

    /**
     * 插入链接
     */
    @OnClick(R.id.editor_bar_btn_insert_link)
    protected void onBtnInsertLinkClick() {
        new MaterialDialog.Builder(context)
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
     * 插入图片 TODO 图片上传接口使用第三方实现
     */
    @OnClick(R.id.editor_bar_btn_insert_photo)
    protected void onBtnInsertPhotoClick() {
        /*
        if (SettingShared.isEnableThirdPartyImageUploadApi(context)) { // 使用第三方图片接口
            if (!LoginShared.isPermitUseThirdPartyImageUploadApi(context)) { // 用户还没有授权，提示授权对话框
                showThirdPartyImageUploadApiPermitDialog();
            } else {
                insertPhotoApiAction();
            }
        } else { // 未开启，使用默认插入方式
            insertPhotoDefaultAction();
        }
        */

        // TODO 使用默认的图片上传方式
        insertPhotoDefaultAction();
    }

    private void insertPhotoApiAction() {
        // TODO
        Toast.makeText(context, "TODO 进入图片附件页面", Toast.LENGTH_SHORT).show();
    }

    private void insertPhotoDefaultAction() {
        edtContent.requestFocus();
        edtContent.getText().insert(edtContent.getSelectionEnd(), " ![Image](http://resource) ");
        edtContent.setSelection(edtContent.getSelectionEnd() - 10, edtContent.getSelectionEnd() - 2);
        imm.showSoftInput(edtContent, 0);
    }

    private void showThirdPartyImageUploadApiPermitDialog() {
        new MaterialDialog.Builder(context)
                .title("授权提示")
                .content("图片上传接口使用第三方服务，要求上传您的用户令牌进行权限认证，这需要您的授权。如果您不信任这个服务，请选择关闭设置。您要授权使用该服务吗？")
                .positiveText("授权使用")
                .negativeText("关闭设置")
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        LoginShared.setPermitUseThirdPartyImageUploadApi(context, true);
                        showThirdPartyImageUploadApiPermitPositiveDialog();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        SettingShared.setEnableThirdPartyImageUploadApi(context, false);
                        showThirdPartyImageUploadApiPermitNegativeDialog();
                    }

                })
                .show();
    }

    private void showThirdPartyImageUploadApiPermitPositiveDialog() {
        new MaterialDialog.Builder(context)
                .content("授权成功，您可以在设置页面关闭这个功能。")
                .positiveText(R.string.confirm)
                .dismissListener(new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        insertPhotoApiAction();
                    }

                })
                .show();
    }

    private void showThirdPartyImageUploadApiPermitNegativeDialog() {
        new MaterialDialog.Builder(context)
                .content("该功能已关闭，插入图片将使用普通模式。您可以在设置页面中重新开启这个功能。")
                .positiveText(R.string.confirm)
                .dismissListener(new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        insertPhotoDefaultAction();
                    }

                })
                .show();
    }

    /**
     * 预览
     */
    @OnClick(R.id.editor_bar_btn_preview)
    protected void onBtnPreviewClick() {
        String content = edtContent.getText().toString();
        if (SettingShared.isEnableTopicSign(context)) { // 添加小尾巴
            content += "\n\n" + SettingShared.getTopicSignContent(context);
        }

        Intent intent = new Intent(context, MarkdownPreviewActivity.class);
        intent.putExtra("markdownText", content);
        context.startActivity(intent);
    }

}
