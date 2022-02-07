package org.cnodejs.android.md.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hadilq.liveevent.LiveEvent
import org.cnodejs.android.md.model.entity.Account
import org.cnodejs.android.md.model.store.AppStoreHolder

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private val accountStore = AppStoreHolder.getInstance(application).accountStore

    val accountData = MutableLiveData<Account>(accountStore.getAccount())
    val accountChangedEvent = LiveEvent<Any>()
}
