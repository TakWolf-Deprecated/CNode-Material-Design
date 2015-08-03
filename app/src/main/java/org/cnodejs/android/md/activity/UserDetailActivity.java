package org.cnodejs.android.md.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.fragment.UserDetailItemFragment;
import org.cnodejs.android.md.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserDetailActivity extends AppCompatActivity {

    @Bind(R.id.user_detail_toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.user_detail_tab_layout)
    protected TabLayout tabLayout;

    @Bind(R.id.user_detail_view_pager)
    protected ViewPager viewPager;

    private ViewPagerAdapter adapter;

    private String loginName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());
        tabLayout.setupWithViewPager(viewPager);

        loginName = getIntent().getStringExtra("loginName");

        getUserAsyncTask();
    }

    private void getUserAsyncTask() {
        ApiClient.service.getUser(loginName, new Callback<Result<User>>() {

            @Override
            public void success(Result<User> result, Response response) {

                adapter.update(result.getData());

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(UserDetailActivity.this, R.string.data_load_faild, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<UserDetailItemFragment> fmList = new ArrayList<>();
        private String[] titles = {
                "最近回复",
                "最新发布",
                "收藏话题"
        };

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            fmList.add(new UserDetailItemFragment());
            fmList.add(new UserDetailItemFragment());
            fmList.add(new UserDetailItemFragment());
        }

        public void update(@NonNull User user) {
            fmList.get(0).notifyDataSetChanged(user.getRecentReplies());
            fmList.get(1).notifyDataSetChanged(user.getRecentTopics());
            fmList.get(2).notifyDataSetChanged(user.getCollectTopics());
        }

        @Override
        public Fragment getItem(int position) {
            return fmList.get(position);
        }

        @Override
        public int getCount() {
            return fmList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

    }

}
