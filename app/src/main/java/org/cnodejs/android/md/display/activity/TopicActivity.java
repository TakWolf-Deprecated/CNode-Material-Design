package org.cnodejs.android.md.display.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.adapter.TopicAdapter;
import org.cnodejs.android.md.display.base.StatusBarActivity;
import org.cnodejs.android.md.display.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.display.view.ITopicView;
import org.cnodejs.android.md.display.viewholder.TopicHeaderViewHolder;
import org.cnodejs.android.md.display.viewholder.TopicReplyViewHolder;
import org.cnodejs.android.md.display.widget.RefreshLayoutUtils;
import org.cnodejs.android.md.display.widget.ThemeUtils;
import org.cnodejs.android.md.model.entity.Reply;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.TopicWithReply;
import org.cnodejs.android.md.model.util.EntityUtils;
import org.cnodejs.android.md.presenter.contract.ITopicPresenter;
import org.cnodejs.android.md.presenter.implement.TopicPresenter;
import org.cnodejs.android.md.util.ShipUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicActivity extends StatusBarActivity implements ITopicView, SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener {

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

    private String topicId;
    private Topic topic;
    private final List<Reply> replyList = new ArrayList<>();

    private TopicReplyViewHolder replyViewHolder;
    private TopicHeaderViewHolder headerViewHolder;
    private TopicAdapter adapter;

    private ITopicPresenter topicPresenter;

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

        replyViewHolder = new TopicReplyViewHolder(this, layoutRoot, topicId, this);
        headerViewHolder = new TopicHeaderViewHolder(this, listView);
        headerViewHolder.update(topic, false, 0);
        adapter = new TopicAdapter(this, replyList, replyViewHolder);
        listView.setAdapter(adapter);

        iconNoData.setVisibility(topic == null ? View.VISIBLE : View.GONE);

        fabReply.attachToListView(listView);

        topicPresenter = new TopicPresenter(this, this);

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
        topicPresenter.getTopicAsyncTask(topicId);
    }

    @OnClick(R.id.topic_fab_reply)
    protected void onBtnReplyClick() {
        if (topic != null && LoginActivity.startForResultWithAccessTokenCheck(this)) {
            replyViewHolder.showReplyWindow();
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

    @Override
    public boolean onGetTopicResultOk(@NonNull Result.Data<TopicWithReply> result) {
        if (!isFinishing()) {
            topic = result.getData();
            headerViewHolder.update(result.getData());
            replyList.clear();
            replyList.addAll(result.getData().getReplyList());
            adapter.notifyDataSetChanged();
            iconNoData.setVisibility(View.GONE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onGetTopicFinish() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void appendReplyAndUpdateViews(@NonNull Reply reply) {
        headerViewHolder.update(replyList.size());
        replyList.add(reply);
        adapter.notifyDataSetChanged();
        listView.smoothScrollToPosition(replyList.size());
    }

}
