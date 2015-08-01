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

    private int unread = 5;

    public MainAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public MainAdapter(Context context, List<Topic> topicList) {
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



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void update(int position) {

        }

    }

}
