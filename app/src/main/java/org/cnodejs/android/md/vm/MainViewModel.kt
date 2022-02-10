package org.cnodejs.android.md.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.cnodejs.android.md.vm.holder.BaseLiveHolder
import org.cnodejs.android.md.vm.holder.TopicPagingLiveHolder

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val baseLiveHolder = BaseLiveHolder()
    val topicPagingLiveHolder = TopicPagingLiveHolder(this, baseLiveHolder)

    var firstBackPressedTime = 0L
}
