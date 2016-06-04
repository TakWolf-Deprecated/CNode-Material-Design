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
import org.cnodejs.android.md.display.util.ActivityUtils;
import org.cnodejs.android.md.display.util.Navigator;
import org.cnodejs.android.md.display.util.ThemeUtils;
import org.cnodejs.android.md.display.util.ToastUtils;
import org.cnodejs.android.md.display.view.IUserDetailView;
import org.cnodejs.android.md.model.api.ApiDefine;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.User;
import org.cnodejs.android.md.presenter.contract.IUserDetailPresenter;
import org.cnodejs.android.md.presenter.implement.UserDetailPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserDetailActivity extends StatusBarActivity implements IUserDetailView, Toolbar.OnMenuItemClickListener {

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

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.tab_layout)
    protected TabLayout tabLayout;

    @BindView(R.id.view_pager)
    protected ViewPager viewPager;

    @BindView(R.id.img_avatar)
    protected ImageView imgAvatar;

    @BindView(R.id.tv_login_name)
    protected TextView tvLoginName;

    @BindView(R.id.tv_github_username)
    protected TextView tvGithubUsername;

    @BindView(R.id.tv_create_time)
    protected TextView tvCreateTime;

    @BindView(R.id.tv_score)
    protected TextView tvScore;

    @BindView(R.id.progress_wheel)
    protected ProgressWheel progressWheel;

    private UserDetailAdapter adapter;

    private IUserDetailPresenter userDetailPresenter;

    private String loginName;
    private String githubUsername;

    private boolean loading = false;

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

        userDetailPresenter = new UserDetailPresenter(this, this);

        userDetailPresenter.getUserAsyncTask(loginName);
        userDetailPresenter.getCollectTopicListAsyncTask(loginName);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_in_browser:
                Navigator.openInBrowser(this, ApiDefine.USER_LINK_URL_PREFIX + loginName);
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

    @OnClick(R.id.img_avatar)
    protected void onBtnAvatarClick() {
        if (!loading) {
            userDetailPresenter.getUserAsyncTask(loginName);
            userDetailPresenter.getCollectTopicListAsyncTask(loginName);
        }
    }

    @OnClick(R.id.tv_github_username)
    protected void onBtnGithubUsernameClick() {
        if (!TextUtils.isEmpty(githubUsername)) {
            Navigator.openInBrowser(this, "https://github.com/" + githubUsername);
        }
    }

    @Override
    public void onGetUserStart() {
        loading = true;
        progressWheel.spin();
    }

    @Override
    public boolean onGetUserResultOk(@NonNull Result.Data<User> result) {
        if (ActivityUtils.isAlive(this)) {
            updateUserInfoViews(result.getData());
            adapter.update(result.getData());
            githubUsername = result.getData().getGithubUsername();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onGetUserResultError(@NonNull Result.Error error) {
        if (ActivityUtils.isAlive(this)) {
            ToastUtils.with(this).show(error.getErrorMessage());
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onGetUserLoadError() {
        if (ActivityUtils.isAlive(this)) {
            ToastUtils.with(this).show(R.string.data_load_faild_and_click_avatar_to_reload);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onGetUserFinish() {
        progressWheel.stopSpinning();
        loading = false;
    }

    @Override
    public void updateUserInfoViews(@NonNull User user) {
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

    @Override
    public boolean onGetCollectTopicListResultOk(@NonNull Result.Data<List<Topic>> result) {
        if (ActivityUtils.isAlive(this)) {
            adapter.update(result.getData());
            return false;
        } else {
            return true;
        }
    }

}
