package org.cnodejs.android.md.presenter.contract;

import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.Tab;

public interface IMainPresenter {

    void switchTab(@NonNull Tab tab);

    void refreshTopicListAsyncTask();

    void loadMoreTopicListAsyncTask(int page);

    void getUserAsyncTask();

    void getMessageCountAsyncTask();

}
