package org.cnodejs.android.md.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.entity.Reply;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.presenter.contract.IReplyPresenter;
import org.cnodejs.android.md.presenter.implement.ReplyPresenter;
import org.cnodejs.android.md.ui.activity.LoginActivity;
import org.cnodejs.android.md.ui.activity.UserDetailActivity;
import org.cnodejs.android.md.ui.util.ToastUtils;
import org.cnodejs.android.md.ui.view.ICreateReplyView;
import org.cnodejs.android.md.ui.view.IReplyView;
import org.cnodejs.android.md.ui.widget.ContentWebView;
import org.cnodejs.android.md.util.FormatUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReplyListAdapter extends RecyclerView.Adapter<ReplyListAdapter.ViewHolder> implements IReplyView {

    private final Activity activity;
    private final LayoutInflater inflater;
    private final List<Reply> replyList = new ArrayList<>();
    private final Map<String, Integer> positionMap = new HashMap<>();
    private final ICreateReplyView createReplyView;
    private final IReplyPresenter replyPresenter;

    public ReplyListAdapter(@NonNull Activity activity, @NonNull ICreateReplyView createReplyView) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        this.createReplyView = createReplyView;
        replyPresenter = new ReplyPresenter(activity, this);
    }

    @NonNull
    public List<Reply> getReplyList() {
        return replyList;
    }

    public void setReplyListAndNotify(@NonNull List<Reply> replyList) {
        this.replyList.clear();
        this.replyList.addAll(replyList);
        positionMap.clear();
        for (int n = 0; n < replyList.size(); n++) {
            Reply reply = replyList.get(n);
            positionMap.put(reply.getId(), n);
        }
        notifyDataSetChanged();
    }

    public void appendReplyAndNotify(@NonNull Reply reply) {
        replyList.add(reply);
        positionMap.put(reply.getId(), replyList.size() - 1);
        notifyItemInserted(replyList.size() - 1);
    }

    @Override
    public void onUpReplyOk(@NonNull Reply reply) {
        for (int position = 0; position < replyList.size(); position++) {
            Reply replyAtPosition = replyList.get(position);
            if (TextUtils.equals(reply.getId(), replyAtPosition.getId())) {
                notifyItemChanged(position, reply);
            }
        }
    }

    @Override
    public int getItemCount() {
        return replyList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_reply, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            for (Object payload : payloads) {
                if (payload instanceof Reply) { // 更新点赞状态
                    Reply reply = (Reply) payload;
                    holder.updateUpViews(reply);
                }
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_avatar)
        ImageView imgAvatar;

        @BindView(R.id.tv_login_name)
        TextView tvLoginName;

        @BindView(R.id.tv_index)
        TextView tvIndex;

        @BindView(R.id.tv_create_time)
        TextView tvCreateTime;

        @BindView(R.id.btn_ups)
        TextView btnUps;

        @BindView(R.id.tv_target_position)
        TextView tvTargetPosition;

        @BindView(R.id.web_content)
        ContentWebView webContent;

        @BindView(R.id.icon_deep_line)
        View iconDeepLine;

        @BindView(R.id.icon_shadow_gap)
        View iconShadowGap;

        private Reply reply;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void update(int position) {
            reply = replyList.get(position);
            updateReplyViews(reply, position, positionMap.get(reply.getReplyId()));
        }

        void updateReplyViews(@NonNull Reply reply, int position, @Nullable Integer targetPosition) {
            Glide.with(activity).load(reply.getAuthor().getAvatarUrl()).placeholder(R.drawable.image_placeholder).dontAnimate().into(imgAvatar);
            tvLoginName.setText(reply.getAuthor().getLoginName());
            tvIndex.setText(activity.getString(R.string.$d_floor, position + 1));
            tvCreateTime.setText(FormatUtils.getRelativeTimeSpanString(reply.getCreateAt()));
            updateUpViews(reply);
            if (targetPosition == null) {
                tvTargetPosition.setVisibility(View.GONE);
            } else {
                tvTargetPosition.setVisibility(View.VISIBLE);
                tvTargetPosition.setText(activity.getString(R.string.reply_$d_floor, targetPosition + 1));
            }

            // 这里直接使用WebView，有性能问题
            webContent.loadRenderedContent(reply.getContentHtml());

            iconDeepLine.setVisibility(position == replyList.size() - 1 ? View.GONE : View.VISIBLE);
            iconShadowGap.setVisibility(position == replyList.size() - 1 ? View.VISIBLE : View.GONE);
        }

        void updateUpViews(@NonNull Reply reply) {
            btnUps.setText(String.valueOf(reply.getUpList().size()));
            btnUps.setCompoundDrawablesWithIntrinsicBounds(reply.getUpList().contains(LoginShared.getId(activity)) ? R.drawable.ic_thumb_up_theme_24dp : R.drawable.ic_thumb_up_grey600_24dp, 0, 0, 0);
        }

        @OnClick(R.id.img_avatar)
        void onBtnAvatarClick() {
            UserDetailActivity.startWithTransitionAnimation(activity, reply.getAuthor().getLoginName(), imgAvatar, reply.getAuthor().getAvatarUrl());
        }

        @OnClick(R.id.btn_ups)
        void onBtnUpsClick() {
            if (LoginActivity.checkLogin(activity)) {
                if (reply.getAuthor().getLoginName().equals(LoginShared.getLoginName(activity))) {
                    ToastUtils.with(activity).show(R.string.can_not_up_yourself_reply);
                } else {
                    replyPresenter.upReplyAsyncTask(reply);
                }
            }
        }

        @OnClick(R.id.btn_at)
        void onBtnAtClick() {
            if (LoginActivity.checkLogin(activity)) {
                createReplyView.onAt(reply, positionMap.get(reply.getId()));
            }
        }

    }

}
