package org.cnodejs.android.md.display.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.base.StatusBarActivity;
import org.cnodejs.android.md.display.listener.DoubleClickBackToContentTopListener;
import org.cnodejs.android.md.display.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.display.util.ActivityUtils;
import org.cnodejs.android.md.display.util.RefreshUtils;
import org.cnodejs.android.md.display.util.ThemeUtils;
import org.cnodejs.android.md.display.view.INotificationView;
import org.cnodejs.android.md.display.widget.NotificationWebView;
import org.cnodejs.android.md.model.entity.Message;
import org.cnodejs.android.md.model.entity.Notification;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.presenter.contract.INotificationPresenter;
import org.cnodejs.android.md.presenter.implement.NotificationPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationCompatActivity extends StatusBarActivity implements INotificationView, Toolbar.OnMenuItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.refresh_layout)
    protected SwipeRefreshLayout refreshLayout;

    @BindView(R.id.web_notification)
    protected NotificationWebView webNotification;

    @BindView(R.id.icon_no_data)
    protected View iconNoData;

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

        RefreshUtils.initOnCreate(refreshLayout, this);
        RefreshUtils.refreshOnCreate(refreshLayout, this);
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
    public boolean onGetMessagesResultOk(@NonNull Result.Data<Notification> result) {
        if (ActivityUtils.isAlive(this)) {
            List<Message> messageList = new ArrayList<>();
            messageList.addAll(result.getData().getHasNotReadMessageList());
            messageList.addAll(result.getData().getHasReadMessageList());
            webNotification.updateMessageList(messageList);
            iconNoData.setVisibility(messageList.size() == 0 ? View.VISIBLE : View.GONE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onGetMessagesFinish() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onMarkAllMessageReadResultOk() {
        if (ActivityUtils.isAlive(this)) {
            webNotification.markAllMessageRead();
            return false;
        } else {
            return true;
        }
    }

}
