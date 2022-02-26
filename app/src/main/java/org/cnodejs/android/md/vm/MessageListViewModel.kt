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
import org.cnodejs.android.md.bus.AccountChangedEvent
import org.cnodejs.android.md.bus.AccountInfoNeedRefreshEvent
import org.cnodejs.android.md.model.api.CNodeClient
import org.cnodejs.android.md.model.entity.ErrorResult
import org.cnodejs.android.md.model.entity.Message
import org.cnodejs.android.md.model.store.AppStoreHolder
import org.cnodejs.android.md.vm.holder.IToastViewModel
import org.cnodejs.android.md.vm.holder.ListLiveHolder
import org.cnodejs.android.md.vm.holder.ToastLiveHolder
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MessageListViewModel(application: Application) : AndroidViewModel(application), IToastViewModel {
    companion object {
        private const val TAG = "MessageListViewModel"
    }

    private val accountStore = AppStoreHolder.getInstance(application).accountStore
    private val api = CNodeClient.getInstance(application).api

    override val toastHolder = ToastLiveHolder()
    val loadingStateData = MutableLiveData(false)
    val messagesHolder = ListLiveHolder<Message>()

    private var dataVersion = 0
    private var isFirstLoadDone = false

    init {
        EventBus.getDefault().register(this)
    }

    override fun onCleared() {
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAccountChanged(event: AccountChangedEvent) {
        dataVersion += 1
        isFirstLoadDone = false
        loadingStateData.value = false
        messagesHolder.clearList()
        if (event.account != null && messagesHolder.entitiesData.hasActiveObservers()) {
            onViewStart()
        }
    }

    fun onViewStart() {
        if (!isFirstLoadDone) {
            isFirstLoadDone = true
            loadMessages()
        }
    }

    fun loadMessages() {
        if (loadingStateData.value == true) {
            return
        }
        accountStore.getAccessToken()?.let { accessToken ->
            val version = dataVersion
            loadingStateData.value = true
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val messages = api.getMessages(accessToken).data.toList()
                    withContext(Dispatchers.Main) {
                        if (dataVersion == version) {
                            dataVersion += 1
                            loadingStateData.value = false
                            messagesHolder.setList(messages)
                        }
                    }
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "loadMessages", e)
                    }
                    val errorResult = ErrorResult.from(e)
                    withContext(Dispatchers.Main) {
                        if (dataVersion == version) {
                            loadingStateData.value = false
                            toastHolder.showToast(errorResult.message)
                        }
                    }
                }
            }
        }
    }

    fun markAllMessagesRead() {
        accountStore.getAccessToken()?.let { accessToken ->
            val version = dataVersion
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val messageIds = api.markAllMessagesRead(accessToken).markedMessages.map { idData -> idData.id }.toSet()
                    withContext(Dispatchers.Main) {
                        if (dataVersion == version) {
                            EventBus.getDefault().post(AccountInfoNeedRefreshEvent(messagesCount = true))
                            messagesHolder.entitiesData.value?.let { messages ->
                                messagesHolder.entitiesData.value = messages.map { message ->
                                    if (messageIds.contains(message.id)) {
                                        message.copy(hasRead = true)
                                    } else {
                                        message
                                    }
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
                        if (dataVersion == version) {
                            toastHolder.showToast(errorResult.message)
                        }
                    }
                }
            }
        }
    }

    fun markMessageRead(messageId: String) {
        accountStore.getAccessToken()?.let { accessToken ->
            val version = dataVersion
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val markedMessageId = api.markMessageRead(messageId, accessToken).markedMessageId
                    if (messageId != markedMessageId && BuildConfig.DEBUG) {
                        Log.w(TAG, "markMessageRead - messageId: $messageId not equals markedMessageId: $markedMessageId")
                    }
                    withContext(Dispatchers.Main) {
                        if (dataVersion == version) {
                            EventBus.getDefault().post(AccountInfoNeedRefreshEvent(messagesCount = true))
                            messagesHolder.entitiesData.value?.let { messages ->
                                messagesHolder.entitiesData.value = messages.map { message ->
                                    if (message.id == markedMessageId) {
                                        message.copy(hasRead = true)
                                    } else {
                                        message
                                    }
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
                        if (dataVersion == version) {
                            toastHolder.showToast(errorResult.message)
                        }
                    }
                }
            }
        }
    }
}
