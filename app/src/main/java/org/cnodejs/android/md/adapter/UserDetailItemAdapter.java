package org.cnodejs.android.md.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.activity.TopicActivity;
import org.cnodejs.android.md.activity.UserDetailActivity;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.entity.TopicSimple;
import org.cnodejs.android.md.util.FormatUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserDetailItemAdapter extends RecyclerView.Adapter<UserDetailItemAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<TopicSimple> topicList;

    public UserDetailItemAdapter(Context context, @NonNull List<TopicSimple> topicList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.topicList = topicList;
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.activity_user_detail_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.user_detail_item_img_avatar)
        protected ImageView imgAvatar;

        @Bind(R.id.user_detail_item_tv_title)
        protected TextView tvTitle;

        @Bind(R.id.user_detail_item_tv_login_name)
        protected TextView tvLoginName;

        @Bind(R.id.user_detail_item_tv_last_reply_time)
        protected TextView tvLastReplyTime;

        private TopicSimple topic;

        protected ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void update(int position) {
            topic = topicList.get(position);

            tvTitle.setText(topic.getTitle());
            Picasso.with(context).load(topic.getAuthor().getAvatarUrl()).placeholder(R.drawable.image_default).into(imgAvatar);
            tvLoginName.setText(topic.getAuthor().getLoginName());
            tvLastReplyTime.setText(FormatUtils.getRecentlyTimeFormatText(topic.getLastReplyAt()));
        }

        @OnClick(R.id.user_detail_item_img_avatar)
        protected void onBtnAvatarClick() {
            Intent intent = new Intent(context, UserDetailActivity.class);
            intent.putExtra("loginName", topic.getAuthor().getLoginName());
            context.startActivity(intent);
        }

        @OnClick(R.id.user_detail_item_btn_item)
        protected void onBtnItemClick() {
            Intent intent = new Intent(context, TopicActivity.class);
            intent.putExtra("topicId", topic.getId());
            context.startActivity(intent);
        }

    }

}
