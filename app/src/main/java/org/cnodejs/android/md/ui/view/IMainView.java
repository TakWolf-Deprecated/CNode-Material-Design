package org.cnodejs.android.md.ui.view;

import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.TabType;
import org.cnodejs.android.md.model.entity.Topic;

import java.util.List;

public interface IMainView {

    boolean onRefreshTopicListOk(@NonNull TabType tab, @NonNull List<Topic> topicList);

    boolean onRefreshTopicListError(@NonNull TabType tab, @NonNull String message);

    void onRefreshTopicListFinish();

    boolean onLoadMoreTopicListOk(@NonNull TabType tab, @NonNull Integer page, @NonNull List<Topic> topicList);

    boolean onLoadMoreTopicListError(@NonNull TabType tab, @NonNull Integer page, @NonNull String message);

    void onLoadMoreTopicListFinish();

    void updateUserInfoViews();

    void updateMessageCountViews(int count);

}
