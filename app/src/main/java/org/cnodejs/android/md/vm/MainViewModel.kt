package org.cnodejs.android.md.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import org.cnodejs.android.md.vm.holder.BaseLiveHolder
import org.cnodejs.android.md.vm.holder.TopicPagingLiveHolder

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val baseLiveHolder = BaseLiveHolder()
    val topicPagingLiveHolder = TopicPagingLiveHolder(viewModelScope, baseLiveHolder)
}
