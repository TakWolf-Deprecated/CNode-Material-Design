package org.cnodejs.android.md.display.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.cnodejs.android.md.display.fragment.UserDetailItemFragment;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.TopicSimple;
import org.cnodejs.android.md.model.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserDetailAdapter extends FragmentPagerAdapter {

    private final List<UserDetailItemFragment> fmList = new ArrayList<>();
    private final String[] titles = {
            "最近回复",
            "最新发布",
            "话题收藏"
    };

    public UserDetailAdapter(FragmentManager manager) {
        super(manager);
        fmList.add(new UserDetailItemFragment());
        fmList.add(new UserDetailItemFragment());
        fmList.add(new UserDetailItemFragment());
    }

    public void update(@NonNull User user) {
        fmList.get(0).notifyDataSetChanged(user.getRecentReplyList());
        fmList.get(1).notifyDataSetChanged(user.getRecentTopicList());
    }

    public void update(@NonNull List<Topic> topicList) {
        List<TopicSimple> topicSimpleList = new ArrayList<>();
        for (Topic topic : topicList) {
            topicSimpleList.add(topic);
        }
        fmList.get(2).notifyDataSetChanged(topicSimpleList);
    }

    @Override
    public Fragment getItem(int position) {
        return fmList.get(position);
    }

    @Override
    public int getCount() {
        return fmList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
