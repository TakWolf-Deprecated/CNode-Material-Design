package org.cnodejs.android.md.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.entity.Message;
import org.cnodejs.android.md.ui.activity.UserDetailActivity;
import org.cnodejs.android.md.ui.util.Navigator;
import org.cnodejs.android.md.ui.widget.ContentWebView;
import org.cnodejs.android.md.util.FormatUtils;
import org.cnodejs.android.md.util.ResUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    private final Activity activity;
    private final LayoutInflater inflater;
    private final List<Message> messageList = new ArrayList<>();

    public MessageListAdapter(@NonNull Activity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    public List<Message> getMessageList() {
        return messageList;
    }

    public void markAllMessageRead() {
        for (Message message : messageList) {
            message.setRead(true);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_message, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(messageList.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_avatar)
        protected ImageView imgAvatar;

        @BindView(R.id.tv_from)
        protected TextView tvFrom;

        @BindView(R.id.tv_time)
        protected TextView tvTime;

        @BindView(R.id.tv_action)
        protected TextView tvAction;

        @BindView(R.id.badge_read)
        protected View badgeRead;

        @BindView(R.id.web_content)
        protected ContentWebView webContent;

        @BindView(R.id.tv_topic_title)
        protected TextView tvTopicTitle;

        private Message message;

        protected ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void update(@NonNull Message message) {
            this.message = message;

            Glide.with(activity).load(message.getAuthor().getAvatarUrl()).placeholder(R.drawable.image_placeholder).dontAnimate().into(imgAvatar);
            tvFrom.setText(message.getAuthor().getLoginName());
            tvTime.setText(FormatUtils.getRelativeTimeSpanString(message.getCreateAt()));
            tvTime.setTextColor(ResUtils.getThemeAttrColor(activity, message.isRead() ? android.R.attr.textColorSecondary : R.attr.colorAccent));
            badgeRead.setVisibility(message.isRead() ? View.GONE : View.VISIBLE);
            tvTopicTitle.setText(activity.getString(R.string.topic_$s, message.getTopic().getTitle()));

            // 判断通知类型
            if (message.getType() == Message.Type.at) {
                if (message.getReply() == null || TextUtils.isEmpty(message.getReply().getId())) {
                    tvAction.setText(R.string.at_you_in_topic);
                    webContent.setVisibility(View.GONE);
                } else {
                    tvAction.setText(R.string.at_you_in_reply);
                    webContent.setVisibility(View.VISIBLE);
                    webContent.loadRenderedContent(message.getReply().getContentHtml());  // 这里直接使用WebView，有性能问题
                }
            } else {
                tvAction.setText(R.string.reply_your_topic);
                webContent.setVisibility(View.VISIBLE);
                webContent.loadRenderedContent(message.getReply().getContentHtml());  // 这里直接使用WebView，有性能问题
            }
        }

        @OnClick(R.id.img_avatar)
        protected void onBtnAvatarClick() {
            UserDetailActivity.startWithTransitionAnimation(activity, message.getAuthor().getLoginName(), imgAvatar, message.getAuthor().getAvatarUrl());
        }

        @OnClick(R.id.btn_item)
        protected void onBtnItemClick() {
            Navigator.TopicWithAutoCompat.start(activity, message.getTopic().getId());
        }

    }

}
