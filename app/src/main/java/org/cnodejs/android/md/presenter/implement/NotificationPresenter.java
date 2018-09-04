package org.cnodejs.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.ApiDefine;
import org.cnodejs.android.md.model.api.SessionCallback;
import org.cnodejs.android.md.model.entity.DataResult;
import org.cnodejs.android.md.model.entity.Notification;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.presenter.contract.INotificationPresenter;
import org.cnodejs.android.md.ui.view.INotificationView;

import okhttp3.Headers;

public class NotificationPresenter implements INotificationPresenter {

    private final Activity activity;
    private final INotificationView notificationView;

    public NotificationPresenter(@NonNull Activity activity, @NonNull INotificationView notificationView) {
        this.activity = activity;
        this.notificationView = notificationView;
    }

    @Override
    public void getMessagesAsyncTask() {
        ApiClient.service.getMessages(LoginShared.getAccessToken(activity), ApiDefine.MD_RENDER).enqueue(new SessionCallback<DataResult<Notification>>(activity) {

            @Override
            public boolean onResultOk(int code, Headers headers, DataResult<Notification> result) {
                notificationView.onGetMessagesOk(result.getData());
                return false;
            }

            @Override
            public void onFinish() {
                notificationView.onGetMessagesFinish();
            }

        });
    }

    @Override
    public void markAllMessageReadAsyncTask() {
        ApiClient.service.markAllMessageRead(LoginShared.getAccessToken(activity)).enqueue(new SessionCallback<Result>(activity) {

            @Override
            public boolean onResultOk(int code, Headers headers, Result result) {
                notificationView.onMarkAllMessageReadOk();
                return false;
            }

        });
    }

}
