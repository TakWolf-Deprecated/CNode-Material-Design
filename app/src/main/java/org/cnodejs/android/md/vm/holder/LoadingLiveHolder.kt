package org.cnodejs.android.md.vm.holder

import androidx.lifecycle.MutableLiveData
import org.cnodejs.android.md.ui.dialog.LoadingDialog

class LoadingLiveHolder {
    val loadingCountData = MutableLiveData(0)

    fun showLoading() {
        val count = loadingCountData.value ?: 0
        loadingCountData.value = count + 1
    }

    fun hideLoading() {
        val count = loadingCountData.value ?: 0
        loadingCountData.value = count - 1
    }
}

interface ILoadingViewModel {
    val loadingHolder: LoadingLiveHolder
}
