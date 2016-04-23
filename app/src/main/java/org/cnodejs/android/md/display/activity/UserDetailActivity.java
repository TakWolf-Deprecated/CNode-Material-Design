package org.cnodejs.android.md.display.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.adapter.UserDetailAdapter;
import org.cnodejs.android.md.display.base.StatusBarActivity;
import org.cnodejs.android.md.display.listener.NavigationFinishClickListener;
import org.cnodejs.android.md.display.widget.ThemeUtils;
import org.cnodejs.android.md.display.widget.ToastUtils;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.CallbackAdapter;
import org.cnodejs.android.md.model.api.DefaultToastCallback;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.User;
import org.cnodejs.android.md.util.HandlerUtils;
import org.cnodejs.android.md.util.ShipUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class UserDetailActivity extends StatusBarActivity implements Toolbar.OnMenuItemClickListener {

    private static final String EXTRA_LOGIN_NAME = "loginName";
    private static final String EXTRA_AVATAR_URL = "avatarUrl";
    private static final String NAME_IMG_AVATAR = "imgAvatar";

    public static void startWithTransitionAnimation(@NonNull Activity activity, String loginName, @NonNull ImageView imgAvatar, String avatarUrl) {
        Intent intent = new Intent(activity, UserDetailActivity.class);
        intent.putExtra(EXTRA_LOGIN_NAME, loginName);
        intent.putExtra(EXTRA_AVATAR_URL, avatarUrl);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, imgAvatar, NAME_IMG_AVATAR);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    public static void start(@NonNull Activity activity, String loginName) {
        Intent intent = new Intent(activity, UserDetailActivity.class);
        intent.putExtra(EXTRA_LOGIN_NAME, loginName);
        activity.startActivity(intent);
    }

    public static void start(@NonNull Context context, String loginName) {
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_LOGIN_NAME, loginName);
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

    private UserDetailAdapter adapter;

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
        toolbar.inflateMenu(R.menu.user_detail);
        toolbar.setOnMenuItemClickListener(this);

        adapter = new UserDetailAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());
        tabLayout.setupWithViewPager(viewPager);

        loginName = getIntent().getStringExtra(EXTRA_LOGIN_NAME);
        tvLoginName.setText(loginName);

        String avatarUrl = getIntent().getStringExtra(EXTRA_AVATAR_URL);
        if (!TextUtils.isEmpty(avatarUrl)) {
            Glide.with(this).load(avatarUrl).placeholder(R.drawable.image_placeholder).dontAnimate().into(imgAvatar);
        }

        getUserAsyncTask();
        getCollectTopicListAsyncTask();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_in_browser:
                ShipUtils.openInBrowser(this, "https://cnodejs.org/user/" + loginName);
                return true;
            default:
                return false;
        }
    }

    /**
     * Activity被销毁的时候不保存Fragment
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {}

    @OnClick(R.id.user_detail_img_avatar)
    protected void onBtnAvatarClick() {
        if (!loading) {
            getUserAsyncTask();
            getCollectTopicListAsyncTask();
        }
    }

    @OnClick(R.id.user_detail_tv_github_username)
    protected void onBtnGithubUsernameClick() {
        if (!TextUtils.isEmpty(githubUsername)) {
            ShipUtils.openInBrowser(this, "https://github.com/" + githubUsername);
        }
    }

    private void updateUserInfoViews(User user) {
        Glide.with(this).load(user.getAvatarUrl()).placeholder(R.drawable.image_placeholder).dontAnimate().into(imgAvatar);
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

    private void getUserAsyncTask() {
        loading = true;
        startLoadingTime = System.currentTimeMillis();
        progressWheel.spin();
        Call<Result.Data<User>> call = ApiClient.service.getUser(loginName);
        call.enqueue(new CallbackAdapter<Result.Data<User>>() {

            private long getPostTime() {
                long postTime = 1000 - (System.currentTimeMillis() - startLoadingTime);
                if (postTime > 0) {
                    return postTime;
                } else {
                    return 0;
                }
            }

            @Override
            public boolean onResultOk(Response<Result.Data<User>> response, final Result.Data<User> result) {
                HandlerUtils.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (!isFinishing()) {
                            updateUserInfoViews(result.getData());
                            adapter.update(result.getData());
                            githubUsername = result.getData().getGithubUsername();
                            onFinish();
                        }
                    }

                }, getPostTime());
                return true;
            }

            @Override
            public boolean onResultError(final Response<Result.Data<User>> response, final Result.Error error) {
                HandlerUtils.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (!isFinishing()) {
                            if (response.code() == 404) {
                                ToastUtils.with(UserDetailActivity.this).show(error.getErrorMessage());
                            } else {
                                ToastUtils.with(UserDetailActivity.this).show(R.string.data_load_faild_and_click_avatar_to_reload);
                            }
                            onFinish();
                        }
                    }

                }, getPostTime());
                return true;
            }

            @Override
            public boolean onCallException(Throwable t, Result.Error error) {
                HandlerUtils.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (!isFinishing()) {
                            ToastUtils.with(UserDetailActivity.this).show(R.string.data_load_faild_and_click_avatar_to_reload);
                            onFinish();
                        }
                    }

                }, getPostTime());
                return true;
            }

            @Override
            public void onFinish() {
                progressWheel.stopSpinning();
                loading = false;
            }

        });
    }

    private void getCollectTopicListAsyncTask() {
        Call<Result.Data<List<Topic>>> call = ApiClient.service.getCollectTopicList(loginName);
        call.enqueue(new DefaultToastCallback<Result.Data<List<Topic>>>(this) {

            @Override
            public boolean onResultOk(Response<Result.Data<List<Topic>>> response, Result.Data<List<Topic>> result) {
                adapter.update(result.getData());
                return false;
            }

        });
    }

}
