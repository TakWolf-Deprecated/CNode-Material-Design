package org.cnodejs.android.md.display.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.entity.Message;
import org.cnodejs.android.md.display.activity.TopicActivity;
import org.cnodejs.android.md.display.activity.UserDetailActivity;
import org.cnodejs.android.md.display.widget.CNodeWebView;
import org.cnodejs.android.md.display.widget.ThemeUtils;
import org.cnodejs.android.md.util.FormatUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final Activity activity;
    private final LayoutInflater inflater;
    private final List<Message> messageList;

    public NotificationAdapter(@NonNull Activity activity, @NonNull List<Message> messageList) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        this.messageList = messageList;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.activity_notification_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.notification_item_img_avatar)
        protected ImageView imgAvatar;

        @Bind(R.id.notification_item_tv_from)
        protected TextView tvFrom;

        @Bind(R.id.notification_item_tv_time)
        protected TextView tvTime;

        @Bind(R.id.notification_item_tv_action)
        protected TextView tvAction;

        @Bind(R.id.notification_item_web_reply_content)
        protected CNodeWebView webReplyContent;

        @Bind(R.id.notification_item_tv_topic_title)
        protected TextView tvTopicTitle;

        private Message message;

        protected ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void update(int position) {
            message = messageList.get(position);

            Picasso.with(activity).load(message.getAuthor().getAvatarUrl()).placeholder(R.drawable.image_placeholder).into(imgAvatar);
            tvFrom.setText(message.getAuthor().getLoginName());
            tvTime.setText(FormatUtils.getRecentlyTimeText(message.getReply().getCreateAt()));
            tvTopicTitle.setText("话题：" + message.getTopic().getTitle());

            // 判断通知类型
            if (message.getType() == Message.Type.at) {
                if (message.getReply() == null || message.getReply().isEmptyContent()) {
                    tvAction.setText("在话题中@了您");
                    webReplyContent.setVisibility(View.GONE);
                } else {
                    tvAction.setText("在回复中@了您");
                    webReplyContent.setVisibility(View.VISIBLE);
                    webReplyContent.loadRenderedContent(message.getReply().getHandleContent());  // 这里直接使用WebView，有性能问题
                }
            } else {
                tvAction.setText("回复了您的话题");
                webReplyContent.setVisibility(View.VISIBLE);
                webReplyContent.loadRenderedContent(message.getReply().getHandleContent());  // 这里直接使用WebView，有性能问题
            }

            // 消息状态
            if (message.isRead()) { // 已读
                tvTime.setTextColor(ThemeUtils.getThemeAttrColor(activity, android.R.attr.textColorSecondary));
                tvFrom.getPaint().setFakeBoldText(false);
                tvFrom.setTextColor(ThemeUtils.getThemeAttrColor(activity, android.R.attr.textColorSecondary));
                tvAction.getPaint().setFakeBoldText(false);
                tvAction.setTextColor(ThemeUtils.getThemeAttrColor(activity, android.R.attr.textColorSecondary));
                tvTopicTitle.getPaint().setFakeBoldText(false);
                tvTopicTitle.setTextColor(ThemeUtils.getThemeAttrColor(activity, android.R.attr.textColorSecondary));
            } else { // 未读
                tvTime.setTextColor(ThemeUtils.getThemeAttrColor(activity, R.attr.colorAccent));
                tvFrom.getPaint().setFakeBoldText(true);
                tvFrom.setTextColor(ThemeUtils.getThemeAttrColor(activity, android.R.attr.textColorPrimary));
                tvAction.getPaint().setFakeBoldText(true);
                tvAction.setTextColor(ThemeUtils.getThemeAttrColor(activity, android.R.attr.textColorPrimary));
                tvTopicTitle.getPaint().setFakeBoldText(true);
                tvTopicTitle.setTextColor(ThemeUtils.getThemeAttrColor(activity, android.R.attr.textColorPrimary));
            }
        }

        @OnClick(R.id.notification_item_img_avatar)
        protected void onBtnAvatarClick() {
            UserDetailActivity.startWithTransitionAnimation(activity, message.getAuthor().getLoginName(), imgAvatar, message.getAuthor().getAvatarUrl());
        }

        @OnClick(R.id.notification_item_btn_item)
        protected void onBtnItemClick() {
            TopicActivity.start(activity, message.getTopic().getId());
        }

    }

}
