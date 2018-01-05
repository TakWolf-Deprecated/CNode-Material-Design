package org.cnodejs.android.md.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.entity.TopicSimple;
import org.cnodejs.android.md.ui.adapter.TopicSimpleListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicSimpleListFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private TopicSimpleListAdapter adapter;

    @Nullable
    private List<TopicSimple> topicSimpleList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topic_simple_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (getActivity() != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new TopicSimpleListAdapter(getActivity());
            recyclerView.setAdapter(adapter);

            if (topicSimpleList != null) {
                adapter.setTopicSimpleListWithNotify(topicSimpleList);
                topicSimpleList = null;
            }
        }
    }

    public void notifyDataSetChanged(List<TopicSimple> topicSimpleList) {
        if (adapter == null) {
            this.topicSimpleList = topicSimpleList;
        } else {
            adapter.setTopicSimpleListWithNotify(topicSimpleList);
        }
    }

}
