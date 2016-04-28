package org.cnodejs.android.md.display.view;

import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.Notification;
import org.cnodejs.android.md.model.entity.Result;

public interface INotificationView {

    boolean onGetMessagesResultOk(@NonNull Result.Data<Notification> result);

    void onGetMessagesFinish();

    boolean onMarkAllMessageReadResultOk();

}
