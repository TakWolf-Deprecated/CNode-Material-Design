package org.cnodejs.android.md.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.cnodejs.android.md.vm.holder.IToastViewModel
import org.cnodejs.android.md.vm.holder.ToastLiveHolder
import org.cnodejs.android.md.vm.holder.TopicForHomePagingLiveHolder

class MainViewModel(application: Application) : AndroidViewModel(application), IToastViewModel {
    override val toastHolder = ToastLiveHolder()
    val topicsHolder = TopicForHomePagingLiveHolder(this, toastHolder)

    var firstBackPressedTime = 0L
}
