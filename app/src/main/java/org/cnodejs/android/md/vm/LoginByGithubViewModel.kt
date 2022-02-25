package org.cnodejs.android.md.vm

import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent

class LoginByGithubViewModel : ViewModel() {
    val accessTokenEvent = LiveEvent<String>()
}
