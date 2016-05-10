package org.cnodejs.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import org.cnodejs.android.md.display.view.INotificationView;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.ApiDefine;
import org.cnodejs.android.md.model.api.DefaultToastCallback;
import org.cnodejs.android.md.model.entity.Notification;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.presenter.contract.INotificationPresenter;

import retrofit2.Call;
import retrofit2.Response;

public class NotificationPresenter implements INotificationPresenter {

    private final Activity activity;
    private final INotificationView notificationView;

    public NotificationPresenter(@NonNull Activity activity, @NonNull INotificationView notificationView) {
        this.activity = activity;
        this.notificationView = notificationView;
    }

    @Override
    public void getMessagesAsyncTask() {
        Call<Result.Data<Notification>> call = ApiClient.service.getMessages(LoginShared.getAccessToken(activity), ApiDefine.MD_RENDER);
        call.enqueue(new DefaultToastCallback<Result.Data<Notification>>(activity) {

            @Override
            public boolean onResultOk(Response<Result.Data<Notification>> response, Result.Data<Notification> result) {
                return notificationView.onGetMessagesResultOk(result);
            }

            @Override
            public void onFinish() {
                notificationView.onGetMessagesFinish();
            }

        });
    }

    @Override
    public void markAllMessageReadAsyncTask() {
        Call<Result> call = ApiClient.service.markAllMessageRead(LoginShared.getAccessToken(activity));
        call.enqueue(new DefaultToastCallback<Result>(activity) {

            @Override
            public boolean onResultOk(Response<Result> response, Result result) {
                return notificationView.onMarkAllMessageReadResultOk();
            }

        });
    }

}
