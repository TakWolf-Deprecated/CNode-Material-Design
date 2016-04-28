package org.cnodejs.android.md.display.view;

import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.Reply;

public interface ITopicItemReplyView {

    void updateReplyViews(@NonNull Reply reply);

    void updateUpViews(@NonNull Reply reply);

    boolean onUpReplyResultOk(@NonNull Reply reply, int position);

}
