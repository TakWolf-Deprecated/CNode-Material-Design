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
import org.cnodejs.android.md.display.activity.LoginActivity;
import org.cnodejs.android.md.display.activity.UserDetailActivity;
import org.cnodejs.android.md.display.widget.CNodeWebView;
import org.cnodejs.android.md.display.widget.ThemeUtils;
import org.cnodejs.android.md.display.widget.ToastUtils;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.DefaultToastCallback;
import org.cnodejs.android.md.model.entity.Reply;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.TopicWithReply;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.util.FormatUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_REPLY = 1;

    private final Activity activity;
    private final LayoutInflater inflater;
    private TopicWithReply topic;

    private boolean isHeaderShow = false; // 当false时，渲染header，其他时间不渲染

    public interface OnAtClickListener {

        void onAt(String loginName);

    }

    private final OnAtClickListener onAtClickListener;

    public TopicAdapter(@NonNull Activity activity, @NonNull OnAtClickListener onAtClickListener) {
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

        @Bind(R.id.topic_item_header_icon_good)
        protected View iconGood;

        @Bind(R.id.topic_item_header_img_avatar)
        protected ImageView imgAvatar;

        @Bind(R.id.topic_item_header_tv_tab)
        protected TextView tvTab;

        @Bind(R.id.topic_item_header_tv_login_name)
        protected TextView tvLoginName;

        @Bind(R.id.topic_item_header_tv_create_time)
        protected TextView tvCreateTime;

        @Bind(R.id.topic_item_header_tv_visit_count)
        protected TextView tvVisitCount;

        @Bind(R.id.topic_item_header_btn_favorite)
        protected ImageView btnFavorite;

        @Bind(R.id.topic_item_header_web_content)
        protected CNodeWebView webContent;

        @Bind(R.id.topic_item_header_layout_no_reply)
        protected ViewGroup layoutNoReply;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void update(int position) {
            if (!isHeaderShow) {
                tvTitle.setText(topic.getTitle());
                iconGood.setVisibility(topic.isGood() ? View.VISIBLE : View.GONE);
                Glide.with(activity).load(topic.getAuthor().getAvatarUrl()).placeholder(R.drawable.image_placeholder).dontAnimate().into(imgAvatar);
                tvTab.setText(topic.isTop() ? R.string.tab_top : topic.getTab().getNameId());
                tvTab.setBackgroundDrawable(ThemeUtils.getThemeAttrDrawable(activity, topic.isTop() ? R.attr.referenceBackgroundAccent : R.attr.referenceBackgroundNormal));
                tvTab.setTextColor(topic.isTop() ? Color.WHITE : ThemeUtils.getThemeAttrColor(activity, android.R.attr.textColorSecondary));
                tvLoginName.setText(topic.getAuthor().getLoginName());
                tvCreateTime.setText(FormatUtils.getRecentlyTimeText(topic.getCreateAt()) + "创建");
                tvVisitCount.setText(topic.getVisitCount() + "次浏览");
                btnFavorite.setImageResource(topic.isCollect() ? R.drawable.ic_favorite_theme_24dp : R.drawable.ic_favorite_outline_grey600_24dp);

                // 这里直接使用WebView，有性能问题
                webContent.loadRenderedContent(topic.getHandleContent());

                isHeaderShow = true;
            }

            layoutNoReply.setVisibility(topic.getReplyList().size() > 0 ? View.GONE : View.VISIBLE);
        }

        @OnClick(R.id.topic_item_header_img_avatar)
        protected void onBtnAvatarClick() {
            UserDetailActivity.startWithTransitionAnimation(activity, topic.getAuthor().getLoginName(), imgAvatar, topic.getAuthor().getAvatarUrl());
        }

        @OnClick(R.id.topic_item_header_btn_favorite)
        protected void onBtnFavoriteClick() {
            if (topic != null) {
                if (LoginActivity.startForResultWithAccessTokenCheck(activity)) {
                    if (topic.isCollect()) {
                        decollectTopicAsyncTask(topic, btnFavorite);
                    } else {
                        collectTopicAsyncTask(topic, btnFavorite);
                    }
                }
            }
        }

    }

    private void collectTopicAsyncTask(@NonNull final TopicWithReply topic, @NonNull final ImageView btnFavorite) {
        Call<Result> call = ApiClient.service.collectTopic(LoginShared.getAccessToken(activity), topic.getId());
        call.enqueue(new DefaultToastCallback<Result>(activity) {

            @Override
            public boolean onResultOk(Response<Result> response, Result result) {
                if (!activity.isFinishing()) {
                    topic.setCollect(true);
                    btnFavorite.setImageResource(R.drawable.ic_favorite_theme_24dp);
                }
                return false;
            }

        });
    }

    private void decollectTopicAsyncTask(@NonNull final TopicWithReply topic, @NonNull final ImageView btnFavorite) {
        Call<Result> call = ApiClient.service.decollectTopic(LoginShared.getAccessToken(activity), topic.getId());
        call.enqueue(new DefaultToastCallback<Result>(activity) {

            @Override
            public boolean onResultOk(Response<Result> response, Result result) {
                if (!activity.isFinishing()) {
                    topic.setCollect(false);
                    btnFavorite.setImageResource(R.drawable.ic_favorite_outline_grey600_24dp);
                }
                return false;
            }

        });
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
        protected CNodeWebView webContent;

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

            Glide.with(activity).load(reply.getAuthor().getAvatarUrl()).placeholder(R.drawable.image_placeholder).dontAnimate().into(imgAvatar);
            tvLoginName.setText(reply.getAuthor().getLoginName());
            tvIndex.setText(position + 1 + "楼");
            tvCreateTime.setText(FormatUtils.getRecentlyTimeText(reply.getCreateAt()));
            btnUps.setText(String.valueOf(reply.getUpList().size()));
            btnUps.setCompoundDrawablesWithIntrinsicBounds(reply.getUpList().contains(LoginShared.getId(activity)) ? R.drawable.ic_thumb_up_theme_24dp : R.drawable.ic_thumb_up_grey600_24dp, 0, 0, 0);
            iconDeepLine.setVisibility(position == topic.getReplyList().size() - 1 ? View.GONE : View.VISIBLE);
            iconShadowGap.setVisibility(position == topic.getReplyList().size() - 1 ? View.VISIBLE : View.GONE);

            // 这里直接使用WebView，有性能问题
            webContent.loadRenderedContent(reply.getHandleContent());
        }

        @OnClick(R.id.topic_item_reply_img_avatar)
        protected void onBtnAvatarClick() {
            UserDetailActivity.startWithTransitionAnimation(activity, reply.getAuthor().getLoginName(), imgAvatar, reply.getAuthor().getAvatarUrl());
        }

        @OnClick(R.id.topic_item_reply_btn_ups)
        protected void onBtnUpsClick() {
            if (LoginActivity.startForResultWithAccessTokenCheck(activity)) {
                if (reply.getAuthor().getLoginName().equals(LoginShared.getLoginName(activity))) {
                    ToastUtils.with(activity).show(R.string.can_not_up_yourself_reply);
                } else {
                    upReplyAsyncTask(this);
                }
            }
        }

        @OnClick(R.id.topic_item_reply_btn_at)
        protected void onBtnAtClick() {
            if (LoginActivity.startForResultWithAccessTokenCheck(activity)) {
                onAtClickListener.onAt(reply.getAuthor().getLoginName());
            }
        }

    }

    private void upReplyAsyncTask(final ReplyViewHolder holder) {
        final int position = holder.position; // 标记当时的位置信息
        final Reply reply = holder.reply; // 保存当时的回复对象
        Call<Result.UpReply> call = ApiClient.service.upReply(holder.reply.getId(), LoginShared.getAccessToken(activity));
        call.enqueue(new DefaultToastCallback<Result.UpReply>(activity) {

            @Override
            public boolean onResultOk(Response<Result.UpReply> response, Result.UpReply result) {
                if (!activity.isFinishing()) {
                    if (result.getAction() == Reply.UpAction.up) {
                        reply.getUpList().add(LoginShared.getId(activity));
                    } else if (result.getAction() == Reply.UpAction.down) {
                        reply.getUpList().remove(LoginShared.getId(activity));
                    }
                    // 如果位置没有变，则更新界面
                    if (position == holder.position) {
                        holder.btnUps.setText(String.valueOf(holder.reply.getUpList().size()));
                        holder.btnUps.setCompoundDrawablesWithIntrinsicBounds(holder.reply.getUpList().contains(LoginShared.getId(activity)) ? R.drawable.ic_thumb_up_theme_24dp : R.drawable.ic_thumb_up_grey600_24dp, 0, 0, 0);
                    }
                }
                return false;
            }

        });
    }

}
