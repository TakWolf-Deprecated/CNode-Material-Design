package org.cnodejs.android.md.ui.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.entity.Reply;
import org.cnodejs.android.md.model.storage.SettingShared;
import org.cnodejs.android.md.presenter.contract.ICreateReplyPresenter;
import org.cnodejs.android.md.presenter.implement.CreateReplyPresenter;
import org.cnodejs.android.md.ui.util.ToastUtils;
import org.cnodejs.android.md.ui.view.ICreateReplyView;
import org.cnodejs.android.md.ui.view.ITopicView;
import org.cnodejs.android.md.ui.widget.EditorBarHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateReplyDialog extends AppCompatDialog implements ICreateReplyView {

    public static CreateReplyDialog createWithAutoTheme(@NonNull Activity activity, @NonNull String topicId, @NonNull ITopicView topicView) {
        return new CreateReplyDialog(
                activity,
                SettingShared.isEnableThemeDark(activity) ? R.style.AppDialogDark_TopicReply : R.style.AppDialogLight_TopicReply,
                topicId,
                topicView
        );
    }

    @BindView(R.id.layout_editor_bar)
    protected ViewGroup editorBar;

    @BindView(R.id.layout_target)
    protected ViewGroup layoutTarget;

    @BindView(R.id.tv_target)
    protected TextView tvTarget;

    @BindView(R.id.edt_content)
    protected EditText edtContent;

    private final String topicId;
    private final ITopicView topicView;
    private final ProgressDialog progressDialog;
    private final ICreateReplyPresenter createReplyPresenter;

    private String targetId = null;

    private CreateReplyDialog(@NonNull Activity activity, int theme, @NonNull String topicId, @NonNull ITopicView topicView) {
        super(activity, theme);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_topic_reply);
        ButterKnife.bind(this);

        this.topicId = topicId;
        this.topicView = topicView;

        progressDialog = ProgressDialog.createWithAutoTheme(activity);
        progressDialog.setMessage(R.string.posting_$_);
        progressDialog.setCancelable(false);

        new EditorBarHandler(activity, editorBar, edtContent); // 创建editorBar

        createReplyPresenter = new CreateReplyPresenter(activity, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
    }

    @OnClick(R.id.btn_tool_close)
    protected void onBtnToolCloseClick() {
        dismissWindow();
    }

    @OnClick(R.id.btn_tool_send)
    protected void onBtnToolSendClick() {
        createReplyPresenter.createReplyAsyncTask(topicId, edtContent.getText().toString().trim(), targetId);
    }

    @OnClick(R.id.btn_clear_target)
    protected void onBtnClearTargetClick() {
        targetId = null;
        layoutTarget.setVisibility(View.GONE);
    }

    @Override
    public void showWindow() {
        show();
    }

    @Override
    public void dismissWindow() {
        dismiss();
    }

    @Override
    public void onAt(@NonNull Reply target, @NonNull Integer targetPosition) {
        targetId = target.getId();
        layoutTarget.setVisibility(View.VISIBLE);
        tvTarget.setText("回复：" + (targetPosition + 1) + "楼");
        edtContent.getText().insert(edtContent.getSelectionEnd(), "@" + target.getAuthor().getLoginName() + " ");
        showWindow();
    }

    @Override
    public void onContentError(@NonNull String message) {
        ToastUtils.with(getContext()).show(message);
        edtContent.requestFocus();
    }

    @Override
    public void onReplyTopicOk(@NonNull Reply reply) {
        topicView.appendReplyAndUpdateViews(reply);
        dismissWindow();
        targetId = null;
        layoutTarget.setVisibility(View.GONE);
        edtContent.setText(null);
        ToastUtils.with(getContext()).show(R.string.post_success);
    }

    @Override
    public void onReplyTopicStart() {
        progressDialog.show();
    }

    @Override
    public void onReplyTopicFinish() {
        progressDialog.dismiss();
    }

}
