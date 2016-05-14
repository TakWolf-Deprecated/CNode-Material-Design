package org.cnodejs.android.md.display.view;

import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.Reply;

public interface ITopicItemReplyView {

    boolean onUpReplyResultOk(@NonNull Reply reply);

}
