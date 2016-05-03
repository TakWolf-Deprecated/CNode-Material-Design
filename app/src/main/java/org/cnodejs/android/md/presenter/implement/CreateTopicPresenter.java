package org.cnodejs.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.cnodejs.android.md.display.view.ICreateTopicView;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.DefaultToastCallback;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.TabType;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.model.storage.SettingShared;
import org.cnodejs.android.md.presenter.contract.ICreateTopicPresenter;

import retrofit2.Call;
import retrofit2.Response;

public class CreateTopicPresenter implements ICreateTopicPresenter {

    private final Activity activity;
    private final ICreateTopicView createTopicView;

    public CreateTopicPresenter(@NonNull Activity activity, @NonNull ICreateTopicView createTopicView) {
        this.activity = activity;
        this.createTopicView = createTopicView;
    }

    @Override
    public void createTopicAsyncTask(@NonNull TabType tab, String title, String content) {
        if (TextUtils.isEmpty(title) || title.length() < 10) {
            createTopicView.onTitleEmptyError();
        } else if (TextUtils.isEmpty(content)) {
            createTopicView.onContentEmptyError();
        } else {
            if (SettingShared.isEnableTopicSign(activity)) { // 添加小尾巴
                content += "\n\n" + SettingShared.getTopicSignContent(activity);
            }
            createTopicView.onCreateTopicStart();
            Call<Result.CreateTopic> call = ApiClient.service.createTopic(LoginShared.getAccessToken(activity), tab, title, content);
            call.enqueue(new DefaultToastCallback<Result.CreateTopic>(activity) {

                @Override
                public boolean onResultOk(Response<Result.CreateTopic> response, Result.CreateTopic result) {
                    return createTopicView.onCreateTopicResultOk(result);
                }

                @Override
                public void onFinish() {
                    createTopicView.onCreateTopicFinish();
                }

            });
        }
    }

}
