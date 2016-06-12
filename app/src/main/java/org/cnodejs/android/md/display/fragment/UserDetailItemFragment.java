package org.cnodejs.android.md.display.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.display.adapter.UserDetailItemAdapter;
import org.cnodejs.android.md.model.entity.TopicSimple;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDetailItemFragment extends Fragment {

    @BindView(R.id.recycler_view)
    protected RecyclerView recyclerView;

    private UserDetailItemAdapter adapter;
    private final List<TopicSimple> topicList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_detail_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new UserDetailItemAdapter(getActivity(), topicList);
        recyclerView.setAdapter(adapter);
    }

    public void notifyDataSetChanged(List<TopicSimple> topicList) {
        this.topicList.clear();
        this.topicList.addAll(topicList);
        adapter.notifyDataSetChanged();
    }

}
