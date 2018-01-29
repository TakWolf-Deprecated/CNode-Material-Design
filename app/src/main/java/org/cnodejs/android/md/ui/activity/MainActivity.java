package org.cnodejs.android.md.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.takwolf.android.hfrecyclerview.HeaderAndFooterRecyclerView;

import org.cnodejs.android.md.BuildConfig;
import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.entity.Tab;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.glide.GlideApp;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.model.storage.SettingShared;
import org.cnodejs.android.md.presenter.contract.IMainPresenter;
import org.cnodejs.android.md.presenter.implement.MainPresenter;
import org.cnodejs.android.md.ui.adapter.TopicListAdapter;
import org.cnodejs.android.md.ui.base.FullLayoutActivity;
import org.cnodejs.android.md.ui.dialog.AlertDialogUtils;
import org.cnodejs.android.md.ui.listener.DoubleClickBackToContentTopListener;
import org.cnodejs.android.md.ui.listener.FloatingActionButtonBehaviorListener;
import org.cnodejs.android.md.ui.listener.NavigationOpenClickListener;
import org.cnodejs.android.md.ui.util.Navigator;
import org.cnodejs.android.md.ui.util.ThemeUtils;
import org.cnodejs.android.md.ui.util.ToastUtils;
import org.cnodejs.android.md.ui.view.IBackToContentTopView;
import org.cnodejs.android.md.ui.view.IMainView;
import org.cnodejs.android.md.ui.viewholder.LoadMoreFooter;
import org.cnodejs.android.md.ui.widget.NavigationItem;
import org.cnodejs.android.md.util.HandlerUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends FullLayoutActivity implements IMainView, IBackToContentTopView, SwipeRefreshLayout.OnRefreshListener, LoadMoreFooter.OnLoadMoreListener {

    /*
     * 抽屉导航布局
     */

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    /*
     * 导航部分的个人信息
     */

    @BindView(R.id.img_avatar)
    ImageView imgAvatar;

    @BindView(R.id.tv_login_name)
    TextView tvLoginName;

    @BindView(R.id.tv_score)
    TextView tvScore;

    @BindView(R.id.btn_logout)
    View btnLogout;

    @BindView(R.id.btn_theme_dark)
    ImageView imgThemeDark;

    @BindView(R.id.nav_top_background)
    View navTopBackground;

    /*
     * 导航项
     */

    @BindViews({
            R.id.btn_nav_all,
            R.id.btn_nav_good,
            R.id.btn_nav_share,
            R.id.btn_nav_ask,
            R.id.btn_nav_job,
            R.id.btn_nav_dev
    })
    List<NavigationItem> navMainItemList;

    @BindView(R.id.btn_nav_dev)
    NavigationItem navItemDev;

    @BindView(R.id.btn_nav_notification)
    NavigationItem navItemNotification;

    /*
     * 内容部分
     */

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recycler_view)
    HeaderAndFooterRecyclerView recyclerView;

    @BindView(R.id.fab_create_topic)
    FloatingActionButton fabCreateTopic;

    private LoadMoreFooter loadMoreFooter;
    private TopicListAdapter adapter;

    private IMainPresenter mainPresenter;

    private int page = 0;
    private long firstBackPressedTime = 0;
    private boolean enableThemeDark;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        enableThemeDark = ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        drawerLayout.setDrawerShadow(R.drawable.navigation_drawer_shadow, GravityCompat.START);
        drawerLayout.addDrawerListener(drawerListener);
        toolbar.setNavigationOnClickListener(new NavigationOpenClickListener(drawerLayout));
        toolbar.setOnClickListener(new DoubleClickBackToContentTopListener(this));

        navItemDev.setVisibility(BuildConfig.DEBUG ? View.VISIBLE : View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadMoreFooter = new LoadMoreFooter(this, recyclerView, this);
        adapter = new TopicListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new FloatingActionButtonBehaviorListener.ForRecyclerView(fabCreateTopic));

        mainPresenter = new MainPresenter(this, this);

        updateUserInfoViews();

        imgThemeDark.setImageResource(enableThemeDark ? R.drawable.ic_wb_sunny_white_24dp : R.drawable.ic_brightness_3_white_24dp);
        navTopBackground.setVisibility(enableThemeDark ? View.INVISIBLE : View.VISIBLE);

        refreshLayout.setColorSchemeResources(R.color.color_accent);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainPresenter.getMessageCountAsyncTask();
        // 判断是否需要切换主题
        if (SettingShared.isEnableThemeDark(this) != enableThemeDark) {
            ThemeUtils.notifyThemeApply(this);
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
            Tab tab = Tab.all;
            for (NavigationItem navItem : navMainItemList) {
                if (navItem.isChecked()) {
                    switch (navItem.getId()) {
                        case R.id.btn_nav_all:
                            tab = Tab.all;
                            break;
                        case R.id.btn_nav_good:
                            tab = Tab.good;
                            break;
                        case R.id.btn_nav_share:
                            tab = Tab.share;
                            break;
                        case R.id.btn_nav_ask:
                            tab = Tab.ask;
                            break;
                        case R.id.btn_nav_job:
                            tab = Tab.job;
                            break;
                        case R.id.btn_nav_dev:
                            tab = Tab.dev;
                            break;
                        default:
                            throw new AssertionError("Unknow tab.");
                    }
                    break;
                }
            }
            mainPresenter.switchTab(tab);
        }

    };

    @Override
    public void onRefresh() {
        mainPresenter.refreshTopicListAsyncTask();
    }

    @Override
    public void onLoadMore() {
        mainPresenter.loadMoreTopicListAsyncTask(page + 1);
    }

    @OnClick({
            R.id.btn_nav_all,
            R.id.btn_nav_good,
            R.id.btn_nav_share,
            R.id.btn_nav_ask,
            R.id.btn_nav_job,
            R.id.btn_nav_dev
    })
    void onMainNavigationItemClick(NavigationItem itemView) {
        for (NavigationItem navItem : navMainItemList) {
            navItem.setChecked(navItem == itemView);
        }
        drawerLayout.closeDrawers();
    }

    @OnClick({
            R.id.btn_nav_notification,
            R.id.btn_nav_setting,
            R.id.btn_nav_about
    })
    void onOtherNavigationItemClick(NavigationItem itemView) {
        switch (itemView.getId()) {
            case R.id.btn_nav_notification:
                if (LoginActivity.checkLogin(this)) {
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

    private class OtherNavigationItemAction implements Runnable {

        private Class gotoClz;

        OtherNavigationItemAction(Class gotoClz) {
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

        void startDelayed() {
            HandlerUtils.handler.postDelayed(this, 400);
        }

    }

    private OtherNavigationItemAction notificationAction = new OtherNavigationItemAction(NotificationActivity.class);
    private OtherNavigationItemAction settingAction = new OtherNavigationItemAction(SettingActivity.class);
    private OtherNavigationItemAction aboutAction = new OtherNavigationItemAction(AboutActivity.class);

    @OnClick(R.id.btn_logout)
    void onBtnLogoutClick() {
        AlertDialogUtils.createBuilderWithAutoTheme(this)
                .setMessage(R.string.logout_tip)
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginShared.logout(MainActivity.this);
                        navItemNotification.setBadge(0); // 未读消息清空
                        updateUserInfoViews();
                    }

                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @OnClick(R.id.btn_theme_dark)
    void onBtnThemeDarkClick() {
        SettingShared.setEnableThemeDark(this, !enableThemeDark);
        ThemeUtils.notifyThemeApply(this);
    }

    @OnClick(R.id.layout_info)
    void onBtnInfoClick() {
        if (TextUtils.isEmpty(LoginShared.getAccessToken(this))) {
            LoginActivity.startForResult(this);
        } else {
            UserDetailActivity.startWithTransitionAnimation(this, LoginShared.getLoginName(this), imgAvatar, LoginShared.getAvatarUrl(this));
        }
    }

    @OnClick(R.id.fab_create_topic)
    void onBtnCreateTopicClick() {
        if (LoginActivity.checkLogin(this)) {
            startActivity(new Intent(this, CreateTopicActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginActivity.REQUEST_DEFAULT && resultCode == RESULT_OK) {
            updateUserInfoViews();
            mainPresenter.getUserAsyncTask();
        }
    }

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
    public void onSwitchTabOk(@NonNull Tab tab) {
        page = 0;
        toolbar.setTitle(tab.getNameId());
        fabCreateTopic.show();
        adapter.clearTopicListAndNotify();
        loadMoreFooter.setState(LoadMoreFooter.STATE_DISABLED);
        refreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefreshTopicListOk(@NonNull List<Topic> topicList) {
        page = 1;
        adapter.setTopicListAndNotify(topicList);
        refreshLayout.setRefreshing(false);
        loadMoreFooter.setState(topicList.isEmpty() ? LoadMoreFooter.STATE_DISABLED : LoadMoreFooter.STATE_ENDLESS);
    }

    @Override
    public void onRefreshTopicListError(@NonNull String message) {
        ToastUtils.with(this).show(message);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMoreTopicListOk(@NonNull List<Topic> topicList) {
        page++;
        adapter.appendTopicListAndNotify(topicList);
        if (topicList.isEmpty()) {
            loadMoreFooter.setState(LoadMoreFooter.STATE_FINISHED);
        } else {
            loadMoreFooter.setState(LoadMoreFooter.STATE_ENDLESS);
        }
    }

    @Override
    public void onLoadMoreTopicListError(@NonNull String message) {
        ToastUtils.with(this).show(message);
        loadMoreFooter.setState(LoadMoreFooter.STATE_FAILED);
    }

    @Override
    public void updateUserInfoViews() {
        if (TextUtils.isEmpty(LoginShared.getAccessToken(this))) {
            GlideApp.with(this).load(R.drawable.image_placeholder).placeholder(R.drawable.image_placeholder).into(imgAvatar);
            tvLoginName.setText(R.string.click_avatar_to_login);
            tvScore.setText(null);
            btnLogout.setVisibility(View.GONE);
        } else {
            GlideApp.with(this).load(LoginShared.getAvatarUrl(this)).placeholder(R.drawable.image_placeholder).into(imgAvatar);
            tvLoginName.setText(LoginShared.getLoginName(this));
            tvScore.setText(getString(R.string.score__, LoginShared.getScore(this)));
            btnLogout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateMessageCountViews(int count) {
        navItemNotification.setBadge(count);
    }

    @Override
    public void backToContentTop() {
        recyclerView.scrollToPosition(0);
        fabCreateTopic.show();
    }

}
