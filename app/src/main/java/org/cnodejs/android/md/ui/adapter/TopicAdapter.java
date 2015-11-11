package org.cnodejs.android.md.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.entity.Reply;
import org.cnodejs.android.md.model.entity.TopicUpInfo;
import org.cnodejs.android.md.model.entity.TopicWithReply;
import org.cnodejs.android.md.storage.LoginShared;
import org.cnodejs.android.md.ui.activity.LoginActivity;
import org.cnodejs.android.md.ui.activity.UserDetailActivity;
import org.cnodejs.android.md.ui.widget.CNodeWebView;
import org.cnodejs.android.md.ui.widget.ThemeUtils;
import org.cnodejs.android.md.ui.widget.ToastUtils;
import org.cnodejs.android.md.util.FormatUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_REPLY = 1;

    private Activity activity;
    private LayoutInflater inflater;
    private TopicWithReply topic;

    private boolean isHeaderShow = false; // TODO 当false时，渲染header，其他时间不渲染

    public interface OnAtClickListener {

        void onAt(String loginName);

    }

    private OnAtClickListener onAtClickListener;

    public TopicAdapter(Activity activity, @NonNull OnAtClickListener onAtClickListener) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        this.onAtClickListener = onAtClickListener;
    }

    public void setTopic(TopicWithReply topic) {
        this.topic = topic;
        isHeaderShow = false;
    }

    @Override
    public int getItemCount() {
        return topic == null ? 0 : topic.getReplyList().size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (topic != null && position != 0) {
            return TYPE_REPLY;
        } else {
            return TYPE_HEADER;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderViewHolder(inflater.inflate(R.layout.activity_topic_item_header, parent, false));
            default: // TYPE_REPLY
                return new ReplyViewHolder(inflater.inflate(R.layout.activity_topic_item_reply, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                holder.update(position);
                break;
            default: // TYPE_REPLY
                holder.update(position - 1);
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected ViewHolder(View itemView) {
            super(itemView);
        }

        protected void update(int position) {}

    }

    public class HeaderViewHolder extends ViewHolder {

        @Bind(R.id.topic_item_header_tv_title)
        protected TextView tvTitle;

        @Bind(R.id.topic_item_header_tv_tab)
        protected TextView tvTab;

        @Bind(R.id.topic_item_header_tv_visit_count)
        protected TextView tvVisitCount;

        @Bind(R.id.topic_item_header_img_avatar)
        protected ImageView imgAvatar;

        @Bind(R.id.topic_item_header_tv_login_name)
        protected TextView tvLoginName;

        @Bind(R.id.topic_item_header_tv_create_time)
        protected TextView tvCreateTime;

        @Bind(R.id.topic_item_header_web_content)
        protected CNodeWebView webReplyContent;

        @Bind(R.id.topic_item_header_icon_good)
        protected View iconGood;

        @Bind(R.id.topic_item_header_layout_no_reply)
        protected ViewGroup layoutNoReply;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void update(int position) {
            if (!isHeaderShow) {
                tvTitle.setText(topic.getTitle());
                tvTab.setText(topic.isTop() ? R.string.tab_top : topic.getTab().getNameId());
                tvTab.setBackgroundDrawable(ThemeUtils.getThemeAttrDrawable(activity, topic.isTop() ? R.attr.referenceBackgroundAccent : R.attr.referenceBackgroundNormal));
                tvTab.setTextColor(topic.isTop() ? Color.WHITE : ThemeUtils.getThemeAttrColor(activity, android.R.attr.textColorSecondary));
                tvVisitCount.setText(topic.getVisitCount() + "次浏览");
                Picasso.with(activity).load(topic.getAuthor().getAvatarUrl()).placeholder(R.drawable.image_placeholder).into(imgAvatar);
                tvLoginName.setText(topic.getAuthor().getLoginName());
                tvCreateTime.setText(activity.getString(R.string.post_at_$) + FormatUtils.getRecentlyTimeText(topic.getCreateAt()));
                iconGood.setVisibility(topic.isGood() ? View.VISIBLE : View.GONE);

                // TODO 这里直接使用WebView，有性能问题
                webReplyContent.loadRenderedContent(topic.getHandleContent());

                isHeaderShow = true;
            }

            layoutNoReply.setVisibility(topic.getReplyList().size() > 0 ? View.GONE : View.VISIBLE);
        }

        @OnClick(R.id.topic_item_header_img_avatar)
        protected void onBtnAvatarClick() {
            UserDetailActivity.openWithTransitionAnimation(activity, topic.getAuthor().getLoginName(), imgAvatar, topic.getAuthor().getAvatarUrl());
        }

    }

    public class ReplyViewHolder extends ViewHolder {

        @Bind(R.id.topic_item_reply_img_avatar)
        protected ImageView imgAvatar;

        @Bind(R.id.topic_item_reply_tv_login_name)
        protected TextView tvLoginName;

        @Bind(R.id.topic_item_reply_tv_index)
        protected TextView tvIndex;

        @Bind(R.id.topic_item_reply_tv_create_time)
        protected TextView tvCreateTime;

        @Bind(R.id.topic_item_reply_btn_ups)
        protected TextView btnUps;

        @Bind(R.id.topic_item_reply_web_content)
        protected CNodeWebView webReplyContent;

        @Bind(R.id.topic_item_reply_icon_deep_line)
        protected View iconDeepLine;

        @Bind(R.id.topic_item_reply_icon_shadow_gap)
        protected View iconShadowGap;

        private Reply reply;
        private int position = -1;

        public ReplyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void update(int position) {
            this.position = position;
            reply = topic.getReplyList().get(position);

            Picasso.with(activity).load(reply.getAuthor().getAvatarUrl()).placeholder(R.drawable.image_placeholder).into(imgAvatar);
            tvLoginName.setText(reply.getAuthor().getLoginName());
            tvIndex.setText(position + 1 + "楼");
            tvCreateTime.setText(FormatUtils.getRecentlyTimeText(reply.getCreateAt()));
            btnUps.setText(String.valueOf(reply.getUpList().size()));
            btnUps.setCompoundDrawablesWithIntrinsicBounds(reply.getUpList().contains(LoginShared.getId(activity)) ? R.drawable.ic_thumb_up_theme_24dp : R.drawable.ic_thumb_up_grey600_24dp, 0, 0, 0);
            iconDeepLine.setVisibility(position == topic.getReplyList().size() - 1 ? View.GONE : View.VISIBLE);
            iconShadowGap.setVisibility(position == topic.getReplyList().size() - 1 ? View.VISIBLE : View.GONE);

            // TODO 这里直接使用WebView，有性能问题
            webReplyContent.loadRenderedContent(reply.getHandleContent());
        }

        @OnClick(R.id.topic_item_reply_img_avatar)
        protected void onBtnAvatarClick() {
            UserDetailActivity.openWithTransitionAnimation(activity, reply.getAuthor().getLoginName(), imgAvatar, reply.getAuthor().getAvatarUrl());
        }

        @OnClick(R.id.topic_item_reply_btn_ups)
        protected void onBtnUpsClick() {
            if (TextUtils.isEmpty(LoginShared.getAccessToken(activity))) {
                showNeedLoginDialog();
            } else if (reply.getAuthor().getLoginName().equals(LoginShared.getLoginName(activity))) {
                ToastUtils.with(activity).show("不能帮自己点赞");
            } else {
                upTopicAsyncTask(this);
            }
        }

        @OnClick(R.id.topic_item_reply_btn_at)
        protected void onBtnAtClick() {
            if (TextUtils.isEmpty(LoginShared.getAccessToken(activity))) {
                showNeedLoginDialog();
            } else {
                onAtClickListener.onAt(reply.getAuthor().getLoginName());
            }
        }

    }

    public void showNeedLoginDialog() {
        new MaterialDialog.Builder(activity)
                .content(R.string.need_login_tip)
                .positiveText(R.string.login)
                .onPositive(new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                    }

                })
                .negativeText(R.string.cancel)
                .show();
    }

    public void showAccessTokenErrorDialog() {
        new MaterialDialog.Builder(activity)
                .content(R.string.access_token_error_tip)
                .positiveText(R.string.confirm)
                .show();
    }

    private void upTopicAsyncTask(final ReplyViewHolder holder) {
        final int position = holder.position; // 标记当时的位置信息
        final Reply reply = holder.reply; // 保存当时的回复对象
        ApiClient.service.upTopic(LoginShared.getAccessToken(activity), holder.reply.getId(), new Callback<TopicUpInfo>() {

            @Override
            public void success(TopicUpInfo info, Response response) {
                if (info.getAction() == TopicUpInfo.Action.up) {
                    reply.getUpList().add(LoginShared.getId(activity));
                } else if (info.getAction() == TopicUpInfo.Action.down) {
                    reply.getUpList().remove(LoginShared.getId(activity));
                }
                // 如果位置没有变，则更新界面
                if (position == holder.position) {
                    holder.btnUps.setText(String.valueOf(holder.reply.getUpList().size()));
                    holder.btnUps.setCompoundDrawablesWithIntrinsicBounds(holder.reply.getUpList().contains(LoginShared.getId(activity)) ? R.drawable.ic_thumb_up_theme_24dp : R.drawable.ic_thumb_up_grey600_24dp, 0, 0, 0);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getResponse() != null && error.getResponse().getStatus() == 403) {
                    showAccessTokenErrorDialog();
                } else {
                    ToastUtils.with(activity).show(R.string.network_faild);
                }
            }

        });
    }

}
