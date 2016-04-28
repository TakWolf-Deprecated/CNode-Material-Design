package org.cnodejs.android.md.display.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.TopicWithReply;

public interface ITopicHeaderView {

    void updateViews(@Nullable Topic topic, boolean isCollect, int replyCount);

    void updateViews(@NonNull TopicWithReply topic);

    void updateReplyCount(int replyCount);

    boolean onCollectTopicResultOk(Result result);

    boolean onDecollectTopicResultOk(Result result);

}
