package org.cnodejs.android.md.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.takwolf.android.hfrecyclerview.HeaderAndFooterRecyclerView;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.entity.Message;
import org.cnodejs.android.md.model.entity.Notification;
import org.cnodejs.android.md.presenter.contract.INotificationPresenter;
import org.cnodejs.android.md.presenter.implement.NotificationPresenter;
import org.cnodejs.android.md.ui.adapter.MessageListAdapter;
import org.cnodejs.android.md.ui.base.StatusBarActivity;
import org.cnodejs.android.md.ui.listener.DoubleClickBackToContentTopListener;
import org.cnodejs.android.md.ui.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.ui.util.ThemeUtils;
import org.cnodejs.android.md.ui.view.IBackToContentTopView;
import org.cnodejs.android.md.ui.view.INotificationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationActivity extends StatusBarActivity implements INotificationView, IBackToContentTopView, Toolbar.OnMenuItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recycler_view)
    HeaderAndFooterRecyclerView recyclerView;

    private MessageListAdapter adapter;

    private INotificationPresenter notificationPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.inflateMenu(R.menu.notification);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setOnClickListener(new DoubleClickBackToContentTopListener(this));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageListAdapter(this);
        recyclerView.setAdapter(adapter);

        notificationPresenter = new NotificationPresenter(this, this);

        refreshLayout.setColorSchemeResources(R.color.color_accent);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done_all:
                notificationPresenter.markAllMessageReadAsyncTask();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onRefresh() {
        notificationPresenter.getMessagesAsyncTask();
    }

    @Override
    public void onGetMessagesOk(@NonNull Notification notification) {
        List<Message> messageList = new ArrayList<>();
        messageList.addAll(notification.getHasNotReadMessageList());
        messageList.addAll(notification.getHasReadMessageList());
        adapter.setMessageListAndNotify(messageList);
    }

    @Override
    public void onGetMessagesFinish() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onMarkAllMessageReadOk() {
        adapter.markAllMessageReadAndNotify();
    }

    @Override
    public void backToContentTop() {
        recyclerView.scrollToPosition(0);
    }

}
