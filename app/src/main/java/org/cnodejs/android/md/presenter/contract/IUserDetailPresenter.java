package org.cnodejs.android.md.presenter.contract;

import android.support.annotation.NonNull;

public interface IUserDetailPresenter {

    void getUserAsyncTask(@NonNull String loginName);

    void getCollectTopicListAsyncTask(@NonNull String loginName);

}
