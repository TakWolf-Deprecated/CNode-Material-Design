package org.cnodejs.android.md.ui.view;

import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.Tab;
import org.cnodejs.android.md.model.entity.Topic;

import java.util.List;

public interface IMainView {

    void onSwitchTabOk(@NonNull Tab tab);

    void onRefreshTopicListOk(@NonNull List<Topic> topicList);

    void onRefreshTopicListError();

    void onLoadMoreTopicListOk(@NonNull List<Topic> topicList);

    void onLoadMoreTopicListError();

    void updateUserInfoViews();

    void updateMessageCountViews(int count);

}
