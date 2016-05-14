package org.cnodejs.android.md.presenter.contract;

import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.Reply;

public interface ITopicItemReplyPresenter {

    void upReplyAsyncTask(@NonNull Reply reply);

}
