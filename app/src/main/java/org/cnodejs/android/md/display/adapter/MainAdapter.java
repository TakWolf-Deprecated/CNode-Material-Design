package org.cnodejs.android.md.display.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.activity.UserDetailActivity;
import org.cnodejs.android.md.display.util.Navigator;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.util.FormatUtils;

import java.util.List;

import butterknife.BindView;
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
                return new LoadMoreViewHolder(inflater.inflate(R.layout.item_load_more, parent, false));
            default: // TYPE_NORMAL
                return new NormalViewHolder(inflater.inflate(R.layout.item_main, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(position);
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {

        protected ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        protected void update(int position) {}

    }

    public class LoadMoreViewHolder extends ViewHolder {

        @BindView(R.id.icon_loading)
        protected View iconLoading;

        @BindView(R.id.icon_finish)
        protected View iconFinish;

        protected LoadMoreViewHolder(@NonNull View itemView) {
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

        @BindView(R.id.ctv_tab)
        protected CheckedTextView ctvTab;

        @BindView(R.id.tv_title)
        protected TextView tvTitle;

        @BindView(R.id.img_avatar)
        protected ImageView imgAvatar;

        @BindView(R.id.tv_author)
        protected TextView tvAuthor;

        @BindView(R.id.tv_create_time)
        protected TextView tvCreateTime;

        @BindView(R.id.tv_reply_count)
        protected TextView tvReplyCount;

        @BindView(R.id.tv_visit_count)
        protected TextView tvVisitCount;

        @BindView(R.id.tv_last_reply_time)
        protected TextView tvLastReplyTime;

        @BindView(R.id.icon_good)
        protected View iconGood;

        private Topic topic;

        protected NormalViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void update(int position) {
            topic = topicList.get(position);

            tvTitle.setText(topic.getTitle());
            ctvTab.setText(topic.isTop() ? R.string.tab_top : topic.getTab().getNameId());
            ctvTab.setChecked(topic.isTop());
            Glide.with(activity).load(topic.getAuthor().getAvatarUrl()).placeholder(R.drawable.image_placeholder).dontAnimate().into(imgAvatar);
            tvAuthor.setText(topic.getAuthor().getLoginName());
            tvCreateTime.setText(activity.getString(R.string.create_at_$) + topic.getCreateAt().toString("yyyy-MM-dd HH:mm:ss"));
            tvReplyCount.setText(String.valueOf(topic.getReplyCount()));
            tvVisitCount.setText(String.valueOf(topic.getVisitCount()));
            tvLastReplyTime.setText(FormatUtils.getRelativeTimeSpanString(topic.getLastReplyAt()));
            iconGood.setVisibility(topic.isGood() ? View.VISIBLE : View.GONE);
        }

        @OnClick(R.id.img_avatar)
        protected void onBtnAvatarClick() {
            UserDetailActivity.startWithTransitionAnimation(activity, topic.getAuthor().getLoginName(), imgAvatar, topic.getAuthor().getAvatarUrl());
        }

        @OnClick(R.id.btn_item)
        protected void onBtnItemClick() {
            Navigator.TopicWithAutoCompat.start(activity, topic);
        }

    }

}
