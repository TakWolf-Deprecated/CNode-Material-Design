package org.cnodejs.android.md.display.viewholder;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.dialog.DialogUtils;
import org.cnodejs.android.md.display.dialog.ProgressDialog;
import org.cnodejs.android.md.display.view.ITopicReplyView;
import org.cnodejs.android.md.display.view.ITopicView;
import org.cnodejs.android.md.display.widget.EditorBarHandler;
import org.cnodejs.android.md.display.widget.ToastUtils;
import org.cnodejs.android.md.model.entity.Reply;
import org.cnodejs.android.md.presenter.contract.ITopicReplyPresenter;
import org.cnodejs.android.md.presenter.implement.TopicReplyPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicReplyViewHolder implements ITopicReplyView {

    @Bind(R.id.editor_bar_layout_root)
    protected ViewGroup editorBar;

    @Bind(R.id.topic_reply_window_edt_content)
    protected EditText edtContent;

    private final Activity activity;
    private final ViewGroup layoutRoot;
    private final String topicId;
    private final ITopicView topicView;
    private final PopupWindow replyWindow;
    private final ProgressDialog progressDialog;

    private final ITopicReplyPresenter topicReplyPresenter;

    public TopicReplyViewHolder(@NonNull Activity activity, @NonNull ViewGroup layoutRoot, @NonNull String topicId, @NonNull ITopicView topicView) {
        this.activity = activity;
        this.layoutRoot = layoutRoot;
        this.topicId = topicId;
        this.topicView = topicView;
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.activity_topic_reply_window, layoutRoot, false);
        ButterKnife.bind(this, view);

        replyWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        replyWindow.setBackgroundDrawable(new ColorDrawable(0x01000000));
        replyWindow.setFocusable(true);
        replyWindow.setOutsideTouchable(true);
        replyWindow.setAnimationStyle(R.style.AppWidget_ReplyWindowAnim);

        progressDialog = DialogUtils.createProgressDialog(activity);
        progressDialog.setMessage(R.string.posting_$_);
        progressDialog.setCancelable(false);

        new EditorBarHandler(activity, editorBar, edtContent); // 创建editorBar

        topicReplyPresenter = new TopicReplyPresenter(activity, this);
    }

    @OnClick(R.id.topic_reply_window_btn_tool_close)
    protected void onBtnToolCloseClick() {
        dismissReplyWindow();
    }

    @OnClick(R.id.topic_reply_window_btn_tool_send)
    protected void onBtnToolSendClick() {
        topicReplyPresenter.replyTopicAsyncTask(topicId, edtContent.getText().toString().trim(), null);
    }

    @Override
    public void showReplyWindow() {
        replyWindow.showAtLocation(layoutRoot, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void dismissReplyWindow() {
        replyWindow.dismiss();
    }

    @Override
    public void onAt(@NonNull Reply target) {
        edtContent.getText().insert(edtContent.getSelectionEnd(), " @" + target.getAuthor().getLoginName() + " ");
        showReplyWindow();
    }

    @Override
    public void onContentEmptyError() {
        ToastUtils.with(activity).show(R.string.content_empty_error_tip);
        edtContent.requestFocus();
    }

    @Override
    public void onReplyTopicStart() {
        progressDialog.show();
    }

    @Override
    public boolean onReplyTopicResultOk(@NonNull Reply reply) {
        topicView.appendReplyAndUpdateViews(reply);
        dismissReplyWindow();
        edtContent.setText(null);
        ToastUtils.with(activity).show(R.string.post_success);
        return false;
    }

    @Override
    public void onReplyTopicFinish() {
        progressDialog.dismiss();
    }

}
