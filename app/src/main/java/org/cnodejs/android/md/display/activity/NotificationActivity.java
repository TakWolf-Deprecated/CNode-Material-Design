package org.cnodejs.android.md.display.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.adapter.NotificationAdapter;
import org.cnodejs.android.md.display.base.StatusBarActivity;
import org.cnodejs.android.md.display.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.display.widget.RefreshLayoutUtils;
import org.cnodejs.android.md.display.widget.ThemeUtils;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.DefaultToastCallback;
import org.cnodejs.android.md.model.entity.Message;
import org.cnodejs.android.md.model.entity.Notification;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.storage.LoginShared;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class NotificationActivity extends StatusBarActivity implements Toolbar.OnMenuItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.notification_toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.notification_refresh_layout)
    protected SwipeRefreshLayout refreshLayout;

    @Bind(R.id.notification_recycler_view)
    protected RecyclerView recyclerView;

    @Bind(R.id.notification_icon_no_data)
    protected View iconNoData;

    private NotificationAdapter adapter;
    private List<Message> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.inflateMenu(R.menu.notification);
        toolbar.setOnMenuItemClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(this, messageList);
        recyclerView.setAdapter(adapter);

        RefreshLayoutUtils.initOnCreate(refreshLayout, this);
        RefreshLayoutUtils.refreshOnCreate(refreshLayout, this);
    }

    @Override
    public void onRefresh() {
        Call<Result.Data<Notification>> call = ApiClient.service.getMessages(LoginShared.getAccessToken(this), true);
        call.enqueue(new DefaultToastCallback<Result.Data<Notification>>(this) {

            @Override
            public boolean onResultOk(Response<Result.Data<Notification>> response, Result.Data<Notification> result) {
                if (!isFinishing()) {
                    messageList.clear();
                    messageList.addAll(result.getData().getHasNotReadMessageList());
                    messageList.addAll(result.getData().getHasReadMessageList());
                    notifyDataSetChanged();
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

    private void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
        iconNoData.setVisibility(messageList.size() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done_all:
                markAllMessageReadAsyncTask();
                return true;
            default:
                return false;
        }
    }

    private void markAllMessageReadAsyncTask() {
        Call<Result> call = ApiClient.service.markAllMessageRead(LoginShared.getAccessToken(this));
        call.enqueue(new DefaultToastCallback<Result>(this) {

            @Override
            public boolean onResultOk(Response<Result> response, Result result) {
                if (!isFinishing()) {
                    for (Message message : messageList) {
                        message.setRead(true);
                    }
                    notifyDataSetChanged();
                }
                return false;
            }

        });
    }

}
