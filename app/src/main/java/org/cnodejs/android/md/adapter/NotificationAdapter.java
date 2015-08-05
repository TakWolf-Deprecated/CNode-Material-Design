package org.cnodejs.android.md.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.activity.MarkdownPreviewActivity;
import org.cnodejs.android.md.activity.TopicActivity;
import org.cnodejs.android.md.activity.UserDetailActivity;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.entity.Message;
import org.cnodejs.android.md.model.entity.MessageType;
import org.cnodejs.android.md.util.FormatUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import us.feras.mdv.util.HttpHelper;

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

        @Bind(R.id.notification_item_tv_reply_content)
        protected TextView tvReplyContent;

        @Bind(R.id.notification_item_tv_topic_title)
        protected TextView tvTopicTitle;

        private Message message;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void update(int position) {
            message = messageList.get(position);

            Picasso.with(context).load(ApiClient.ROOT_HOST + message.getAuthor().getAvatarUrl()).error(R.drawable.image_default).into(imgAvatar);
            tvFrom.setText(message.getAuthor().getLoginName());
            tvAction.setText(message.getType() == MessageType.at ? "在回复中@了您" : "回复了您的话题");
            tvTime.setText(FormatUtils.getRecentlyTimeFormatText(message.getReply().getCreateAt()));
            tvTopicTitle.setText("原话题：" + message.getTopic().getTitle());

            // TODO 这里显示html文本
            tvReplyContent.setText(message.getReply().makeSureRenderAndGetSpannedContent());

            // 已读未读状态
            tvTime.setTextColor(context.getResources().getColor(message.isRead() ? R.color.text_color_secondary : R.color.color_accent));
            tvFrom.getPaint().setFakeBoldText(!message.isRead());
            tvFrom.setTextColor(context.getResources().getColor(message.isRead() ? R.color.text_color_primary : R.color.text_color_primary));
            tvAction.getPaint().setFakeBoldText(!message.isRead());
            tvAction.setTextColor(context.getResources().getColor(message.isRead() ? R.color.text_color_secondary : R.color.text_color_primary));
            tvReplyContent.getPaint().setFakeBoldText(!message.isRead());
            tvTopicTitle.getPaint().setFakeBoldText(!message.isRead());
        }

        @OnClick(R.id.notification_item_img_avatar)
        protected void onBtnAvatarClick() {
            Intent intent = new Intent(context, UserDetailActivity.class);
            intent.putExtra("loginName", message.getAuthor().getLoginName());
            context.startActivity(intent);
        }

        @OnClick(R.id.notification_item_btn_item)
        protected void onBtnItemClick() {
            Intent intent = new Intent(context, TopicActivity.class);
            intent.putExtra("topicId", message.getTopic().getId());
            context.startActivity(intent);
        }

        @OnLongClick(R.id.notification_item_btn_item)
        protected boolean onBtnItemLongClick() {
            final MaterialDialog dialog = new MaterialDialog.Builder(context)
                    .customView(R.layout.dialog_markdown_preview, false)
                    .build();
            ButterKnife.findById(dialog.getCustomView(), R.id.dialog_markdown_preview_btn).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(context, MarkdownPreviewActivity.class);
                    intent.putExtra("markdownText", message.getReply().getContent());
                    context.startActivity(intent);
                }

            });
            dialog.show();
            return true;
        }

    }

}
