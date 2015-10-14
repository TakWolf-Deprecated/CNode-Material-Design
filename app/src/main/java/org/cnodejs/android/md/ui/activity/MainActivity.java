package org.cnodejs.android.md.ui.activity;

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
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.umeng.update.UmengUpdateAgent;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.CallbackAdapter;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.TabType;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.User;
import org.cnodejs.android.md.storage.LoginShared;
import org.cnodejs.android.md.ui.adapter.MainAdapter;
import org.cnodejs.android.md.ui.listener.NavigationOpenClickListener;
import org.cnodejs.android.md.ui.listener.RecyclerViewLoadMoreListener;
import org.cnodejs.android.md.ui.widget.RefreshLayoutUtils;
import org.cnodejs.android.md.util.FormatUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerViewLoadMoreListener.OnLoadMoreListener {

    private static final int REQUEST_LOGIN = 1024;

    // 抽屉导航布局
    @Bind(R.id.main_drawer_layout)
    protected DrawerLayout drawerLayout;

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

    @Bind(R.id.main_fab_new_topic)
    protected FloatingActionButton fabNewTopic;

    @Bind(R.id.main_layout_no_data)
    protected ViewGroup layoutNoData;

    // 当前版块，默认为all
    private TabType currentTab = TabType.all;
    private int currentPage = 0; // 从未加载
    private List<Topic> topicList = new ArrayList<>();
    private MainAdapter adapter;

    // 首次按下返回键时间戳
    private long firstBackPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        drawerLayout.setDrawerShadow(R.drawable.navigation_drawer_shadow, GravityCompat.START);
        drawerLayout.setDrawerListener(openDrawerListener);
        toolbar.setNavigationOnClickListener(new NavigationOpenClickListener(drawerLayout));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MainAdapter(this, topicList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerViewLoadMoreListener(linearLayoutManager, this, 20));
        fabNewTopic.attachToRecyclerView(recyclerView);

        updateUserInfoViews();

        RefreshLayoutUtils.initOnCreate(refreshLayout, this);
        RefreshLayoutUtils.refreshOnCreate(refreshLayout, this);

        // 友盟更新
        UmengUpdateAgent.update(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMessageCountAsyncTask();
    }

    /**
     * 未读消息数目
     */
    private void getMessageCountAsyncTask() {
        if (!TextUtils.isEmpty(LoginShared.getAccessToken(this))) {
            ApiClient.service.getMessageCount(LoginShared.getAccessToken(this), new Callback<Result<Integer>>() {

                @Override
                public void success(Result<Integer> result, Response response) {
                    tvBadgerNotification.setText(FormatUtils.getNavigationDisplayCountText(result.getData()));
                }

                @Override
                public void failure(RetrofitError error) {
                    if (error.getResponse() != null && error.getResponse().getStatus() == 403) {
                        tvBadgerNotification.setText(null);
                    }
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
            Picasso.with(this).load(R.drawable.image_placeholder).placeholder(R.drawable.image_placeholder).into(imgAvatar);
            tvLoginName.setText(R.string.click_avatar_to_login);
            tvScore.setText(null);
            btnLogout.setVisibility(View.GONE);
        } else {
            Picasso.with(this).load(LoginShared.getAvatarUrl(this)).placeholder(R.drawable.image_placeholder).into(imgAvatar);
            tvLoginName.setText(LoginShared.getLoginName(this));
            tvScore.setText(getString(R.string.score_$) + LoginShared.getScore(this));
            btnLogout.setVisibility(View.VISIBLE);
        }
    }

    private void getUserAsyncTask() {
        if (!TextUtils.isEmpty(LoginShared.getAccessToken(this))) {
            ApiClient.service.getUser(LoginShared.getLoginName(this), new CallbackAdapter<Result<User>>() {

                @Override
                public void success(Result<User> result, Response response) {
                    LoginShared.update(MainActivity.this, result.getData());
                    updateUserInfoViews();
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
        ApiClient.service.getTopics(tab, 1, 20, false, new Callback<Result<List<Topic>>>() {

            @Override
            public void success(Result<List<Topic>> result, Response response) {
                if (currentTab == tab && result.getData() != null) {
                    topicList.clear();
                    topicList.addAll(result.getData());
                    notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                    currentPage = 1;
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (currentTab == tab) {
                    Toast.makeText(MainActivity.this, R.string.data_load_faild, Toast.LENGTH_SHORT).show();
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
            ApiClient.service.getTopics(tab, page + 1, 20, false, new Callback<Result<List<Topic>>>() {

                @Override
                public void success(Result<List<Topic>> result, Response response) {
                    if (currentTab == tab && currentPage == page) {
                        if (result.getData().size() > 0) {
                            topicList.addAll(result.getData());
                            adapter.setLoading(false);
                            adapter.notifyItemRangeInserted(topicList.size() - result.getData().size(), result.getData().size());
                            currentPage++;
                        } else {
                            Toast.makeText(MainActivity.this, R.string.have_no_more_data, Toast.LENGTH_SHORT).show();
                            adapter.setLoading(false);
                            adapter.notifyItemChanged(adapter.getItemCount() - 1);
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (currentTab == tab && currentPage == page) {
                        Toast.makeText(MainActivity.this, R.string.data_load_faild, Toast.LENGTH_SHORT).show();
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
        layoutNoData.setVisibility(topicList.size() == 0 ? View.VISIBLE : View.GONE);
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
                fabNewTopic.show(true);
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
                if (TextUtils.isEmpty(LoginShared.getAccessToken(this))) {
                    showNeedLoginDialog();
                } else {
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
        new MaterialDialog.Builder(this)
                .content(R.string.logout_tip)
                .positiveText(R.string.logout)
                .negativeText(R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        LoginShared.logout(MainActivity.this);
                        tvBadgerNotification.setText(null); // 未读消息清空
                        updateUserInfoViews();
                    }

                })
                .show();
    }

    /**
     * 用户信息按钮
     */
    @OnClick(R.id.main_nav_layout_info)
    protected void onBtnInfoClick() {
        if (TextUtils.isEmpty(LoginShared.getAccessToken(this))) {
            startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_LOGIN);
        } else {
            UserDetailActivity.open(this, LoginShared.getLoginName(this));
        }
    }

    /**
     * 发帖按钮
     */
    @OnClick(R.id.main_fab_new_topic)
    protected void onBtnNewTopicClick() {
        if (TextUtils.isEmpty(LoginShared.getAccessToken(this))) {
            showNeedLoginDialog();
        } else {
            startActivity(new Intent(this, NewTopicActivity.class));
        }
    }

    /**
     * 显示需要登录对话框
     */
    private void showNeedLoginDialog() {
        new MaterialDialog.Builder(this)
                .content(R.string.need_login_tip)
                .positiveText(R.string.login)
                .negativeText(R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), REQUEST_LOGIN);
                    }

                })
                .show();
    }

    /**
     * 判断登录是否成功
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN && resultCode == RESULT_OK) {
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
                Toast.makeText(this, R.string.press_back_again_to_exit, Toast.LENGTH_SHORT).show();
                firstBackPressedTime = secondBackPressedTime;
            } else {
                finish();
            }
        }
    }

}
