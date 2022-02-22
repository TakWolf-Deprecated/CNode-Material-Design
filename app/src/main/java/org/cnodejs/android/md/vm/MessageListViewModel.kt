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
import org.cnodejs.android.md.model.entity.MessageWithSummary
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
    val messagesHolder = ListLiveHolder<MessageWithSummary>()
    var isLoadingData = MutableLiveData(false)

    init {
        loadMessages()
    }

    fun loadMessages() {
        if (isLoadingData.value == true) {
            return
        }
        accountStore.getAccessToken()?.let { accessToken ->
            isLoadingData.value = true
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = api.getMessages(accessToken, true)
                    val messages = mutableListOf<MessageWithSummary>().apply {
                        addAll(result.data.hasNotReadMessages)
                        addAll(result.data.hasReadMessages)
                    }
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

    fun markAllMessagesRead() {
        accountStore.getAccessToken()?.let { accessToken ->
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val messageIds = api.markAllMessagesRead(accessToken).markedMessages.map { idData -> idData.id }.toSet()
                    withContext(Dispatchers.Main) {
                        messagesHolder.entitiesData.value?.let { messages ->
                            messagesHolder.entitiesData.value = messages.map { message ->
                                if (messageIds.contains(message.message.id)) {
                                    MessageWithSummary(message.message.copy(hasRead = true))
                                } else {
                                    message
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "markAllMessagesRead", e)
                    }
                    val errorResult = ErrorResult.from(e)
                    withContext(Dispatchers.Main) {
                        toastHolder.showToast(errorResult.message)
                    }
                }
            }
        }
    }

    fun markMessageRead(messageId: String) {
        accountStore.getAccessToken()?.let { accessToken ->
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val markedMessageId = api.markMessageRead(messageId, accessToken).markedMessageId
                    if (messageId != markedMessageId && BuildConfig.DEBUG) {
                        Log.w(TAG, "markMessageRead - messageId: $messageId not equals markedMessageId: $markedMessageId")
                    }
                    withContext(Dispatchers.Main) {
                        messagesHolder.entitiesData.value?.let { messages ->
                            messagesHolder.entitiesData.value = messages.map { message ->
                                if (message.message.id == markedMessageId) {
                                    MessageWithSummary(message.message.copy(hasRead = true))
                                } else {
                                    message
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "markMessageRead", e)
                    }
                    val errorResult = ErrorResult.from(e)
                    withContext(Dispatchers.Main) {
                        toastHolder.showToast(errorResult.message)
                    }
                }
            }
        }
    }
}
