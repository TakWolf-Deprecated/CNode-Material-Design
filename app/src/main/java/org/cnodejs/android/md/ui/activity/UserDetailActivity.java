package org.cnodejs.android.md.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.picasso.Picasso;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.User;
import org.cnodejs.android.md.ui.base.BaseActivity;
import org.cnodejs.android.md.ui.fragment.UserDetailItemFragment;
import org.cnodejs.android.md.ui.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.ui.widget.ThemeUtils;
import org.cnodejs.android.md.ui.widget.ToastUtils;
import org.cnodejs.android.md.util.HandlerUtils;
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

    private static final String NAME_IMG_AVATAR = "imgAvatar";

    public static void openWithTransitionAnimation(Activity activity, String loginName, ImageView imgAvatar, String avatarUrl) {
        Intent intent = new Intent(activity, UserDetailActivity.class);
        intent.putExtra("loginName", loginName);
        intent.putExtra("avatarUrl", avatarUrl);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, imgAvatar, NAME_IMG_AVATAR);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

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
    private long startLoadingTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        ButterKnife.bind(this);

        ViewCompat.setTransitionName(imgAvatar, NAME_IMG_AVATAR);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());
        tabLayout.setupWithViewPager(viewPager);

        loginName = getIntent().getStringExtra("loginName");
        if (!TextUtils.isEmpty(loginName)) {
            tvLoginName.setText(loginName);
        }

        String avatarUrl = getIntent().getStringExtra("avatarUrl");
        if (!TextUtils.isEmpty(avatarUrl)) {
            Picasso.with(this).load(avatarUrl).placeholder(R.drawable.image_placeholder).into(imgAvatar);
        }

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
        startLoadingTime = System.currentTimeMillis();
        progressWheel.spin();
        ApiClient.service.getUser(loginName, new Callback<Result<User>>() {

            private long getPostTime() {
                long postTime = 1000 - (System.currentTimeMillis() - startLoadingTime);
                if (postTime > 0) {
                    return postTime;
                } else {
                    return 0;
                }
            }

            @Override
            public void success(final Result<User> result, Response response) {
                HandlerUtils.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (!isFinishing()) {
                            updateUserInfoViews(result.getData());
                            adapter.update(result.getData());
                            githubUsername = result.getData().getGithubUsername();
                            progressWheel.setProgress(0);
                            loading = false;
                        }
                    }

                }, getPostTime());
            }

            @Override
            public void failure(final RetrofitError error) {
                HandlerUtils.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (!isFinishing()) {
                            if (error.getResponse() != null && error.getResponse().getStatus() == 404) {
                                ToastUtils.with(UserDetailActivity.this).show(R.string.user_not_found);
                            } else {
                                ToastUtils.with(UserDetailActivity.this).show(R.string.data_load_faild_and_click_avatar_to_reload);
                            }
                            progressWheel.setProgress(0);
                            loading = false;
                        }
                    }

                }, getPostTime());
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
            fmList.get(0).notifyDataSetChanged(user.getRecentReplyList());
            fmList.get(1).notifyDataSetChanged(user.getRecentTopicList());
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
