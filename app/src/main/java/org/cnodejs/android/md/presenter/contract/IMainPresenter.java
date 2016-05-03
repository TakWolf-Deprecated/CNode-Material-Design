package org.cnodejs.android.md.presenter.contract;

import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.TabType;

public interface IMainPresenter {

    void refreshTopicListAsyncTask(@NonNull TabType tab, @NonNull Integer limit, @NonNull Boolean mdrender);

    void loadMoreTopicListAsyncTask(@NonNull TabType tab, @NonNull Integer page, @NonNull Integer limit, @NonNull Boolean mdrender);

    void getUserAsyncTask();

    void getMessageCountAsyncTask();

}
