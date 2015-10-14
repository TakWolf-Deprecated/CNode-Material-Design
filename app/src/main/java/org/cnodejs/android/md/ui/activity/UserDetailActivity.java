package org.cnodejs.android.md.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.picasso.Picasso;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.User;
import org.cnodejs.android.md.ui.fragment.UserDetailItemFragment;
import org.cnodejs.android.md.ui.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.util.ShipUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserDetailActivity extends BaseActivity {

    public static void open(Context context, String loginName) {
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("loginName", loginName);
        context.startActivity(intent);
    }

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

    @Bind(R.id.user_detail_progress_wheel)
    protected ProgressWheel progressWheel;

    private ViewPagerAdapter adapter;

    private String loginName;
    private String githubUsername;

    private boolean loading = false;

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

    @OnClick(R.id.user_detail_img_avatar)
    protected void onBtnAvatarClick() {
        if (!loading) {
            getUserAsyncTask();
        }
    }

    private void getUserAsyncTask() {
        loading = true;
        progressWheel.spin();
        ApiClient.service.getUser(loginName, new Callback<Result<User>>() {

            @Override
            public void success(Result<User> result, Response response) {
                if (!isFinishing()) {
                    updateUserInfoViews(result.getData());
                    adapter.update(result.getData());
                    githubUsername = result.getData().getGithubUsername();
                    progressWheel.setProgress(0);
                    loading = false;
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isFinishing()) {
                    if (error.getResponse() != null && error.getResponse().getStatus() == 404) {
                        Toast.makeText(UserDetailActivity.this, R.string.user_not_found, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UserDetailActivity.this, R.string.data_load_faild_and_click_avatar_to_reload, Toast.LENGTH_SHORT).show();
                    }
                    progressWheel.setProgress(0);
                    loading = false;
                }
            }

        });
    }

    private void updateUserInfoViews(User user) {
        Picasso.with(this).load(user.getAvatarUrl()).placeholder(R.drawable.image_placeholder).into(imgAvatar);
        tvLoginName.setText(user.getLoginName());
        if (TextUtils.isEmpty(user.getGithubUsername())) {
            tvGithubUsername.setVisibility(View.INVISIBLE);
            tvGithubUsername.setText(null);
        } else {
            tvGithubUsername.setVisibility(View.VISIBLE);
            tvGithubUsername.setText(Html.fromHtml("<u>" + user.getGithubUsername() + "@github.com" + "</u>"));
        }
        tvCreateTime.setText(getString(R.string.register_time_$) + user.getCreateAt().toString("yyyy-MM-dd"));
        tvScore.setText(getString(R.string.score_$) + user.getScore());
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<UserDetailItemFragment> fmList = new ArrayList<>();
        private String[] titles = {
                "最近回复",
                "最新发布"
        };

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            fmList.add(new UserDetailItemFragment());
            fmList.add(new UserDetailItemFragment());
        }

        public void update(@NonNull User user) {
            fmList.get(0).notifyDataSetChanged(user.getRecentReplies());
            fmList.get(1).notifyDataSetChanged(user.getRecentTopics());
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
            ShipUtils.openInBrowser(this, "https://github.com/" + githubUsername);
        }
    }

}
