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
        loadMyInfo()
    }

    override fun onCleared() {
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAccountChanged(event: AccountChangedEvent) {
        accountData.value = event.account
        loadMyInfo()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAccountUpdated(event: AccountUpdatedEvent) {
        accountData.value = event.account
    }

    fun isLogined(): Boolean {
        return accountData.value != null
    }

    fun loadMyInfo() {
        loadMyUserInfo()
        loadMessageCount()
    }

    private fun loadMyUserInfo() {
        accountData.value?.loginName?.let { loginName ->
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = api.getUser(loginName)
                    accountStore.update(result.data)
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "loadMyUserInfo", e)
                    }
                }
            }
        }
    }

    private fun loadMessageCount() {
        accountData.value?.id?.let { accountId ->
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = api.getMessagesCount(accountStore.requireAccessToken())
                    withContext(Dispatchers.Main) {
                        if (accountData.value?.id == accountId) {
                            messageCountData.value = result.data
                        }
                    }
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "loadMessageCount", e)
                    }
                }
            }
        }
    }
}
