package org.cnodejs.android.md.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.TopicSimple;
import org.cnodejs.android.md.model.entity.User;
import org.cnodejs.android.md.ui.viewholder.TopicSimpleListController;

import java.util.ArrayList;
import java.util.List;

public class UserDetailPagerAdapter extends PagerAdapter {

    private static final String[] TITLES = {
            "最近回复",
            "最新发布",
            "话题收藏"
    };

    private final List<TopicSimpleListController> controllerList = new ArrayList<>();

    public UserDetailPagerAdapter(@NonNull Activity activity, @NonNull ViewPager viewPager) {
        controllerList.add(new TopicSimpleListController(activity, viewPager));
        controllerList.add(new TopicSimpleListController(activity, viewPager));
        controllerList.add(new TopicSimpleListController(activity, viewPager));
    }

    public void update(@NonNull User user) {
        controllerList.get(0).notifyDataSetChanged(user.getRecentReplyList());
        controllerList.get(1).notifyDataSetChanged(user.getRecentTopicList());
    }

    public void update(@NonNull List<Topic> topicList) {
        List<TopicSimple> topicSimpleList = new ArrayList<>();
        topicSimpleList.addAll(topicList);
        controllerList.get(2).notifyDataSetChanged(topicSimpleList);
    }

    @Override
    public int getCount() {
        return controllerList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        TopicSimpleListController controller = controllerList.get(position);
        container.addView(controller.getContentView());
        return controller;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        TopicSimpleListController controller = TopicSimpleListController.assertType(object);
        container.removeView(controller.getContentView());
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        TopicSimpleListController controller = TopicSimpleListController.assertType(object);
        return view == controller.getContentView();
    }

}
