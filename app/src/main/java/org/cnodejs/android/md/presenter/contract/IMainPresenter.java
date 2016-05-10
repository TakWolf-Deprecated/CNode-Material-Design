package org.cnodejs.android.md.presenter.contract;

import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.TabType;

public interface IMainPresenter {

    void refreshTopicListAsyncTask(@NonNull TabType tab, @NonNull Integer limit);

    void loadMoreTopicListAsyncTask(@NonNull TabType tab, @NonNull Integer page, @NonNull Integer limit);

    void getUserAsyncTask();

    void getMessageCountAsyncTask();

}
