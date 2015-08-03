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
import android.text.Html;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.fragment.UserDetailItemFragment;
import org.cnodejs.android.md.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.User;
import org.cnodejs.android.md.util.ShipUtils;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @Bind(R.id.user_detail_img_avatar)
    protected ImageView imgAvatar;

    @Bind(R.id.user_detail_tv_login_name)
    protected TextView tvLoginName;

    @Bind(R.id.user_detail_tv_github_username)
    protected TextView tvGithubUsername;

    @Bind(R.id.user_detail_tv_create_time)
    protected TextView tvCreateTime;

    @Bind(R.id.user_detail_tv_score)
    protected TextView tvScore;

    private ViewPagerAdapter adapter;

    private String loginName;
    private String githubUsername;

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
                if (!isFinishing()) {
                    updateUserInfoViews(result.getData());
                    adapter.update(result.getData());
                    githubUsername = result.getData().getGithubUsername();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isFinishing()) {
                    Toast.makeText(UserDetailActivity.this, R.string.data_load_faild, Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void updateUserInfoViews(User user) {
        Picasso.with(this).load(ApiClient.ROOT_HOST + user.getAvatarUrl()).error(R.drawable.image_default).into(imgAvatar);
        tvLoginName.setText(user.getLoginName());
        tvGithubUsername.setText(Html.fromHtml("<u>" + user.getGithubUsername() + "@github.com" + "</u>"));
        tvCreateTime.setText(getString(R.string.register_time_$) + user.getCreateAt().toString("yyyy-MM-dd"));
        tvScore.setText(getString(R.string.score_$) + user.getScore());
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

    @OnClick(R.id.user_detail_tv_github_username)
    protected void onBtnGithubUsernameClick() {
        if (!TextUtils.isEmpty(githubUsername)) {
            ShipUtils.openUrlByBrowser(this, "https://github.com/" + githubUsername);
        }
    }

}
