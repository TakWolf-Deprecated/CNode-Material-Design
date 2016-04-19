package org.cnodejs.android.md.display.viewholder;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.activity.LoginActivity;
import org.cnodejs.android.md.display.activity.UserDetailActivity;
import org.cnodejs.android.md.display.widget.CNodeWebView;
import org.cnodejs.android.md.display.widget.ThemeUtils;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.DefaultToastCallback;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.TopicWithReply;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.util.FormatUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class TopicHeaderViewHolder {

    @Bind(R.id.topic_item_header_layout_content)
    protected ViewGroup layoutContent;

    @Bind(R.id.topic_item_header_icon_good)
    protected View iconGood;

    @Bind(R.id.topic_item_header_tv_title)
    protected TextView tvTitle;

    @Bind(R.id.topic_item_header_img_avatar)
    protected ImageView imgAvatar;

    @Bind(R.id.topic_item_header_tv_tab)
    protected TextView tvTab;

    @Bind(R.id.topic_item_header_tv_login_name)
    protected TextView tvLoginName;

    @Bind(R.id.topic_item_header_tv_create_time)
    protected TextView tvCreateTime;

    @Bind(R.id.topic_item_header_tv_visit_count)
    protected TextView tvVisitCount;

    @Bind(R.id.topic_item_header_btn_favorite)
    protected ImageView btnFavorite;

    @Bind(R.id.topic_item_header_web_content)
    protected CNodeWebView webContent;

    @Bind(R.id.topic_item_header_layout_no_reply)
    protected ViewGroup layoutNoReply;

    private final Activity activity;
    private Topic topic;
    private boolean isCollect;

    public TopicHeaderViewHolder(@NonNull Activity activity, @NonNull ListView listView) {
        this.activity = activity;
        LayoutInflater inflater = LayoutInflater.from(activity);
        View headerView = inflater.inflate(R.layout.activity_topic_item_header, listView, false);
        ButterKnife.bind(this, headerView);
        listView.addHeaderView(headerView, null, false);
    }

    public void update(@Nullable Topic topic, boolean isCollect, int replyCount) {
        this.topic = topic;
        this.isCollect = isCollect;
        if (topic != null) {
            layoutContent.setVisibility(View.VISIBLE);
            iconGood.setVisibility(topic.isGood() ? View.VISIBLE : View.GONE);

            tvTitle.setText(topic.getTitle());
            Glide.with(activity).load(topic.getAuthor().getAvatarUrl()).placeholder(R.drawable.image_placeholder).dontAnimate().into(imgAvatar);
            tvTab.setText(topic.isTop() ? R.string.tab_top : topic.getTab().getNameId());
            tvTab.setBackgroundDrawable(ThemeUtils.getThemeAttrDrawable(activity, topic.isTop() ? R.attr.referenceBackgroundAccent : R.attr.referenceBackgroundNormal));
            tvTab.setTextColor(topic.isTop() ? Color.WHITE : ThemeUtils.getThemeAttrColor(activity, android.R.attr.textColorSecondary));
            tvLoginName.setText(topic.getAuthor().getLoginName());
            tvCreateTime.setText(FormatUtils.getRecentlyTimeText(topic.getCreateAt()) + "创建");
            tvVisitCount.setText(topic.getVisitCount() + "次浏览");
            btnFavorite.setImageResource(isCollect ? R.drawable.ic_favorite_theme_24dp : R.drawable.ic_favorite_outline_grey600_24dp);

            // 这里直接使用WebView，有性能问题
            webContent.loadRenderedContent(topic.getHandleContent());

            layoutNoReply.setVisibility(replyCount > 0 ? View.GONE : View.VISIBLE);
        } else {
            layoutContent.setVisibility(View.GONE);
            iconGood.setVisibility(View.GONE);
        }
    }

    public void update(@NonNull TopicWithReply topic) {
        update(topic, topic.isCollect(), topic.getReplyList().size());
    }

    @OnClick(R.id.topic_item_header_img_avatar)
    protected void onBtnAvatarClick() {
        UserDetailActivity.startWithTransitionAnimation(activity, topic.getAuthor().getLoginName(), imgAvatar, topic.getAuthor().getAvatarUrl());
    }

    @OnClick(R.id.topic_item_header_btn_favorite)
    protected void onBtnFavoriteClick() {
        if (topic != null) {
            if (LoginActivity.startForResultWithAccessTokenCheck(activity)) {
                if (isCollect) {
                    decollectTopicAsyncTask();
                } else {
                    collectTopicAsyncTask();
                }
            }
        }
    }

    private void collectTopicAsyncTask() {
        Call<Result> call = ApiClient.service.collectTopic(LoginShared.getAccessToken(activity), topic.getId());
        call.enqueue(new DefaultToastCallback<Result>(activity) {

            @Override
            public boolean onResultOk(Response<Result> response, Result result) {
                if (!activity.isFinishing()) {
                    isCollect = true;
                    btnFavorite.setImageResource(R.drawable.ic_favorite_theme_24dp);
                }
                return false;
            }

        });
    }

    private void decollectTopicAsyncTask() {
        Call<Result> call = ApiClient.service.decollectTopic(LoginShared.getAccessToken(activity), topic.getId());
        call.enqueue(new DefaultToastCallback<Result>(activity) {

            @Override
            public boolean onResultOk(Response<Result> response, Result result) {
                if (!activity.isFinishing()) {
                    isCollect = false;
                    btnFavorite.setImageResource(R.drawable.ic_favorite_outline_grey600_24dp);
                }
                return false;
            }

        });
    }

}
