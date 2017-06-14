package org.cnodejs.android.md.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.TopicSimple;
import org.cnodejs.android.md.model.entity.User;
import org.cnodejs.android.md.ui.fragment.TopicSimpleListFragment;

import java.util.ArrayList;
import java.util.List;

public class UserDetailPagerAdapter extends FragmentPagerAdapter {

    private static final String[] titles = {
            "最近回复",
            "最新发布",
            "话题收藏"
    };
    
    private final List<TopicSimpleListFragment> fragmentList = new ArrayList<>();

    public UserDetailPagerAdapter(@NonNull FragmentManager manager) {
        super(manager);
        fragmentList.add(new TopicSimpleListFragment());
        fragmentList.add(new TopicSimpleListFragment());
        fragmentList.add(new TopicSimpleListFragment());
    }

    public void update(@NonNull User user) {
        fragmentList.get(0).notifyDataSetChanged(user.getRecentReplyList());
        fragmentList.get(1).notifyDataSetChanged(user.getRecentTopicList());
    }

    public void update(@NonNull List<Topic> topicList) {
        List<TopicSimple> topicSimpleList = new ArrayList<>();
        for (Topic topic : topicList) {
            topicSimpleList.add(topic);
        }
        fragmentList.get(2).notifyDataSetChanged(topicSimpleList);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
