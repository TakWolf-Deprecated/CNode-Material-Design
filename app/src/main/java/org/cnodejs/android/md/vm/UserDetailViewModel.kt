package org.cnodejs.android.md.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.cnodejs.android.md.vm.holder.BaseLiveHolder

class UserDetailViewModel(application: Application) : AndroidViewModel(application) {
    val baseLiveHolder = BaseLiveHolder()
}
