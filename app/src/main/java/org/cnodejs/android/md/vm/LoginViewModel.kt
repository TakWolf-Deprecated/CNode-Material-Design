package org.cnodejs.android.md.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.cnodejs.android.md.model.api.CNodeClient
import org.cnodejs.android.md.model.entity.ErrorResult
import org.cnodejs.android.md.model.store.AppStoreHolder
import org.cnodejs.android.md.util.notifyDataChanged
import org.cnodejs.android.md.vm.holder.BaseLiveHolder

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val accountStore = AppStoreHolder.getInstance(application).accountStore
    private val api = CNodeClient.getInstance(application).api

    val baseLiveHolder = BaseLiveHolder()
    val loginedEvent = LiveEvent<Unit>()
    val errorMessageEvent = LiveEvent<String>()

    fun login(accessToken: String) {
        baseLiveHolder.showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = api.login(accessToken)
                accountStore.login(accessToken, result)
                withContext(Dispatchers.Main) {
                    baseLiveHolder.hideLoading()
                    loginedEvent.notifyDataChanged()
                }
            } catch (e: Exception) {
                val errorResult = ErrorResult.from(e)
                withContext(Dispatchers.Main) {
                    baseLiveHolder.hideLoading()
                    errorMessageEvent.value = errorResult.message
                }
            }
        }
    }
}
