package org.cnodejs.android.md.presenter.contract;

import android.support.annotation.NonNull;

public interface ITopicReplyPresenter {

    void replyTopicAsyncTask(@NonNull String topicId, String content, String targetId);

}
