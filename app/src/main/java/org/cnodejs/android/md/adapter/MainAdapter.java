package org.cnodejs.android.md.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.cnodejs.android.md.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;

    private int unread = 5;

    public MainAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.activity_main_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.main_item_img_avatar)
        protected ImageView imgAvatar;

        @Bind(R.id.main_item_tv_from)
        protected TextView tvFrom;

        @Bind(R.id.main_item_tv_time)
        protected TextView tvTime;

        @Bind(R.id.main_item_tv_content)
        protected TextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void update(int position) {
            tvTime.setText(position + "天前");
            tvTime.setTextColor(context.getResources().getColor(position < unread ? R.color.color_primary : R.color.text_color_secondary));
            tvFrom.setText("昵称");
            tvFrom.getPaint().setFakeBoldText(position < unread);
            tvFrom.setTextColor(context.getResources().getColor(position < unread ? R.color.text_color_primary : R.color.text_color_secondary));
            tvContent.setText("内容");
            tvContent.getPaint().setFakeBoldText(position < unread);
            tvContent.setTextColor(context.getResources().getColor(position < unread ? R.color.text_color_primary : R.color.text_color_secondary));
        }

    }

}
