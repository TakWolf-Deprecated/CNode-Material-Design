package org.cnodejs.android.md.activity;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.adapter.MainAdapter;
import org.cnodejs.android.md.listener.NavigationOpenClickListener;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.TabType;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.util.HandlerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    // 抽屉导航布局
    @Bind(R.id.main_drawer_layout)
    protected DrawerLayout drawerLayout;

    // 导航部分的个人信息
    @Bind(R.id.main_left_img_avatar)
    protected ImageView imgAvatar;

    @Bind(R.id.main_left_tv_nickname)
    protected TextView tvNickname;

    @Bind(R.id.main_left_tv_score)
    protected TextView tvScore;

    @Bind(R.id.main_left_tv_badger_notification)
    protected TextView tvBadgerNotification;

    // 主要导航项
    @Bind({
            R.id.main_left_btn_all,
            R.id.main_left_btn_good,
            R.id.main_left_btn_share,
            R.id.main_left_btn_ask,
            R.id.main_left_btn_job
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
        toolbar.setNavigationOnClickListener(new NavigationOpenClickListener(drawerLayout));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainAdapter(this, topicList);
        recyclerView.setAdapter(adapter);
        fabNewTopic.attachToRecyclerView(recyclerView);

        refreshLayout.setColorSchemeResources(R.color.red_light, R.color.green_light, R.color.blue_light, R.color.orange_light);
        refreshLayout.setOnRefreshListener(this);

        HandlerUtils.postDelayed(new Runnable() {

            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                onRefresh();
            }

        }, 100); // TODO refreshLayout无法直接在onCreate中设置刷新状态
    }

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
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (currentTab == tab) {
                    Toast.makeText(MainActivity.this, "数据加载失败，请重试", Toast.LENGTH_SHORT).show();
                    refreshLayout.setRefreshing(false);
                }
            }

        });
    }

    /**
     * 更新列表
     */
    private void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
        layoutNoData.setVisibility(topicList.size() == 0 ? View.VISIBLE : View.GONE);
    }

    /**
     * 主导航项单击事件
     */
    @OnClick({
            R.id.main_left_btn_all,
            R.id.main_left_btn_good,
            R.id.main_left_btn_share,
            R.id.main_left_btn_ask,
            R.id.main_left_btn_job
    })
    public void onNavigationMainItemClick(CheckedTextView itemView) {
        switch (itemView.getId()) {
            case R.id.main_left_btn_all:
                drawerLayout.setDrawerListener(tabAllDrawerListener);
                break;
            case R.id.main_left_btn_good:
                drawerLayout.setDrawerListener(tabGoodDrawerListener);
                break;
            case R.id.main_left_btn_share:
                drawerLayout.setDrawerListener(tabShareDrawerListener);
                break;
            case R.id.main_left_btn_ask:
                drawerLayout.setDrawerListener(tabAskDrawerListener);
                break;
            case R.id.main_left_btn_job:
                drawerLayout.setDrawerListener(tabJobDrawerListener);
                break;
            default:
                drawerLayout.setDrawerListener(null);
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
                toolbar.setTitle(currentTab.getNameId());
                topicList.clear();
                notifyDataSetChanged();
                refreshLayout.setRefreshing(true);
                onRefresh();
                fabNewTopic.show(true);
            }
            drawerLayout.setDrawerListener(null);
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

    // TODO

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
