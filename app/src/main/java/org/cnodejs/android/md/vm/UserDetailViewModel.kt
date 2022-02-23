package org.cnodejs.android.md.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import org.cnodejs.android.md.BuildConfig
import org.cnodejs.android.md.model.api.CNodeClient
import org.cnodejs.android.md.model.entity.ErrorResult
import org.cnodejs.android.md.model.entity.UserDetail
import org.cnodejs.android.md.model.store.AppStoreHolder
import org.cnodejs.android.md.vm.holder.IToastViewModel
import org.cnodejs.android.md.vm.holder.ToastLiveHolder

class UserDetailViewModel(application: Application) : AndroidViewModel(application), IToastViewModel {
    companion object {
        private const val TAG = "UserDetailViewModel"
    }

    private val accountStore = AppStoreHolder.getInstance(application).accountStore
    private val api = CNodeClient.getInstance(application).api

    override val toastHolder = ToastLiveHolder()
    val loadingStateData = MutableLiveData(false)
    val userDetailData = MutableLiveData<UserDetail>()

    var loginName: String? = null
    set(value) {
        if (field != null && field != value) {
            error("loginName not the same")
        }
        if (field == null && value != null) {
            field = value
            loadUserDetail()
        }
    }

    fun loadUserDetail() {
        if (loadingStateData.value == true) {
            return
        }
        loginName?.also { loginName ->
            loadingStateData.value = true
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val userDetail = supervisorScope {
                        val job1 = async { api.getUser(loginName) }
                        val job2 = async { api.getUserCollectTopics(loginName) }
                        UserDetail(
                            job1.await().data,
                            job2.await().data,
                        )
                    }
                    if (accountStore.getLoginName() == userDetail.user.loginName) {
                        accountStore.update(userDetail.user)
                    }
                    delay(500)
                    withContext(Dispatchers.Main) {
                        loadingStateData.value = false
                        userDetailData.value = userDetail
                    }
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "loadUserDetail", e)
                    }
                    val errorResult = ErrorResult.from(e)
                    delay(500)
                    withContext(Dispatchers.Main) {
                        loadingStateData.value = false
                        toastHolder.showToast(errorResult.message)
                    }
                }
            }
        }
    }
}
