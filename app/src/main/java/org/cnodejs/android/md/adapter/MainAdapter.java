package org.cnodejs.android.md.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.activity.MainActivity;
import org.cnodejs.android.md.model.entity.Topic;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Topic> topicList;

    public MainAdapter(Context context, List<Topic> topicList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.topicList = topicList;
    }

    @Override
    public int getItemCount() {
        return topicList == null ? 0 : topicList.size();
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

        @Bind(R.id.main_item_tv_tab)
        protected TextView tvTab;

        @Bind(R.id.main_item_tv_title)
        protected TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void update(int position) {
            Topic topic = topicList.get(position);
            tvTitle.setText(topic.getTitle());
            tvTab.setText(topic.isTop() ? "置顶" : context.getString(topic.getTab().getNameId()));
            tvTab.setBackgroundResource(topic.isTop() ? R.drawable.topic_tab_top_background : R.drawable.topic_tab_normal_background);
            tvTab.setTextColor(context.getResources().getColor(topic.isTop() ? android.R.color.white : R.color.text_color_secondary));
        }

    }

}
