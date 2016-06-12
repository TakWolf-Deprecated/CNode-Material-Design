package org.cnodejs.android.md.display.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import org.cnodejs.android.md.display.base.FullLayoutActivity;
import org.cnodejs.android.md.display.dialog.AlertDialogUtils;
import org.cnodejs.android.md.display.listener.DoubleClickBackToContentTopListener;
import org.cnodejs.android.md.display.listener.NavigationOpenClickListener;
import org.cnodejs.android.md.display.listener.RecyclerViewLoadMoreListener;
import org.cnodejs.android.md.display.util.ActivityUtils;
import org.cnodejs.android.md.display.util.DisplayUtils;
import org.cnodejs.android.md.display.util.Navigator;
import org.cnodejs.android.md.display.util.RefreshUtils;
import org.cnodejs.android.md.display.util.ThemeUtils;
import org.cnodejs.android.md.display.util.ToastUtils;
import org.cnodejs.android.md.display.view.IBackToContentTopView;
import org.cnodejs.android.md.display.view.IMainView;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.TabType;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.model.storage.SettingShared;
import org.cnodejs.android.md.presenter.contract.IMainPresenter;
import org.cnodejs.android.md.presenter.implement.MainPresenter;
import org.cnodejs.android.md.util.FormatUtils;
import org.cnodejs.android.md.util.HandlerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends FullLayoutActivity implements IMainView, IBackToContentTopView, SwipeRefreshLayout.OnRefreshListener, RecyclerViewLoadMoreListener.OnLoadMoreListener {

    private static final int PAGE_LIMIT = 20;

    // 抽屉导航布局
    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;

    // 状态栏
    @BindView(R.id.center_adapt_status_bar)
    protected View centerAdaptStatusBar;

    @BindView(R.id.nav_adapt_status_bar)
    protected View navAdaptStatusBar;

    // 导航部分的个人信息
    @BindView(R.id.img_avatar)
    protected ImageView imgAvatar;

    @BindView(R.id.tv_login_name)
    protected TextView tvLoginName;

    @BindView(R.id.tv_score)
    protected TextView tvScore;

    @BindView(R.id.badge_nav_notification)
    protected TextView tvBadgeNotification;

    @BindView(R.id.btn_logout)
    protected View btnLogout;

    @BindView(R.id.btn_theme_dark)
    protected ImageView imgThemeDark;

    @BindView(R.id.img_nav_top_background)
    protected ImageView imgTopBackground;

    // 主要导航项
    @BindViews({
            R.id.btn_nav_all,
            R.id.btn_nav_good,
            R.id.btn_nav_share,
            R.id.btn_nav_ask,
            R.id.btn_nav_job
    })
    protected List<CheckedTextView> navMainItemList;

    // 内容部分
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.refresh_layout)
    protected SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recycler_view)
    protected RecyclerView recyclerView;

    @BindView(R.id.icon_no_data)
    protected View iconNoData;

    @BindView(R.id.fab_create_topic)
    protected FloatingActionButton fabCreateTopic;

    // 当前版块，默认为all
    private TabType currentTab = TabType.all;
    private int currentPage = 0; // 从未加载
    private final List<Topic> topicList = new ArrayList<>();
    private MainAdapter adapter;

    private IMainPresenter mainPresenter;

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

        DisplayUtils.adaptStatusBar(this, centerAdaptStatusBar);
        DisplayUtils.adaptStatusBar(this, navAdaptStatusBar);

        drawerLayout.setDrawerShadow(R.drawable.navigation_drawer_shadow, GravityCompat.START);
        drawerLayout.addDrawerListener(drawerListener);
        toolbar.setNavigationOnClickListener(new NavigationOpenClickListener(drawerLayout));
        toolbar.setOnClickListener(new DoubleClickBackToContentTopListener(this));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MainAdapter(this, topicList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerViewLoadMoreListener(linearLayoutManager, this, PAGE_LIMIT));
        fabCreateTopic.attachToRecyclerView(recyclerView);

        mainPresenter = new MainPresenter(this, this);

        updateUserInfoViews();

        imgThemeDark.setImageResource(enableThemeDark ? R.drawable.ic_wb_sunny_white_24dp : R.drawable.ic_brightness_3_white_24dp);
        imgTopBackground.setVisibility(enableThemeDark ? View.INVISIBLE : View.VISIBLE);

        RefreshUtils.initOnCreate(refreshLayout, this);
        RefreshUtils.refreshOnCreate(refreshLayout, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainPresenter.getMessageCountAsyncTask();
        // 判断是否需要切换主题
        if (SettingShared.isEnableThemeDark(this) != enableThemeDark) {
            ThemeUtils.notifyThemeApply(this, true);
        }
    }

    private final DrawerLayout.DrawerListener drawerListener = new DrawerLayout.SimpleDrawerListener() {

        @Override
        public void onDrawerOpened(View drawerView) {
            updateUserInfoViews();
            mainPresenter.getUserAsyncTask();
            mainPresenter.getMessageCountAsyncTask();
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            TabType newTab = TabType.all;
            for (CheckedTextView navItem : navMainItemList) {
                if (navItem.isChecked()) {
                    switch (navItem.getId()) {
                        case R.id.btn_nav_all:
                            newTab = TabType.all;
                            break;
                        case R.id.btn_nav_good:
                            newTab = TabType.good;
                            break;
                        case R.id.btn_nav_share:
                            newTab = TabType.share;
                            break;
                        case R.id.btn_nav_ask:
                            newTab = TabType.ask;
                            break;
                        case R.id.btn_nav_job:
                            newTab = TabType.job;
                            break;
                        default:
                            newTab = TabType.all;
                            break;
                    }
                    break;
                }
            }
            if (newTab != currentTab) {
                currentTab = newTab;
                currentPage = 0;
                toolbar.setTitle(currentTab.getNameId());
                topicList.clear();
                notifyDataSetChanged();
                refreshLayout.setRefreshing(true);
                onRefresh();
                fabCreateTopic.show(true);
            }
        }

    };

    @Override
    public void onRefresh() {
        mainPresenter.refreshTopicListAsyncTask(currentTab, PAGE_LIMIT);
    }

    @Override
    public void onLoadMore() {
        if (adapter.canLoadMore()) {
            adapter.setLoading(true);
            adapter.notifyItemChanged(adapter.getItemCount() - 1);
            mainPresenter.loadMoreTopicListAsyncTask(currentTab, currentPage, PAGE_LIMIT);
        }
    }

    /**
     * 更新列表
     */
    private void notifyDataSetChanged() {
        if (topicList.size() < PAGE_LIMIT) {
            adapter.setLoading(false);
        }
        adapter.notifyDataSetChanged();
        iconNoData.setVisibility(topicList.size() == 0 ? View.VISIBLE : View.GONE);
    }

    /**
     * 主导航项单击事件
     */
    @OnClick({
            R.id.btn_nav_all,
            R.id.btn_nav_good,
            R.id.btn_nav_share,
            R.id.btn_nav_ask,
            R.id.btn_nav_job
    })
    public void onNavigationMainItemClick(CheckedTextView itemView) {
        for (CheckedTextView navItem : navMainItemList) {
            navItem.setChecked(navItem.getId() == itemView.getId());
        }
        drawerLayout.closeDrawers();
    }

    /**
     * 次要菜单导航
     */

    @OnClick({
            R.id.btn_nav_notification,
            R.id.btn_nav_setting,
            R.id.btn_nav_about
    })
    public void onNavigationItemOtherClick(View itemView) {
        switch (itemView.getId()) {
            case R.id.btn_nav_notification:
                if (LoginActivity.startForResultWithAccessTokenCheck(this)) {
                    notificationAction.startDelayed();
                    drawerLayout.closeDrawers();
                }
                break;
            case R.id.btn_nav_setting:
                settingAction.startDelayed();
                drawerLayout.closeDrawers();
                break;
            case R.id.btn_nav_about:
                aboutAction.startDelayed();
                drawerLayout.closeDrawers();
                break;
        }
    }

    private class OtherItemAction implements Runnable {

        private Class gotoClz;

        protected OtherItemAction(Class gotoClz) {
            this.gotoClz = gotoClz;
        }

        @Override
        public void run() {
            if (gotoClz == NotificationActivity.class) {
                Navigator.NotificationWithAutoCompat.start(MainActivity.this);
            } else {
                startActivity(new Intent(MainActivity.this, gotoClz));
            }
        }

        public void startDelayed() {
            HandlerUtils.postDelayed(this, 400);
        }

    }

    private OtherItemAction notificationAction = new OtherItemAction(NotificationActivity.class);
    private OtherItemAction settingAction = new OtherItemAction(SettingActivity.class);
    private OtherItemAction aboutAction = new OtherItemAction(AboutActivity.class);

    /**
     * 注销按钮
     */
    @OnClick(R.id.btn_logout)
    protected void onBtnLogoutClick() {
        AlertDialogUtils.createBuilderWithAutoTheme(this)
                .setMessage(R.string.logout_tip)
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginShared.logout(MainActivity.this);
                        tvBadgeNotification.setText(null); // 未读消息清空
                        updateUserInfoViews();
                    }

                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    /**
     * 主题按钮
     */
    @OnClick(R.id.btn_theme_dark)
    protected void onBtnThemeDarkClick() {
        SettingShared.setEnableThemeDark(this, !enableThemeDark);
        ThemeUtils.notifyThemeApply(this, false);
    }

    /**
     * 用户信息按钮
     */
    @OnClick(R.id.layout_info)
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
    @OnClick(R.id.fab_create_topic)
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
            mainPresenter.getUserAsyncTask();
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

    @Override
    public boolean onRefreshTopicListResultOk(@NonNull TabType tab, @NonNull Result.Data<List<Topic>> result) {
        if (currentTab == tab) {
            topicList.clear();
            topicList.addAll(result.getData());
            notifyDataSetChanged();
            currentPage = 1;
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onRefreshTopicListResultErrorOrCallException(@NonNull TabType tab, @NonNull Result.Error error) {
        if (currentTab == tab) {
            ToastUtils.with(this).show(error.getErrorMessage());
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRefreshTopicListFinish() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onLoadMoreTopicListResultOk(@NonNull TabType tab, @NonNull Integer page, Result.Data<List<Topic>> result) {
        if (currentTab == tab && currentPage == page) {
            if (result.getData().size() > 0) {
                topicList.addAll(result.getData());
                adapter.setLoading(false);
                adapter.notifyItemRangeInserted(topicList.size() - result.getData().size(), result.getData().size());
                currentPage++;
                return true;
            } else {
                ToastUtils.with(this).show(R.string.have_no_more_data);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public boolean onLoadMoreTopicListResultErrorOrCallException(@NonNull TabType tab, @NonNull Integer page, @NonNull Result.Error error) {
        if (currentTab == tab && currentPage == page) {
            ToastUtils.with(this).show(error.getErrorMessage());
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onLoadMoreTopicListFinish() {
        adapter.setLoading(false);
        adapter.notifyItemChanged(adapter.getItemCount() - 1);
    }

    @Override
    public void updateUserInfoViews() {
        if (ActivityUtils.isAlive(this)) {
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
    }

    @Override
    public void updateMessageCountViews(@NonNull Result.Data<Integer> result) {
        if (ActivityUtils.isAlive(this)) {
            tvBadgeNotification.setText(FormatUtils.getNavigationDisplayCountString(result.getData()));
        }
    }

    @Override
    public void backToContentTop() {
        recyclerView.scrollToPosition(0);
    }

}
