package org.cnodejs.android.md.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.model.api.ApiClient;
import org.cnodejs.android.md.model.api.DefaultCallback;
import org.cnodejs.android.md.model.entity.Result;
import org.cnodejs.android.md.model.entity.TabType;
import org.cnodejs.android.md.model.storage.LoginShared;
import org.cnodejs.android.md.model.storage.SettingShared;
import org.cnodejs.android.md.presenter.contract.ICreateTopicPresenter;
import org.cnodejs.android.md.ui.view.ICreateTopicView;

import okhttp3.Headers;

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
            createTopicView.onTitleError(activity.getString(R.string.title_empty_error_tip));
        } else if (TextUtils.isEmpty(content)) {
            createTopicView.onContentError(activity.getString(R.string.content_empty_error_tip));
        } else {
            if (SettingShared.isEnableTopicSign(activity)) { // 添加小尾巴
                content += "\n\n" + SettingShared.getTopicSignContent(activity);
            }
            createTopicView.onCreateTopicStart();
            ApiClient.service.createTopic(LoginShared.getAccessToken(activity), tab, title, content).enqueue(new DefaultCallback<Result.CreateTopic>(activity) {

                @Override
                public boolean onResultOk(int code, Headers headers, Result.CreateTopic result) {
                    createTopicView.onCreateTopicOk(result.getTopicId());
                    return false;
                }

                @Override
                public void onFinish() {
                    createTopicView.onCreateTopicFinish();
                }

            });
        }
    }

}
