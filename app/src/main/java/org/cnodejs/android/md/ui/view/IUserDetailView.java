package org.cnodejs.android.md.ui.view;

import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.User;

import java.util.List;

public interface IUserDetailView {

    void onGetUserOk(@NonNull User user);

    void onGetCollectTopicListOk(@NonNull List<Topic> topicList);

    void onGetUserError(@NonNull String message);

    void onGetUserStart();

    void onGetUserFinish();

}
