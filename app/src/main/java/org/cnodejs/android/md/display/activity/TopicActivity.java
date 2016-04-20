package org.cnodejs.android.md.display.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.melnykov.fab.FloatingActionButton;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.adapter.TopicAdapter;
import org.cnodejs.android.md.display.base.StatusBarActivity;
import org.cnodejs.android.md.display.dialog.DialogUtils;
import org.cnodejs.android.md.display.dialog.ProgressDialog;
import org.cnodejs.android.md.display.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.display.viewholder.TopicHeaderViewHolder;
import org.cnodejs.android.md.display.widget.EditorBarHandler;
import org.cnodejs.android.md.display.widget.RefreshLayoutUtils;
import org.cnodejs.android.md.display.widget.ThemeUtils;
import org.cnodejs.android.md.display.widget.ToastUtils;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.DefaultToastCallback;
import org.cnodejs.android.md.model.entity.Author;
import org.cnodejs.android.md.model.entity.Reply;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.TopicWithReply;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.model.storage.SettingShared;
import org.cnodejs.android.md.model.util.EntityUtils;
import org.cnodejs.android.md.util.FormatUtils;
import org.cnodejs.android.md.util.ShipUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class TopicActivity extends StatusBarActivity implements SwipeRefreshLayout.OnRefreshListener, TopicAdapter.OnAtClickListener, Toolbar.OnMenuItemClickListener {

    private static final String EXTRA_TOPIC_ID = "topicId";
    private static final String EXTRA_TOPIC = "topic";

    public static void start(@NonNull Activity activity, @NonNull Topic topic) {
        Intent intent = new Intent(activity, TopicActivity.class);
        intent.putExtra(EXTRA_TOPIC_ID, topic.getId());
        intent.putExtra(EXTRA_TOPIC, EntityUtils.gson.toJson(topic));
        activity.startActivity(intent);
    }

    public static void start(@NonNull Activity activity, String topicId) {
        Intent intent = new Intent(activity, TopicActivity.class);
        intent.putExtra(EXTRA_TOPIC_ID, topicId);
        activity.startActivity(intent);
    }

    public static void start(@NonNull Context context, String topicId) {
        Intent intent = new Intent(context, TopicActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_TOPIC_ID, topicId);
        context.startActivity(intent);
    }

    @Bind(R.id.topic_layout_root)
    protected ViewGroup layoutRoot;

    @Bind(R.id.topic_toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.topic_refresh_layout)
    protected SwipeRefreshLayout refreshLayout;

    @Bind(R.id.topic_list_view)
    protected ListView listView;

    @Bind(R.id.topic_icon_no_data)
    protected View iconNoData;

    @Bind(R.id.topic_fab_reply)
    protected FloatingActionButton fabReply;

    private PopupWindow replyWindow;
    private ReplyHandler replyHandler;

    private ProgressDialog progressDialog;

    private String topicId;
    private Topic topic;
    private final List<Reply> replyList = new ArrayList<>();

    private TopicHeaderViewHolder headerViewHolder;
    private TopicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        ButterKnife.bind(this);

        topicId = getIntent().getStringExtra(EXTRA_TOPIC_ID);
        if (!TextUtils.isEmpty(getIntent().getStringExtra(EXTRA_TOPIC))) {
            topic = EntityUtils.gson.fromJson(getIntent().getStringExtra(EXTRA_TOPIC), Topic.class);
        }

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.inflateMenu(R.menu.topic);
        toolbar.setOnMenuItemClickListener(this);

        headerViewHolder = new TopicHeaderViewHolder(this, listView);
        headerViewHolder.update(topic, false, 0);
        adapter = new TopicAdapter(this, replyList, this);
        listView.setAdapter(adapter);

        iconNoData.setVisibility(topic == null ? View.VISIBLE : View.GONE);

        fabReply.attachToListView(listView);

        // 创建回复窗口
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.activity_topic_reply_window, layoutRoot, false);
        replyHandler = new ReplyHandler(view);

        replyWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        replyWindow.setBackgroundDrawable(new ColorDrawable(0x01000000));
        replyWindow.setFocusable(true);
        replyWindow.setOutsideTouchable(true);
        replyWindow.setAnimationStyle(R.style.AppWidget_ReplyWindowAnim);
        // - END -

        progressDialog = DialogUtils.createProgressDialog(this);
        progressDialog.setMessage(R.string.posting_$_);
        progressDialog.setCancelable(false);

        RefreshLayoutUtils.initOnCreate(refreshLayout, this);
        RefreshLayoutUtils.refreshOnCreate(refreshLayout, this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_in_browser:
                ShipUtils.openInBrowser(this, "https://cnodejs.org/topic/" + topicId);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onRefresh() {
        Call<Result.Data<TopicWithReply>> call = ApiClient.service.getTopic(topicId, LoginShared.getAccessToken(this), true);
        call.enqueue(new DefaultToastCallback<Result.Data<TopicWithReply>>(this) {

            @Override
            public boolean onResultOk(Response<Result.Data<TopicWithReply>> response, Result.Data<TopicWithReply> result) {
                if (!isFinishing()) {
                    topic = result.getData();
                    headerViewHolder.update(result.getData());
                    replyList.clear();
                    replyList.addAll(result.getData().getReplyList());
                    adapter.notifyDataSetChanged();
                    iconNoData.setVisibility(View.GONE);
                }
                return false;
            }

            @Override
            public void onFinish() {
                if (!isFinishing()) {
                    refreshLayout.setRefreshing(false);
                }
            }

        });
    }

    @Override
    public void onAt(String loginName) {
        replyHandler.edtContent.getText().insert(replyHandler.edtContent.getSelectionEnd(), " @" + loginName + " ");
        replyWindow.showAtLocation(layoutRoot, Gravity.BOTTOM, 0, 0);
    }

    @OnClick(R.id.topic_fab_reply)
    protected void onBtnReplyClick() {
        if (topic != null && LoginActivity.startForResultWithAccessTokenCheck(this)) {
            replyWindow.showAtLocation(layoutRoot, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginActivity.REQUEST_LOGIN && resultCode == RESULT_OK) {
            refreshLayout.setRefreshing(true);
            onRefresh();
        }
    }

    //==============
    // 回复框逻辑处理
    //==============

    protected class ReplyHandler {

        @Bind(R.id.editor_bar_layout_root)
        protected ViewGroup editorBar;

        @Bind(R.id.topic_reply_window_edt_content)
        protected EditText edtContent;

        protected ReplyHandler(View view) {
            ButterKnife.bind(this, view);
            new EditorBarHandler(TopicActivity.this, editorBar, edtContent); // 创建editorBar
        }

        @OnClick(R.id.topic_reply_window_btn_tool_close)
        protected void onBtnToolCloseClick() {
            replyWindow.dismiss();
        }

        @OnClick(R.id.topic_reply_window_btn_tool_send)
        protected void onBtnToolSendClick() {
            if (edtContent.length() == 0) {
                ToastUtils.with(TopicActivity.this).show(R.string.content_empty_error_tip);
            } else {
                String content = edtContent.getText().toString();
                if (SettingShared.isEnableTopicSign(TopicActivity.this)) { // 添加小尾巴
                    content += "\n\n" + SettingShared.getTopicSignContent(TopicActivity.this);
                }
                replyTopicAsyncTask(content);
            }
        }

        private void replyTopicAsyncTask(final String content) {
            progressDialog.show();
            Call<Result.ReplyTopic> call = ApiClient.service.replyTopic(topicId, LoginShared.getAccessToken(TopicActivity.this), content, null);
            call.enqueue(new DefaultToastCallback<Result.ReplyTopic>(TopicActivity.this) {

                @Override
                public boolean onResultOk(Response<Result.ReplyTopic> response, Result.ReplyTopic result) {
                    // 本地创建一个回复对象
                    Reply reply = new Reply();
                    reply.setId(result.getReplyId());
                    Author author = new Author();
                    author.setLoginName(LoginShared.getLoginName(TopicActivity.this));
                    author.setAvatarUrl(LoginShared.getAvatarUrl(TopicActivity.this));
                    reply.setAuthor(author);
                    reply.setContent(content);
                    reply.setHandleContent(FormatUtils.renderMarkdown(content)); // 本地要做预渲染处理
                    reply.setCreateAt(new DateTime());
                    reply.setUpList(new ArrayList<String>());
                    replyList.add(reply);
                    // 更新header和adapter
                    replyWindow.dismiss();
                    headerViewHolder.update(replyList.size());
                    adapter.notifyDataSetChanged();
                    listView.smoothScrollToPosition(replyList.size());
                    // 清空回复框内容
                    edtContent.setText(null);
                    // 提示
                    ToastUtils.with(TopicActivity.this).show(R.string.post_success);
                    // 继续执行onFinish()
                    return false;
                }

                @Override
                public void onFinish() {
                    progressDialog.dismiss();
                }

            });
        }

    }

}
