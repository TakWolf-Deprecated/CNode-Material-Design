package org.cnodejs.android.md.ui.viewholder;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.entity.TopicSimple;
import org.cnodejs.android.md.ui.adapter.TopicSimpleListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicSimpleListController {

    public static TopicSimpleListController assertType(@NonNull Object object) {
        if (object instanceof TopicSimpleListController) {
            return (TopicSimpleListController) object;
        } else {
            throw new AssertionError("Impossible controller type.");
        }
    }

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private final View contentView;
    private final TopicSimpleListAdapter adapter;

    public TopicSimpleListController(@NonNull Activity activity, @NonNull ViewPager viewPager) {
        contentView = LayoutInflater.from(activity).inflate(R.layout.controller_topic_simple_list, viewPager, false);
        ButterKnife.bind(this, contentView);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new TopicSimpleListAdapter(activity);
        recyclerView.setAdapter(adapter);
    }

    @NonNull
    public View getContentView() {
        return contentView;
    }

    public void setTopicSimpleList(@NonNull List<TopicSimple> topicSimpleList) {
        adapter.setTopicSimpleListWithNotify(topicSimpleList);
    }

}
