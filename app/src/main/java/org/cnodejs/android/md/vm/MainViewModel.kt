package org.cnodejs.android.md.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.cnodejs.android.md.vm.holder.IToastViewModel
import org.cnodejs.android.md.vm.holder.ToastLiveHolder
import org.cnodejs.android.md.vm.holder.TopicHomePagingLiveHolder

class MainViewModel(application: Application) : AndroidViewModel(application), IToastViewModel {
    override val toastHolder = ToastLiveHolder()
    val topicsHolder = TopicHomePagingLiveHolder(this, toastHolder)

    var firstBackPressedTime = 0L
}
