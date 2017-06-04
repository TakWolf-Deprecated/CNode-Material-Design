package org.cnodejs.android.md.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.ui.activity.UserDetailActivity;
import org.cnodejs.android.md.ui.util.Navigator;
import org.cnodejs.android.md.util.FormatUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicListAdapter extends BaseAdapter {

    private final Activity activity;
    private final LayoutInflater inflater;
    private final List<Topic> topicList = new ArrayList<>();

    public TopicListAdapter(@NonNull Activity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    public List<Topic> getTopicList() {
        return topicList;
    }

    @Override
    public int getCount() {
        return topicList.size();
    }

    @Override
    public Object getItem(int position) {
        return topicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_topic, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.update(topicList.get(position));
        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.ctv_tab)
        CheckedTextView ctvTab;

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.img_avatar)
        ImageView imgAvatar;

        @BindView(R.id.tv_author)
        TextView tvAuthor;

        @BindView(R.id.tv_create_time)
        TextView tvCreateTime;

        @BindView(R.id.tv_reply_count)
        TextView tvReplyCount;

        @BindView(R.id.tv_visit_count)
        TextView tvVisitCount;

        @BindView(R.id.tv_last_reply_time)
        TextView tvLastReplyTime;

        @BindView(R.id.icon_good)
        View iconGood;

        private Topic topic;

        ViewHolder(@NonNull View itemView) {
            ButterKnife.bind(this, itemView);
        }

        void update(@NonNull Topic topic) {
            this.topic = topic;

            tvTitle.setText(topic.getTitle());
            ctvTab.setText(topic.isTop() ? R.string.tab_top : topic.getTab().getNameId());
            ctvTab.setChecked(topic.isTop());
            Glide.with(activity).load(topic.getAuthor().getAvatarUrl()).placeholder(R.drawable.image_placeholder).dontAnimate().into(imgAvatar);
            tvAuthor.setText(topic.getAuthor().getLoginName());
            tvCreateTime.setText(activity.getString(R.string.create_at_$s, topic.getCreateAt().toString("yyyy-MM-dd HH:mm:ss")));
            tvReplyCount.setText(String.valueOf(topic.getReplyCount()));
            tvVisitCount.setText(String.valueOf(topic.getVisitCount()));
            tvLastReplyTime.setText(FormatUtils.getRelativeTimeSpanString(topic.getLastReplyAt()));
            iconGood.setVisibility(topic.isGood() ? View.VISIBLE : View.GONE);
        }

        @OnClick(R.id.img_avatar)
        void onBtnAvatarClick() {
            UserDetailActivity.startWithTransitionAnimation(activity, topic.getAuthor().getLoginName(), imgAvatar, topic.getAuthor().getAvatarUrl());
        }

        @OnClick(R.id.btn_item)
        void onBtnItemClick() {
            Navigator.TopicWithAutoCompat.start(activity, topic);
        }

    }

}
