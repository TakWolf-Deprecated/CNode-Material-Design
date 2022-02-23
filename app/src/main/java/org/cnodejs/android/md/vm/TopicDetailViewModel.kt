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
import org.cnodejs.android.md.model.api.CNodeClient
import org.cnodejs.android.md.model.entity.ErrorResult
import org.cnodejs.android.md.model.entity.TopicDetail
import org.cnodejs.android.md.model.store.AppStoreHolder
import org.cnodejs.android.md.vm.holder.IToastViewModel
import org.cnodejs.android.md.vm.holder.ToastLiveHolder
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class TopicDetailViewModel(application: Application) : AndroidViewModel(application), IToastViewModel {
    companion object {
        private const val TAG = "TopicDetailViewModel"
    }

    private val accountStore = AppStoreHolder.getInstance(application).accountStore
    private val api = CNodeClient.getInstance(application).api

    override val toastHolder = ToastLiveHolder()
    val loadingStateData = MutableLiveData(false)
    val topicDetailData = MutableLiveData<TopicDetail?>()

    lateinit var topicId: String

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
        if (topicDetailData.hasActiveObservers()) {
            onViewStart()
        }
    }

    fun onViewStart() {
        if (!isFirstLoadDone) {
            isFirstLoadDone = true
            loadTopicDetail()
        }
    }

    fun loadTopicDetail() {
        if (loadingStateData.value == true) {
            return
        }
        val version = dataVersion
        loadingStateData.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = api.getTopic(topicId, accountStore.getAccessToken())
                withContext(Dispatchers.Main) {
                    if (dataVersion == version) {
                        dataVersion += 1
                        loadingStateData.value = false
                        topicDetailData.value = result.data
                    }
                }
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "loadTopicDetail", e)
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
