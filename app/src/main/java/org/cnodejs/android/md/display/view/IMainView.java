package org.cnodejs.android.md.display.view;

import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.TabType;
import org.cnodejs.android.md.model.entity.Topic;

import java.util.List;

public interface IMainView {

    boolean onRefreshTopicListResultOk(@NonNull TabType tab, @NonNull Result.Data<List<Topic>> result);

    boolean onRefreshTopicListResultErrorOrCallException(@NonNull TabType tab, @NonNull Result.Error error);

    void onRefreshTopicListFinish();

    boolean onLoadMoreTopicListResultOk(@NonNull TabType tab, @NonNull Integer page, Result.Data<List<Topic>> result);

    boolean onLoadMoreTopicListResultErrorOrCallException(@NonNull TabType tab, @NonNull Integer page, @NonNull Result.Error error);

    void onLoadMoreTopicListFinish();

    void updateUserInfoViews();

    void updateMessageCountViews(@NonNull Result.Data<Integer> result);

}
