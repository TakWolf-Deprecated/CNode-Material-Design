package org.cnodejs.android.md.logic.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.melnykov.fab.FloatingActionButton;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.entity.Author;
import org.cnodejs.android.md.model.entity.Reply;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.TopicWithReply;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.model.storage.SettingShared;
import org.cnodejs.android.md.logic.adapter.TopicAdapter;
import org.cnodejs.android.md.logic.base.StatusBarActivity;
import org.cnodejs.android.md.logic.dialog.DialogUtils;
import org.cnodejs.android.md.logic.dialog.ProgressDialog;
import org.cnodejs.android.md.logic.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.logic.widget.EditorBarHandler;
import org.cnodejs.android.md.logic.widget.RefreshLayoutUtils;
import org.cnodejs.android.md.logic.widget.ThemeUtils;
import org.cnodejs.android.md.logic.widget.ToastUtils;
import org.cnodejs.android.md.util.FormatUtils;
import org.cnodejs.android.md.util.ShipUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TopicActivity extends StatusBarActivity implements SwipeRefreshLayout.OnRefreshListener, TopicAdapter.OnAtClickListener, Toolbar.OnMenuItemClickListener {

    private static final String EXTRA_TOPIC_ID = "topicId";

    public static void open(Context context, String topicId) {
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

    @Bind(R.id.topic_recycler_view)
    protected RecyclerView recyclerView;

    @Bind(R.id.topic_icon_no_data)
    protected View iconNoData;

    @Bind(R.id.topic_fab_reply)
    protected FloatingActionButton fabReply;

    private PopupWindow replyWindow;
    private ReplyHandler replyHandler;

    private ProgressDialog dialog;

    private String topicId;
    private TopicWithReply topic;

    private TopicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        ButterKnife.bind(this);

        topicId = getIntent().getStringExtra(EXTRA_TOPIC_ID);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.inflateMenu(R.menu.topic);
        toolbar.setOnMenuItemClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TopicAdapter(this, this);
        recyclerView.setAdapter(adapter);

        fabReply.attachToRecyclerView(recyclerView);

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

        dialog = DialogUtils.createProgressDialog(this);
        dialog.setMessage(R.string.posting_$_);
        dialog.setCancelable(false);

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
        ApiClient.service.getTopic(topicId, true, new Callback<Result<TopicWithReply>>() {

            @Override
            public void success(Result<TopicWithReply> result, Response response) {
                if (!isFinishing()) {
                    topic = result.getData();
                    adapter.setTopic(result.getData());
                    adapter.notifyDataSetChanged();
                    iconNoData.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isFinishing()) {
                    if (error.getResponse() != null && error.getResponse().getStatus() == 404) {
                        ToastUtils.with(TopicActivity.this).show(R.string.topic_not_found);
                    } else {
                        ToastUtils.with(TopicActivity.this).show(R.string.data_load_faild);
                    }
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
        if (topic != null) {
            if (TextUtils.isEmpty(LoginShared.getAccessToken(this))) {
                adapter.showNeedLoginDialog();
            } else {
                replyWindow.showAtLocation(layoutRoot, Gravity.BOTTOM, 0, 0);
            }
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

        /**
         * 关闭
         */
        @OnClick(R.id.topic_reply_window_btn_tool_close)
        protected void onBtnToolCloseClick() {
            replyWindow.dismiss();
        }

        /**
         * 发送
         */
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
            dialog.show();
            ApiClient.service.replyTopic(LoginShared.getAccessToken(TopicActivity.this), topicId, content, null, new Callback<Map<String, String>>() {

                @Override
                public void success(Map<String, String> result, Response response) {
                    dialog.dismiss();
                    // 本地创建一个回复对象
                    Reply reply = new Reply();
                    reply.setId(result.get("reply_id"));
                    Author author = new Author();
                    author.setLoginName(LoginShared.getLoginName(TopicActivity.this));
                    author.setAvatarUrl(LoginShared.getAvatarUrl(TopicActivity.this));
                    reply.setAuthor(author);
                    reply.setContent(content);
                    reply.setHandleContent(FormatUtils.renderMarkdown(content)); // 本地要做预渲染处理
                    reply.setCreateAt(new DateTime());
                    reply.setUpList(new ArrayList<String>());
                    topic.getReplyList().add(reply);
                    // 更新adapter并让recyclerView滑动到最底部
                    replyWindow.dismiss();
                    if (topic.getReplyList().size() == 1) { // 需要全刷新
                        adapter.notifyDataSetChanged();
                    } else { // 插入刷新
                        adapter.notifyItemChanged(topic.getReplyList().size() - 1);
                        adapter.notifyItemInserted(topic.getReplyList().size());
                    }
                    recyclerView.smoothScrollToPosition(topic.getReplyList().size());
                    // 清空回复框内容
                    edtContent.setText(null);
                    // 提示
                    ToastUtils.with(TopicActivity.this).show(R.string.post_success);
                }

                @Override
                public void failure(RetrofitError error) {
                    dialog.dismiss();
                    if (error.getResponse() != null && error.getResponse().getStatus() == 403) {
                        adapter.showAccessTokenErrorDialog();
                    } else {
                        ToastUtils.with(TopicActivity.this).show(R.string.network_faild);
                    }
                }

            });
        }

    }

}
