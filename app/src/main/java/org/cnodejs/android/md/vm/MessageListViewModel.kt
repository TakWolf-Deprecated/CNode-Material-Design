package org.cnodejs.android.md.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.cnodejs.android.md.BuildConfig
import org.cnodejs.android.md.model.api.CNodeClient
import org.cnodejs.android.md.model.entity.ErrorResult
import org.cnodejs.android.md.model.entity.Message
import org.cnodejs.android.md.model.store.AppStoreHolder
import org.cnodejs.android.md.vm.holder.IToastViewModel
import org.cnodejs.android.md.vm.holder.ListLiveHolder
import org.cnodejs.android.md.vm.holder.ToastLiveHolder

class MessageListViewModel(application: Application) : AndroidViewModel(application), IToastViewModel {
    companion object {
        private const val TAG = "MessageListViewModel"
    }

    private val accountStore = AppStoreHolder.getInstance(application).accountStore
    private val api = CNodeClient.getInstance(application).api

    override val toastHolder = ToastLiveHolder()
    val messagesHolder = ListLiveHolder<Message>()
    var isLoadingData = MutableLiveData(false)

    fun loadMessages() {
        if (isLoadingData.value == true) {
            return
        }
        accountStore.getAccessToken()?.let { accessToken ->
            isLoadingData.value = true
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = api.getMessages(accessToken)
                    val messages = mutableListOf<Message>()
                    messages.addAll(result.data.hasNotReadMessages)
                    messages.addAll(result.data.hasReadMessages)
                    withContext(Dispatchers.Main) {
                        isLoadingData.value = false
                        messagesHolder.setList(messages)
                    }
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "loadMessages", e)
                    }
                    val errorResult = ErrorResult.from(e)
                    withContext(Dispatchers.Main) {
                        isLoadingData.value = false
                        toastHolder.showToast(errorResult.message)
                    }
                }
            }
        }
    }
}
