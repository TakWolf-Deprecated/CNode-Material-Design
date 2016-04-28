package org.cnodejs.android.md.display.view;

import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.Result;

public interface ICreateTopicView {

    void onTitleEmptyError();

    void onContentEmptyError();

    void onCreateTopicStart();

    boolean onCreateTopicResultOk(@NonNull Result.CreateTopic result);

    void onCreateTopicFinish();

}
