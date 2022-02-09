package org.cnodejs.android.md.vm.holder

import androidx.lifecycle.MutableLiveData
import com.hadilq.liveevent.LiveEvent

class BaseLiveHolder {
    val toastEvent = LiveEvent<String>()
    val loadingCountData = MutableLiveData(0)

    fun showToast(message: String) {
        toastEvent.value = message
    }

    fun showLoading() {
        val count = loadingCountData.value ?: 0
        loadingCountData.value = count + 1
    }

    fun hideLoading() {
        val count = loadingCountData.value ?: 0
        loadingCountData.value = count - 1
    }
}
