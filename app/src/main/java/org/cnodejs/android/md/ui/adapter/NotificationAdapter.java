package org.cnodejs.android.md.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.entity.Message;
import org.cnodejs.android.md.ui.activity.TopicActivity;
import org.cnodejs.android.md.ui.activity.UserDetailActivity;
import org.cnodejs.android.md.ui.widget.CNodeWebView;
import org.cnodejs.android.md.util.FormatUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Message> messageList;

    public NotificationAdapter(Context context, @NonNull List<Message> messageList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
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

            Picasso.with(context).load(message.getAuthor().getAvatarUrl()).placeholder(R.drawable.image_placeholder).into(imgAvatar);
            tvFrom.setText(message.getAuthor().getLoginName());
            tvTime.setText(FormatUtils.getRecentlyTimeText(message.getReply().getCreateAt()));
            tvTopicTitle.setText("话题：" + message.getTopic().getTitle());

            // 判断通知类型
            if (message.getType() == Message.Type.at) {
                if (message.getReply() == null || TextUtils.isEmpty(message.getReply().getContent())) {
                    tvAction.setText("在话题中@了您");
                    webReplyContent.setVisibility(View.GONE);
                } else {
                    tvAction.setText("在回复中@了您");
                    webReplyContent.setVisibility(View.VISIBLE);
                    webReplyContent.loadRenderedContent(message.getReply().getRenderedContent());  // TODO 这里直接使用WebView，有性能问题
                }
            } else {
                tvAction.setText("回复了您的话题");
                webReplyContent.setVisibility(View.VISIBLE);
                webReplyContent.loadRenderedContent(message.getReply().getRenderedContent());  // TODO 这里直接使用WebView，有性能问题
            }

            // 已读未读状态
            tvTime.setTextColor(context.getResources().getColor(message.isRead() ? R.color.text_color_secondary : R.color.color_accent));
            tvFrom.getPaint().setFakeBoldText(!message.isRead());
            tvFrom.setTextColor(context.getResources().getColor(message.isRead() ? R.color.text_color_primary : R.color.text_color_primary));
            tvAction.getPaint().setFakeBoldText(!message.isRead());
            tvAction.setTextColor(context.getResources().getColor(message.isRead() ? R.color.text_color_secondary : R.color.text_color_primary));
            tvTopicTitle.getPaint().setFakeBoldText(!message.isRead());
        }

        @OnClick(R.id.notification_item_img_avatar)
        protected void onBtnAvatarClick() {
            UserDetailActivity.open(context, message.getAuthor().getLoginName());
        }

        @OnClick(R.id.notification_item_btn_item)
        protected void onBtnItemClick() {
            TopicActivity.open(context, message.getTopic().getId());
        }

    }

}
