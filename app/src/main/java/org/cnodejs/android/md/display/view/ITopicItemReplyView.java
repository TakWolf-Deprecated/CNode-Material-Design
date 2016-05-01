package org.cnodejs.android.md.display.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.cnodejs.android.md.model.entity.Reply;

public interface ITopicItemReplyView {

    void updateReplyViews(@NonNull Reply reply, int position, @Nullable Integer targetPosition);

    void updateUpViews(@NonNull Reply reply);

    boolean onUpReplyResultOk(@NonNull Reply reply, int position);

}
