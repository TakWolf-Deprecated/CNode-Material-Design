package org.cnodejs.android.md.display.view;

import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.Reply;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.TopicWithReply;

public interface ITopicView {

    boolean onGetTopicResultOk(@NonNull Result.Data<TopicWithReply> result);

    void onGetTopicFinish();

    void appendReplyAndUpdateViews(@NonNull Reply reply);

}
