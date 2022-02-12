package org.cnodejs.android.md.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.cnodejs.android.md.vm.holder.IToastViewModel
import org.cnodejs.android.md.vm.holder.ToastLiveHolder
import org.cnodejs.android.md.vm.holder.TopicPagingLiveHolder

class MainViewModel(application: Application) : AndroidViewModel(application), IToastViewModel {
    override val toastLiveHolder = ToastLiveHolder()
    val topicsLiveHolder = TopicPagingLiveHolder(this, toastLiveHolder)

    var firstBackPressedTime = 0L
}
