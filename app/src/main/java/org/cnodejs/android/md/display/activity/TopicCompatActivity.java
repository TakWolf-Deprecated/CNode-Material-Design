package org.cnodejs.android.md.display.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.melnykov.fab.FloatingActionButton;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.base.StatusBarActivity;
import org.cnodejs.android.md.display.dialog.TopicReplyDialog;
import org.cnodejs.android.md.display.listener.DoubleClickBackToContentTopListener;
import org.cnodejs.android.md.display.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.display.listener.TopicJavascriptInterface;
import org.cnodejs.android.md.display.util.ActivityUtils;
import org.cnodejs.android.md.display.util.Navigator;
import org.cnodejs.android.md.display.util.RefreshUtils;
import org.cnodejs.android.md.display.util.ThemeUtils;
import org.cnodejs.android.md.display.view.ITopicHeaderView;
import org.cnodejs.android.md.display.view.ITopicItemReplyView;
import org.cnodejs.android.md.display.view.ITopicReplyView;
import org.cnodejs.android.md.display.view.ITopicView;
import org.cnodejs.android.md.display.widget.TopicWebView;
import org.cnodejs.android.md.model.api.ApiDefine;
import org.cnodejs.android.md.model.entity.Reply;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.TopicWithReply;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.presenter.contract.ITopicHeaderPresenter;
import org.cnodejs.android.md.presenter.contract.ITopicItemReplyPresenter;
import org.cnodejs.android.md.presenter.contract.ITopicPresenter;
import org.cnodejs.android.md.presenter.implement.TopicHeaderPresenter;
import org.cnodejs.android.md.presenter.implement.TopicItemReplyPresenter;
import org.cnodejs.android.md.presenter.implement.TopicPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicCompatActivity extends StatusBarActivity implements ITopicView, ITopicHeaderView, ITopicItemReplyView, SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.refresh_layout)
    protected SwipeRefreshLayout refreshLayout;

    @BindView(R.id.web_topic)
    protected TopicWebView webTopic;

    @BindView(R.id.icon_no_data)
    protected View iconNoData;

    @BindView(R.id.fab_reply)
    protected FloatingActionButton fabReply;

    private String topicId;
    private Topic topic;

    private ITopicReplyView topicReplyView;

    private ITopicPresenter topicPresenter;
    private ITopicHeaderPresenter topicHeaderPresenter;
    private ITopicItemReplyPresenter topicItemReplyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_compat);
        ButterKnife.bind(this);

        topicId = getIntent().getStringExtra(Navigator.TopicWithAutoCompat.EXTRA_TOPIC_ID);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.inflateMenu(R.menu.topic);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setOnClickListener(new DoubleClickBackToContentTopListener(webTopic));

        topicPresenter = new TopicPresenter(this, this);
        topicHeaderPresenter = new TopicHeaderPresenter(this, this);
        topicItemReplyPresenter = new TopicItemReplyPresenter(this, this);

        topicReplyView = TopicReplyDialog.createWithAutoTheme(this, topicId, this);

        webTopic.setFabReply(fabReply);
        webTopic.setBridgeAndLoadPage(new TopicJavascriptInterface(this, topicReplyView, topicHeaderPresenter, topicItemReplyPresenter));

        RefreshUtils.initOnCreate(refreshLayout, this);
        RefreshUtils.refreshOnCreate(refreshLayout, this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                if (topic != null) {
                    Navigator.openShare(this, "《" + topic.getTitle() + "》\n" + ApiDefine.TOPIC_LINK_URL_PREFIX + topicId + "\n—— 来自CNode社区");
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onRefresh() {
        topicPresenter.getTopicAsyncTask(topicId);
    }

    @OnClick(R.id.fab_reply)
    protected void onBtnReplyClick() {
        if (topic != null && LoginActivity.startForResultWithAccessTokenCheck(this)) {
            topicReplyView.showReplyWindow();
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
        if (ActivityUtils.isAlive(this)) {
            topic = result.getData();
            webTopic.updateTopicAndUserId(result.getData(), LoginShared.getId(this));
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
        webTopic.appendReply(reply);
    }

    @Override
    public boolean onCollectTopicResultOk(Result result) {
        if (ActivityUtils.isAlive(this)) {
            webTopic.updateTopicCollect(true);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onDecollectTopicResultOk(Result result) {
        if (ActivityUtils.isAlive(this)) {
            webTopic.updateTopicCollect(false);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onUpReplyResultOk(@NonNull Reply reply) {
        if (ActivityUtils.isAlive(this)) {
            webTopic.updateReply(reply);
            return false;
        } else {
            return true;
        }
    }

}
