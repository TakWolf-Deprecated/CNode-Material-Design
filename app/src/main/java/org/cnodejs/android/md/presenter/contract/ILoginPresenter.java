package org.cnodejs.android.md.presenter.contract;

import org.cnodejs.android.md.model.entity.Result;

import retrofit2.Call;

public interface ILoginPresenter {

    Call<Result.Login> loginAsyncTask(String accessToken);

}
