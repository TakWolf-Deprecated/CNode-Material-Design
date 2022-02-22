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
import org.cnodejs.android.md.bus.AccountUpdatedEvent
import org.cnodejs.android.md.model.api.CNodeClient
import org.cnodejs.android.md.model.entity.Account
import org.cnodejs.android.md.model.store.AppStoreHolder
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val TAG = "AccountViewModel"
    }

    private val accountStore = AppStoreHolder.getInstance(application).accountStore
    private val api = CNodeClient.getInstance(application).api

    val accountData = MutableLiveData<Account?>(accountStore.getAccount())
    val messageCountData = MutableLiveData(0)

    init {
        EventBus.getDefault().register(this)
        loadUser()
        loadMessagesCount()
    }

    override fun onCleared() {
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAccountChanged(event: AccountChangedEvent) {
        accountData.value = event.account
        messageCountData.value = 0
        loadUser()
        loadMessagesCount()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAccountUpdated(event: AccountUpdatedEvent) {
        if (accountData.value != null) {
            accountData.value = event.account
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAccountInfoNeedRefresh(event: AccountInfoNeedRefreshEvent) {
        if (event.user) {
            loadUser()
        }
        if (event.messagesCount) {
            loadMessagesCount()
        }
    }

    fun isLogined(): Boolean {
        return accountData.value != null
    }

    fun loadUser() {
        accountData.value?.let { account ->
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = api.getUser(account.loginName)
                    if (accountStore.getLoginName() == result.data.loginName) {
                        accountStore.update(result.data)
                    }
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "loadUser", e)
                    }
                }
            }
        }
    }

    fun loadMessagesCount() {
        accountData.value?.let { account ->
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = api.getMessagesCount(account.accessToken)
                    withContext(Dispatchers.Main) {
                        if (accountData.value?.id == account.id) {
                            messageCountData.value = result.data
                        }
                    }
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "loadMessagesCount", e)
                    }
                }
            }
        }
    }
}
