package org.cnodejs.android.md.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    private Context context;
    private LayoutInflater inflater;
    private TopicWithReply topic;

    private boolean isHeaderShow = false; // TODO 当false时，渲染header，其他时间不渲染

    public interface OnAtClickListener {

        void onAt(String loginName);

    }

    private OnAtClickListener onAtClickListener;

    public TopicAdapter(Context context, @NonNull OnAtClickListener onAtClickListener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.onAtClickListener = onAtClickListener;
    }

    public void setTopic(TopicWithReply topic) {
        this.topic = topic;
        isHeaderShow = false;
    }

    @Override
    public int getItemCount() {
        return topic == null ? 0 : topic.getReplies().size() + 1;
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
                tvTab.setBackgroundResource(topic.isTop() ? R.drawable.topic_tab_top_background : R.drawable.topic_tab_normal_background);
                tvTab.setTextColor(context.getResources().getColor(topic.isTop() ? android.R.color.white : R.color.text_color_secondary));
                tvVisitCount.setText(topic.getVisitCount() + "次浏览");
                Picasso.with(context).load(topic.getAuthor().getAvatarUrl()).placeholder(R.drawable.image_placeholder).into(imgAvatar);
                tvLoginName.setText(topic.getAuthor().getLoginName());
                tvCreateTime.setText(context.getString(R.string.post_at_$) + FormatUtils.getRecentlyTimeText(topic.getCreateAt()));
                iconGood.setVisibility(topic.isGood() ? View.VISIBLE : View.GONE);

                // TODO 这里直接使用WebView，有性能问题
                webReplyContent.loadRenderedContent(topic.getRenderedContent());

                isHeaderShow = true;
            }

            layoutNoReply.setVisibility(topic.getReplies().size() > 0 ? View.GONE : View.VISIBLE);
        }

        @OnClick(R.id.topic_item_header_img_avatar)
        protected void onBtnAvatarClick() {
            UserDetailActivity.open(context, topic.getAuthor().getLoginName());
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
            reply = topic.getReplies().get(position);

            Picasso.with(context).load(reply.getAuthor().getAvatarUrl()).placeholder(R.drawable.image_placeholder).into(imgAvatar);
            tvLoginName.setText(reply.getAuthor().getLoginName());
            tvIndex.setText(position + 1 + "楼");
            tvCreateTime.setText(FormatUtils.getRecentlyTimeText(reply.getCreateAt()));
            btnUps.setText(String.valueOf(reply.getUps().size()));
            btnUps.setCompoundDrawablesWithIntrinsicBounds(reply.getUps().contains(LoginShared.getId(context)) ? R.drawable.main_nav_ic_good_theme_24dp : R.drawable.main_nav_ic_good_grey_24dp, 0, 0, 0);
            iconDeepLine.setVisibility(position == topic.getReplies().size() - 1 ? View.GONE : View.VISIBLE);
            iconShadowGap.setVisibility(position == topic.getReplies().size() - 1 ? View.VISIBLE : View.GONE);

            // TODO 这里直接使用WebView，有性能问题
            webReplyContent.loadRenderedContent(reply.getRenderedContent());
        }

        @OnClick(R.id.topic_item_reply_img_avatar)
        protected void onBtnAvatarClick() {
            UserDetailActivity.open(context, reply.getAuthor().getLoginName());
        }

        @OnClick(R.id.topic_item_reply_btn_ups)
        protected void onBtnUpsClick() {
            if (TextUtils.isEmpty(LoginShared.getAccessToken(context))) {
                showNeedLoginDialog();
            } else if (reply.getAuthor().getLoginName().equals(LoginShared.getLoginName(context))) {
                Toast.makeText(context, "不能帮自己点赞", Toast.LENGTH_SHORT).show();
            } else {
                upTopicAsyncTask(this);
            }
        }

        @OnClick(R.id.topic_item_reply_btn_at)
        protected void onBtnAtClick() {
            if (TextUtils.isEmpty(LoginShared.getAccessToken(context))) {
                showNeedLoginDialog();
            } else {
                onAtClickListener.onAt(reply.getAuthor().getLoginName());
            }
        }

    }

    public void showNeedLoginDialog() {
        new MaterialDialog.Builder(context)
                .content(R.string.need_login_tip)
                .positiveText(R.string.login)
                .negativeText(R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        context.startActivity(new Intent(context, LoginActivity.class));
                    }

                })
                .show();
    }

    public void showAccessTokenErrorDialog() {
        new MaterialDialog.Builder(context)
                .content(R.string.access_token_error_tip)
                .positiveText(R.string.confirm)
                .show();
    }

    private void upTopicAsyncTask(final ReplyViewHolder holder) {
        final int position = holder.position; // 标记当时的位置信息
        final Reply reply = holder.reply; // 保存当时的回复对象
        ApiClient.service.upTopic(LoginShared.getAccessToken(context), holder.reply.getId(), new Callback<TopicUpInfo>() {

            @Override
            public void success(TopicUpInfo info, Response response) {
                if (info.getAction() == TopicUpInfo.Action.up) {
                    reply.getUps().add(LoginShared.getId(context));
                } else if (info.getAction() == TopicUpInfo.Action.down) {
                    reply.getUps().remove(LoginShared.getId(context));
                }
                // 如果位置没有变，则更新界面
                if (position == holder.position) {
                    holder.btnUps.setText(String.valueOf(holder.reply.getUps().size()));
                    holder.btnUps.setCompoundDrawablesWithIntrinsicBounds(holder.reply.getUps().contains(LoginShared.getId(context)) ? R.drawable.main_nav_ic_good_theme_24dp : R.drawable.main_nav_ic_good_grey_24dp, 0, 0, 0);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getResponse() != null && error.getResponse().getStatus() == 403) {
                    showAccessTokenErrorDialog();
                } else {
                    Toast.makeText(context, "网络访问失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

}
