package org.cnodejs.android.md.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.entity.Message;
import org.cnodejs.android.md.model.entity.Notification;
import org.cnodejs.android.md.presenter.contract.INotificationPresenter;
import org.cnodejs.android.md.presenter.implement.NotificationPresenter;
import org.cnodejs.android.md.ui.base.StatusBarActivity;
import org.cnodejs.android.md.ui.listener.DoubleClickBackToContentTopListener;
import org.cnodejs.android.md.ui.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.ui.util.ThemeUtils;
import org.cnodejs.android.md.ui.view.INotificationView;
import org.cnodejs.android.md.ui.widget.NotificationWebView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationCompatActivity extends StatusBarActivity implements INotificationView, Toolbar.OnMenuItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.web_notification)
    NotificationWebView webNotification;

    @BindView(R.id.icon_no_data)
    View iconNoData;

    private INotificationPresenter notificationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_compat);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.inflateMenu(R.menu.notification);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setOnClickListener(new DoubleClickBackToContentTopListener(webNotification));

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
        webNotification.updateMessageList(messageList);
        iconNoData.setVisibility(messageList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onGetMessagesFinish() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onMarkAllMessageReadOk() {
        webNotification.markAllMessageRead();
    }

}
