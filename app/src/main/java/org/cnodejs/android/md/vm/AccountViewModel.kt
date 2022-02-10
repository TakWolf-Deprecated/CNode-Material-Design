package org.cnodejs.android.md.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import org.cnodejs.android.md.bus.AccountChangedEvent
import org.cnodejs.android.md.bus.AccountUpdatedEvent
import org.cnodejs.android.md.model.entity.Account
import org.cnodejs.android.md.model.store.AppStoreHolder
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private val accountStore = AppStoreHolder.getInstance(application).accountStore

    val accountData = MutableLiveData<Account?>(accountStore.getAccount())

    init {
        EventBus.getDefault().register(this)
    }

    override fun onCleared() {
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAccountChanged(event: AccountChangedEvent) {
        accountData.value = event.account
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAccountUpdated(event: AccountUpdatedEvent) {
        accountData.value = event.account
    }

    fun isLogined(): Boolean {
        return accountData.value != null
    }

    fun logout() {
        accountStore.logout()
    }
}
