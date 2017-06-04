package org.cnodejs.android.md.ui.fragment;

import android.os.Bundle;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topic_simple_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TopicSimpleListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
    }

    public void notifyDataSetChanged(List<TopicSimple> topicSimpleList) {
        adapter.getTopicSimpleList().clear();
        adapter.getTopicSimpleList().addAll(topicSimpleList);
        adapter.notifyDataSetChanged();
    }

}
