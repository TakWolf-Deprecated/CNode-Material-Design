package org.cnodejs.android.md.ui.view;

import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.Reply;

public interface ITopicReplyView {

    void showReplyWindow();

    void dismissReplyWindow();

    void onAt(@NonNull Reply target, @NonNull Integer targetPosition);

    void onContentError(@NonNull String message);

    void onReplyTopicOk(@NonNull Reply reply);

    void onReplyTopicStart();

    void onReplyTopicFinish();

}
