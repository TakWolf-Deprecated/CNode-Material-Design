package org.cnodejs.android.md.display.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.display.activity.TopicActivity;
import org.cnodejs.android.md.display.activity.UserDetailActivity;
import org.cnodejs.android.md.display.widget.ThemeUtils;
import org.cnodejs.android.md.util.FormatUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_LOAD_MORE = 1;

    private final Activity activity;
    private final LayoutInflater inflater;
    private final List<Topic> topicList;

    private boolean loading = false;

    public MainAdapter(@NonNull Activity activity, @NonNull List<Topic> topicList) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        this.topicList = topicList;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public boolean canLoadMore() {
        return topicList.size() >= 20 && !loading;
    }

    @Override
    public int getItemCount() {
        return topicList.size() >= 20 ? topicList.size() + 1 : topicList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (topicList.size() >= 20 && position == getItemCount() - 1) {
            return TYPE_LOAD_MORE;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LOAD_MORE:
                return new LoadMoreViewHolder(inflater.inflate(R.layout.activity_item_load_more, parent, false));
            default: // TYPE_NORMAL
                return new NormalViewHolder(inflater.inflate(R.layout.activity_main_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected ViewHolder(View itemView) {
            super(itemView);
        }

        protected void update(int position) {}

    }

    public class LoadMoreViewHolder extends ViewHolder {

        @Bind(R.id.item_load_more_icon_loading)
        protected View iconLoading;

        @Bind(R.id.item_load_more_icon_finish)
        protected View iconFinish;

        protected LoadMoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void update(int position) {
            iconLoading.setVisibility(loading ? View.VISIBLE : View.GONE);
            iconFinish.setVisibility(loading ? View.GONE : View.VISIBLE);
        }

    }

    public class NormalViewHolder extends ViewHolder {

        @Bind(R.id.main_item_tv_tab)
        protected TextView tvTab;

        @Bind(R.id.main_item_tv_title)
        protected TextView tvTitle;

        @Bind(R.id.main_item_img_avatar)
        protected ImageView imgAvatar;

        @Bind(R.id.main_item_tv_author)
        protected TextView tvAuthor;

        @Bind(R.id.main_item_tv_create_time)
        protected TextView tvCreateTime;

        @Bind(R.id.main_item_tv_reply_count)
        protected TextView tvReplyCount;

        @Bind(R.id.main_item_tv_visit_count)
        protected TextView tvVisitCount;

        @Bind(R.id.main_item_tv_last_reply_time)
        protected TextView tvLastReplyTime;

        @Bind(R.id.main_item_icon_good)
        protected View iconGood;

        private Topic topic;

        protected NormalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void update(int position) {
            topic = topicList.get(position);

            tvTitle.setText(topic.getTitle());
            tvTab.setText(topic.isTop() ? R.string.tab_top : topic.getTab().getNameId());
            tvTab.setBackgroundDrawable(ThemeUtils.getThemeAttrDrawable(activity, topic.isTop() ? R.attr.referenceBackgroundAccent : R.attr.referenceBackgroundNormal));
            tvTab.setTextColor(topic.isTop() ? Color.WHITE : ThemeUtils.getThemeAttrColor(activity, android.R.attr.textColorSecondary));
            Glide.with(activity).load(topic.getAuthor().getAvatarUrl()).placeholder(R.drawable.image_placeholder).dontAnimate().into(imgAvatar);
            tvAuthor.setText(topic.getAuthor().getLoginName());
            tvCreateTime.setText(activity.getString(R.string.create_at_$) + topic.getCreateAt().toString("yyyy-MM-dd HH:mm:ss"));
            tvReplyCount.setText(String.valueOf(topic.getReplyCount()));
            tvVisitCount.setText(String.valueOf(topic.getVisitCount()));
            tvLastReplyTime.setText(FormatUtils.getRecentlyTimeText(topic.getLastReplyAt()));
            iconGood.setVisibility(topic.isGood() ? View.VISIBLE : View.GONE);
        }

        @OnClick(R.id.main_item_img_avatar)
        protected void onBtnAvatarClick() {
            UserDetailActivity.startWithTransitionAnimation(activity, topic.getAuthor().getLoginName(), imgAvatar, topic.getAuthor().getAvatarUrl());
        }

        @OnClick(R.id.main_item_btn_item)
        protected void onBtnItemClick() {
            TopicActivity.start(activity, topic);
        }

    }

}
