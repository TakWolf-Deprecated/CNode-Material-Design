package org.cnodejs.android.md.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.cnodejs.android.md.model.api.CNodeClient
import org.cnodejs.android.md.model.store.AppStoreHolder
import org.cnodejs.android.md.vm.holder.IToastViewModel
import org.cnodejs.android.md.vm.holder.ToastLiveHolder

class TopicDetailViewModel(application: Application) : AndroidViewModel(application), IToastViewModel {
    companion object {
        private const val TAG = "TopicDetailViewModel"
    }

    private val accountStore = AppStoreHolder.getInstance(application).accountStore
    private val api = CNodeClient.getInstance(application).api

    override val toastHolder = ToastLiveHolder()
}
