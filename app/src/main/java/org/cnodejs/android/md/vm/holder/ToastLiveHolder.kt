package org.cnodejs.android.md.vm.holder

import com.hadilq.liveevent.LiveEvent

class ToastLiveHolder {
    val toastEvent = LiveEvent<String>()

    fun showToast(message: String) {
        toastEvent.value = message
    }
}

interface IToastViewModel {
    val toastLiveHolder: ToastLiveHolder
}
