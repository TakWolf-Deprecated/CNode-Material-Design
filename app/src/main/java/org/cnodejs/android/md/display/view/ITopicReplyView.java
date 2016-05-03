package org.cnodejs.android.md.display.view;

import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.Reply;

public interface ITopicReplyView {

    void showReplyWindow();

    void dismissReplyWindow();

    void onAt(@NonNull Reply target, @NonNull Integer targetPosition);

    void onContentEmptyError();

    void onReplyTopicStart();

    boolean onReplyTopicResultOk(@NonNull Reply reply);

    void onReplyTopicFinish();

}
