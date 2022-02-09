package org.cnodejs.android.md.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.cnodejs.android.md.vm.holder.BaseLiveHolder

class TopicDetailViewModel(application: Application) : AndroidViewModel(application) {
    val baseLiveHolder = BaseLiveHolder()
}
