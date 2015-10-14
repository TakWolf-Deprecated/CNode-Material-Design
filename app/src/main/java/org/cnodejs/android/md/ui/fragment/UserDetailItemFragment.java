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
import org.cnodejs.android.md.ui.adapter.UserDetailItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserDetailItemFragment extends Fragment {

    @Bind(R.id.user_detail_fragment_recycler_view)
    protected RecyclerView recyclerView;

    private UserDetailItemAdapter adapter;
    private List<TopicSimple> topicList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_user_detail_fragment, container, false);
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
