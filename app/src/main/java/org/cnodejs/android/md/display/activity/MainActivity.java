package org.cnodejs.android.md.display.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.melnykov.fab.FloatingActionButton;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.adapter.MainAdapter;
import org.cnodejs.android.md.display.base.DrawerLayoutActivity;
import org.cnodejs.android.md.display.dialog.DialogUtils;
import org.cnodejs.android.md.display.listener.NavigationOpenClickListener;
import org.cnodejs.android.md.display.listener.RecyclerViewLoadMoreListener;
import org.cnodejs.android.md.display.widget.RefreshLayoutUtils;
import org.cnodejs.android.md.display.widget.ThemeUtils;
import org.cnodejs.android.md.display.widget.ToastUtils;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.CallbackAdapter;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.TabType;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.User;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.model.storage.SettingShared;
import org.cnodejs.android.md.util.FormatUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends DrawerLayoutActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerViewLoadMoreListener.OnLoadMoreListener {

    // 抽屉导航布局
    @Bind(R.id.main_drawer_layout)
    protected DrawerLayout drawerLayout;

    // 状态栏
    @Bind(R.id.main_center_adapt_status_bar)
    protected View centerAdaptStatusBar;

    @Bind(R.id.main_nav_adapt_status_bar)
    protected View navAdaptStatusBar;

    // 导航部分的个人信息
    @Bind(R.id.main_nav_img_avatar)
    protected ImageView imgAvatar;

    @Bind(R.id.main_nav_tv_login_name)
    protected TextView tvLoginName;

    @Bind(R.id.main_nav_tv_score)
    protected TextView tvScore;

    @Bind(R.id.main_nav_tv_badger_notification)
    protected TextView tvBadgerNotification;

    @Bind(R.id.main_nav_btn_logout)
    protected View btnLogout;

    @Bind(R.id.main_nav_btn_theme_dark)
    protected ImageView imgThemeDark;

    @Bind(R.id.main_nav_img_top_background)
    protected ImageView imgTopBackground;

    // 主要导航项
    @Bind({
            R.id.main_nav_btn_all,
            R.id.main_nav_btn_good,
            R.id.main_nav_btn_share,
            R.id.main_nav_btn_ask,
            R.id.main_nav_btn_job
    })
    protected List<CheckedTextView> navMainItemList;

    // 内容部分
    @Bind(R.id.main_toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.main_refresh_layout)
    protected SwipeRefreshLayout refreshLayout;

    @Bind(R.id.main_recycler_view)
    protected RecyclerView recyclerView;

    @Bind(R.id.main_icon_no_data)
    protected View iconNoData;

    @Bind(R.id.main_fab_create_topic)
    protected FloatingActionButton fabCreateTopic;

    // 当前版块，默认为all
    private TabType currentTab = TabType.all;
    private int currentPage = 0; // 从未加载
    private final List<Topic> topicList = new ArrayList<>();
    private MainAdapter adapter;

    // 首次按下返回键时间戳
    private long firstBackPressedTime = 0;

    // 是否启用夜间模式
    private boolean enableThemeDark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableThemeDark = ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight_FitsStatusBar, R.style.AppThemeDark_FitsStatusBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        adaptStatusBar(centerAdaptStatusBar);
        adaptStatusBar(navAdaptStatusBar);

        drawerLayout.setDrawerShadow(R.drawable.navigation_drawer_shadow, GravityCompat.START);
        drawerLayout.setDrawerListener(openDrawerListener);
        toolbar.setNavigationOnClickListener(new NavigationOpenClickListener(drawerLayout));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MainAdapter(this, topicList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerViewLoadMoreListener(linearLayoutManager, this, 20));
        fabCreateTopic.attachToRecyclerView(recyclerView);

        updateUserInfoViews();

        imgThemeDark.setImageResource(enableThemeDark ? R.drawable.ic_wb_sunny_white_24dp : R.drawable.ic_brightness_3_white_24dp);
        imgTopBackground.setVisibility(enableThemeDark ? View.INVISIBLE : View.VISIBLE);

        RefreshLayoutUtils.initOnCreate(refreshLayout, this);
        RefreshLayoutUtils.refreshOnCreate(refreshLayout, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMessageCountAsyncTask();
        // 判断是否需要切换主题
        if (SettingShared.isEnableThemeDark(this) != enableThemeDark) {
            ThemeUtils.recreateActivity(this);
        }
    }

    /**
     * 未读消息数目
     */
    private void getMessageCountAsyncTask() {
        final String accessToken = LoginShared.getAccessToken(this);
        if (!TextUtils.isEmpty(accessToken)) {
            Call<Result.Data<Integer>> call =  ApiClient.service.getMessageCount(accessToken);
            call.enqueue(new CallbackAdapter<Result.Data<Integer>>() {

                @Override
                public boolean onResultOk(Response<Result.Data<Integer>> response, Result.Data<Integer> result) {
                    if (TextUtils.equals(accessToken, LoginShared.getAccessToken(MainActivity.this))) {
                        tvBadgerNotification.setText(FormatUtils.getNavigationDisplayCountText(result.getData()));
                    }
                    return false;
                }

            });
        }
    }

    /**
     * 用户信息更新逻辑
     */

    private DrawerLayout.DrawerListener openDrawerListener = new DrawerLayout.SimpleDrawerListener() {

        @Override
        public void onDrawerOpened(View drawerView) {
            updateUserInfoViews();
            getUserAsyncTask();
            getMessageCountAsyncTask();
        }

    };

    private void updateUserInfoViews() {
        if (TextUtils.isEmpty(LoginShared.getAccessToken(this))) {
            Glide.with(this).load(R.drawable.image_placeholder).placeholder(R.drawable.image_placeholder).dontAnimate().into(imgAvatar);
            tvLoginName.setText(R.string.click_avatar_to_login);
            tvScore.setText(null);
            btnLogout.setVisibility(View.GONE);
        } else {
            Glide.with(this).load(LoginShared.getAvatarUrl(this)).placeholder(R.drawable.image_placeholder).dontAnimate().into(imgAvatar);
            tvLoginName.setText(LoginShared.getLoginName(this));
            tvScore.setText(getString(R.string.score_$) + LoginShared.getScore(this));
            btnLogout.setVisibility(View.VISIBLE);
        }
    }

    private void getUserAsyncTask() {
        final String accessToken = LoginShared.getAccessToken(this);
        if (!TextUtils.isEmpty(accessToken)) {
            Call<Result.Data<User>> call = ApiClient.service.getUser(LoginShared.getLoginName(this));
            call.enqueue(new CallbackAdapter<Result.Data<User>>() {

                @Override
                public boolean onResultOk(Response<Result.Data<User>> response, Result.Data<User> result) {
                    if (TextUtils.equals(accessToken, LoginShared.getAccessToken(MainActivity.this))) {
                        LoginShared.update(MainActivity.this, result.getData());
                        updateUserInfoViews();
                    }
                    return false;
                }

            });
        }
    }

    /**
     * 刷新和加载逻辑
     */
    @Override
    public void onRefresh() {
        final TabType tab = currentTab;
        Call<Result.Data<List<Topic>>> call = ApiClient.service.getTopicList(tab, 1, 20, true);
        call.enqueue(new CallbackAdapter<Result.Data<List<Topic>>>() {

            @Override
            public boolean onResultOk(Response<Result.Data<List<Topic>>> response, Result.Data<List<Topic>> result) {
                if (currentTab == tab) {
                    topicList.clear();
                    topicList.addAll(result.getData());
                    notifyDataSetChanged();
                    currentPage = 1;
                }
                return false;
            }

            @Override
            public boolean onResultError(Response<Result.Data<List<Topic>>> response, Result.Error error) {
                if (currentTab == tab) {
                    ToastUtils.with(MainActivity.this).show(error.getErrorMessage());
                }
                return false;
            }

            @Override
            public boolean onCallException(Throwable t, Result.Error error) {
                if (currentTab == tab) {
                    ToastUtils.with(MainActivity.this).show(error.getErrorMessage());
                }
                return false;
            }

            @Override
            public void onFinish() {
                if (currentTab == tab) {
                    refreshLayout.setRefreshing(false);
                }
            }

        });
    }

    @Override
    public void onLoadMore() {
        if (adapter.canLoadMore()) {
            adapter.setLoading(true);
            adapter.notifyItemChanged(adapter.getItemCount() - 1);

            final TabType tab = currentTab;
            final int page = currentPage;
            Call<Result.Data<List<Topic>>> call = ApiClient.service.getTopicList(tab, page + 1, 20, true);
            call.enqueue(new CallbackAdapter<Result.Data<List<Topic>>>() {

                @Override
                public boolean onResultOk(Response<Result.Data<List<Topic>>> response, Result.Data<List<Topic>> result) {
                    if (currentTab == tab && currentPage == page) {
                        if (result.getData().size() > 0) {
                            topicList.addAll(result.getData());
                            adapter.setLoading(false);
                            adapter.notifyItemRangeInserted(topicList.size() - result.getData().size(), result.getData().size());
                            currentPage++;
                            return true;
                        } else {
                            ToastUtils.with(MainActivity.this).show(R.string.have_no_more_data);
                            return false;
                        }
                    }
                    return false;
                }

                @Override
                public boolean onResultError(Response<Result.Data<List<Topic>>> response, Result.Error error) {
                    if (currentTab == tab && currentPage == page) {
                        ToastUtils.with(MainActivity.this).show(error.getErrorMessage());
                    }
                    return false;
                }

                @Override
                public boolean onCallException(Throwable t, Result.Error error) {
                    if (currentTab == tab && currentPage == page) {
                        ToastUtils.with(MainActivity.this).show(error.getErrorMessage());
                    }
                    return false;
                }

                @Override
                public void onFinish() {
                    if (currentTab == tab && currentPage == page) {
                        adapter.setLoading(false);
                        adapter.notifyItemChanged(adapter.getItemCount() - 1);
                    }
                }

            });
        }
    }

    /**
     * 更新列表
     */
    private void notifyDataSetChanged() {
        if (topicList.size() < 20) {
            adapter.setLoading(false);
        }
        adapter.notifyDataSetChanged();
        iconNoData.setVisibility(topicList.size() == 0 ? View.VISIBLE : View.GONE);
    }

    /**
     * 主导航项单击事件
     */
    @OnClick({
            R.id.main_nav_btn_all,
            R.id.main_nav_btn_good,
            R.id.main_nav_btn_share,
            R.id.main_nav_btn_ask,
            R.id.main_nav_btn_job
    })
    public void onNavigationMainItemClick(CheckedTextView itemView) {
        switch (itemView.getId()) {
            case R.id.main_nav_btn_all:
                drawerLayout.setDrawerListener(tabAllDrawerListener);
                break;
            case R.id.main_nav_btn_good:
                drawerLayout.setDrawerListener(tabGoodDrawerListener);
                break;
            case R.id.main_nav_btn_share:
                drawerLayout.setDrawerListener(tabShareDrawerListener);
                break;
            case R.id.main_nav_btn_ask:
                drawerLayout.setDrawerListener(tabAskDrawerListener);
                break;
            case R.id.main_nav_btn_job:
                drawerLayout.setDrawerListener(tabJobDrawerListener);
                break;
            default:
                drawerLayout.setDrawerListener(openDrawerListener);
                break;
        }
        for (CheckedTextView navItem : navMainItemList) {
            navItem.setChecked(navItem.getId() == itemView.getId());
        }
        drawerLayout.closeDrawers();
    }

    private class MainItemDrawerListener extends DrawerLayout.SimpleDrawerListener {

        private TabType tabType;

        protected MainItemDrawerListener(TabType tabType) {
            this.tabType = tabType;
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            if (tabType != currentTab) {
                currentTab = tabType;
                currentPage = 0;
                toolbar.setTitle(currentTab.getNameId());
                topicList.clear();
                notifyDataSetChanged();
                refreshLayout.setRefreshing(true);
                onRefresh();
                fabCreateTopic.show(true);
            }
            drawerLayout.setDrawerListener(openDrawerListener);
        }

    }

    private DrawerLayout.DrawerListener tabAllDrawerListener = new MainItemDrawerListener(TabType.all);
    private DrawerLayout.DrawerListener tabGoodDrawerListener = new MainItemDrawerListener(TabType.good);
    private DrawerLayout.DrawerListener tabShareDrawerListener = new MainItemDrawerListener(TabType.share);
    private DrawerLayout.DrawerListener tabAskDrawerListener = new MainItemDrawerListener(TabType.ask);
    private DrawerLayout.DrawerListener tabJobDrawerListener = new MainItemDrawerListener(TabType.job);

    /**
     * 次要菜单导航
     */

    @OnClick({
            R.id.main_nav_btn_notification,
            R.id.main_nav_btn_setting,
            R.id.main_nav_btn_about
    })
    public void onNavigationItemOtherClick(View itemView) {
        switch (itemView.getId()) {
            case R.id.main_nav_btn_notification:
                if (LoginActivity.startForResultWithAccessTokenCheck(this)) {
                    drawerLayout.setDrawerListener(notificationDrawerListener);
                    drawerLayout.closeDrawers();
                }
                break;
            case R.id.main_nav_btn_setting:
                drawerLayout.setDrawerListener(settingDrawerListener);
                drawerLayout.closeDrawers();
                break;
            case R.id.main_nav_btn_about:
                drawerLayout.setDrawerListener(aboutDrawerListener);
                drawerLayout.closeDrawers();
                break;
            default:
                drawerLayout.setDrawerListener(openDrawerListener);
                drawerLayout.closeDrawers();
                break;
        }
    }

    private class OtherItemDrawerListener extends DrawerLayout.SimpleDrawerListener {

        private Class gotoClz;

        protected OtherItemDrawerListener(Class gotoClz) {
            this.gotoClz = gotoClz;
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            startActivity(new Intent(MainActivity.this, gotoClz));
            drawerLayout.setDrawerListener(openDrawerListener);
        }

    }

    private DrawerLayout.DrawerListener notificationDrawerListener = new OtherItemDrawerListener(NotificationActivity.class);
    private DrawerLayout.DrawerListener settingDrawerListener = new OtherItemDrawerListener(SettingActivity.class);
    private DrawerLayout.DrawerListener aboutDrawerListener = new OtherItemDrawerListener(AboutActivity.class);

    /**
     * 注销按钮
     */
    @OnClick(R.id.main_nav_btn_logout)
    protected void onBtnLogoutClick() {
        DialogUtils.createAlertDialogBuilder(this)
                .setMessage(R.string.logout_tip)
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginShared.logout(MainActivity.this);
                        tvBadgerNotification.setText(null); // 未读消息清空
                        updateUserInfoViews();
                    }

                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    /**
     * 主题按钮
     */
    @OnClick(R.id.main_nav_btn_theme_dark)
    protected void onBtnThemeDarkClick() {
        SettingShared.setEnableThemeDark(this, !enableThemeDark);
        ThemeUtils.recreateActivity(this); // 重启Activity
    }

    /**
     * 用户信息按钮
     */
    @OnClick(R.id.main_nav_layout_info)
    protected void onBtnInfoClick() {
        if (TextUtils.isEmpty(LoginShared.getAccessToken(this))) {
            LoginActivity.startForResult(this);
        } else {
            UserDetailActivity.startWithTransitionAnimation(this, LoginShared.getLoginName(this), imgAvatar, LoginShared.getAvatarUrl(this));
        }
    }

    /**
     * 发帖按钮
     */
    @OnClick(R.id.main_fab_create_topic)
    protected void onBtnCreateTopicClick() {
        if (LoginActivity.startForResultWithAccessTokenCheck(this)) {
            startActivity(new Intent(this, CreateTopicActivity.class));
        }
    }

    /**
     * 判断登录是否成功
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginActivity.REQUEST_LOGIN && resultCode == RESULT_OK) {
            updateUserInfoViews();
            getUserAsyncTask();
        }
    }

    /**
     * 返回键关闭导航
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            long secondBackPressedTime = System.currentTimeMillis();
            if (secondBackPressedTime - firstBackPressedTime > 2000) {
                ToastUtils.with(this).show(R.string.press_back_again_to_exit);
                firstBackPressedTime = secondBackPressedTime;
            } else {
                super.onBackPressed();
            }
        }
    }

}
