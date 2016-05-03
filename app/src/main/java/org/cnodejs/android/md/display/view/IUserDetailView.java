package org.cnodejs.android.md.display.view;

import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.Topic;
import org.cnodejs.android.md.model.entity.User;

import java.util.List;

public interface IUserDetailView {

    void onGetUserStart();

    boolean onGetUserResultOk(@NonNull Result.Data<User> result);

    boolean onGetUserResultError(@NonNull Result.Error error);

    boolean onGetUserLoadError();

    void onGetUserFinish();

    void updateUserInfoViews(@NonNull User user);

    boolean onGetCollectTopicListResultOk(@NonNull Result.Data<List<Topic>> result);

}
