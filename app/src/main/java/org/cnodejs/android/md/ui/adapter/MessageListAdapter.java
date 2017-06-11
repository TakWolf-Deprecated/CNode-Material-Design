package org.cnodejs.android.md.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class MessageListAdapter extends BaseAdapter {

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
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_message, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.update(messageList.get(position));
        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.img_avatar)
        ImageView imgAvatar;

        @BindView(R.id.tv_from)
        TextView tvFrom;

        @BindView(R.id.tv_time)
        TextView tvTime;

        @BindView(R.id.tv_action)
        TextView tvAction;

        @BindView(R.id.badge_read)
        View badgeRead;

        @BindView(R.id.web_content)
        ContentWebView webContent;

        @BindView(R.id.tv_topic_title)
        TextView tvTopicTitle;

        private Message message;

        ViewHolder(@NonNull View itemView) {
            ButterKnife.bind(this, itemView);
        }

        void update(@NonNull Message message) {
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
        void onBtnAvatarClick() {
            UserDetailActivity.startWithTransitionAnimation(activity, message.getAuthor().getLoginName(), imgAvatar, message.getAuthor().getAvatarUrl());
        }

        @OnClick(R.id.btn_item)
        void onBtnItemClick() {
            Navigator.TopicWithAutoCompat.start(activity, message.getTopic().getId());
        }

    }

}
