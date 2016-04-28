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
import org.cnodejs.android.md.display.activity.TopicActivity;
import org.cnodejs.android.md.display.adapter.TopicAdapter;
import org.cnodejs.android.md.display.dialog.DialogUtils;
import org.cnodejs.android.md.display.dialog.ProgressDialog;
import org.cnodejs.android.md.display.widget.EditorBarHandler;
import org.cnodejs.android.md.display.widget.ToastUtils;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.DefaultToastCallback;
import org.cnodejs.android.md.model.entity.Author;
import org.cnodejs.android.md.model.entity.Reply;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.model.storage.SettingShared;
import org.cnodejs.android.md.util.FormatUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class TopicReplyViewHolder implements TopicAdapter.OnAtClickListener {

    @Bind(R.id.editor_bar_layout_root)
    protected ViewGroup editorBar;

    @Bind(R.id.topic_reply_window_edt_content)
    protected EditText edtContent;

    private final Activity activity;
    private final ViewGroup layoutRoot;
    private final String topicId;
    private final TopicActivity topicActivity;
    private final PopupWindow replyWindow;
    private final ProgressDialog progressDialog;

    public TopicReplyViewHolder(@NonNull Activity activity, @NonNull ViewGroup layoutRoot, @NonNull String topicId, @NonNull TopicActivity topicActivity) {
        this.activity = activity;
        this.layoutRoot = layoutRoot;
        this.topicId = topicId;
        this.topicActivity = topicActivity;
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
    }

    public void showReplyWindow() {
        replyWindow.showAtLocation(layoutRoot, Gravity.BOTTOM, 0, 0);
    }

    public void dismissReplyWindow() {
        replyWindow.dismiss();
    }

    @OnClick(R.id.topic_reply_window_btn_tool_close)
    protected void onBtnToolCloseClick() {
        dismissReplyWindow();
    }

    @OnClick(R.id.topic_reply_window_btn_tool_send)
    protected void onBtnToolSendClick() {
        if (edtContent.length() == 0) {
            ToastUtils.with(activity).show(R.string.content_empty_error_tip);
        } else {
            String content = edtContent.getText().toString();
            if (SettingShared.isEnableTopicSign(activity)) { // 添加小尾巴
                content += "\n\n" + SettingShared.getTopicSignContent(activity);
            }
            replyTopicAsyncTask(content);
        }
    }

    private void replyTopicAsyncTask(final String content) {
        progressDialog.show();
        Call<Result.ReplyTopic> call = ApiClient.service.replyTopic(topicId, LoginShared.getAccessToken(activity), content, null);
        call.enqueue(new DefaultToastCallback<Result.ReplyTopic>(activity) {

            @Override
            public boolean onResultOk(Response<Result.ReplyTopic> response, Result.ReplyTopic result) {
                Reply reply = new Reply();
                reply.setId(result.getReplyId());
                Author author = new Author();
                author.setLoginName(LoginShared.getLoginName(activity));
                author.setAvatarUrl(LoginShared.getAvatarUrl(activity));
                reply.setAuthor(author);
                reply.setContent(content);
                reply.setHandleContent(FormatUtils.renderMarkdown(content)); // 本地要做预渲染处理
                reply.setCreateAt(new DateTime());
                reply.setUpList(new ArrayList<String>());
                topicActivity.insertReplyAndUpdateViews(reply);
                dismissReplyWindow();
                edtContent.setText(null);
                ToastUtils.with(activity).show(R.string.post_success);
                return false;
            }

            @Override
            public void onFinish() {
                progressDialog.dismiss();
            }

        });
    }

    @Override
    public void onAt(String loginName) {
        edtContent.getText().insert(edtContent.getSelectionEnd(), " @" + loginName + " ");
        showReplyWindow();
    }

}
