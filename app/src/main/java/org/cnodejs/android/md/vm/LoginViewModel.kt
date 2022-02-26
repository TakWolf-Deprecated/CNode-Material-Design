package org.cnodejs.android.md.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.cnodejs.android.md.BuildConfig
import org.cnodejs.android.md.model.api.CNodeClient
import org.cnodejs.android.md.model.entity.ErrorResult
import org.cnodejs.android.md.model.store.AppStoreHolder
import org.cnodejs.android.md.util.notifyDataChanged
import org.cnodejs.android.md.vm.holder.ILoadingViewModel
import org.cnodejs.android.md.vm.holder.LoadingLiveHolder

class LoginViewModel(application: Application) : AndroidViewModel(application), ILoadingViewModel {
    companion object {
        private const val TAG = "LoginViewModel"
    }

    private val accountStore = AppStoreHolder.getInstance(application).accountStore
    private val api = CNodeClient.getInstance(application).api

    override val loadingHolder = LoadingLiveHolder()
    val loginedEvent = LiveEvent<Unit>()
    val errorMessageEvent = LiveEvent<String>()

    fun login(accessToken: String) {
        loadingHolder.showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = api.login(accessToken)
                accountStore.login(accessToken, result)
                delay(500)
                withContext(Dispatchers.Main) {
                    loadingHolder.hideLoading()
                    loginedEvent.notifyDataChanged()
                }
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "login", e)
                }
                val errorResult = ErrorResult.from(e)
                delay(500)
                withContext(Dispatchers.Main) {
                    loadingHolder.hideLoading()
                    errorMessageEvent.value = errorResult.message
                }
            }
        }
    }
}
