package org.cnodejs.android.md.presenter.contract;

import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.Tab;

public interface ICreateTopicPresenter {

    void createTopicAsyncTask(@NonNull Tab tab, String title, String content);

}
