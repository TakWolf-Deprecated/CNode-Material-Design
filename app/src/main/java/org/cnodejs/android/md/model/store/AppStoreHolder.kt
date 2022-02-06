package org.cnodejs.android.md.model.store

import android.app.Application
import androidx.annotation.GuardedBy

class AppStoreHolder private constructor(application: Application) {
    companion object {
        private val lock = Any()

        @GuardedBy("lock")
        @Volatile
        private var instance: AppStoreHolder? = null

        fun getInstance(application: Application) = instance ?: synchronized(lock) {
            instance ?: AppStoreHolder(application).also { instance = it }
        }
    }

    val loginStore = LoginStore(application)
    val settingStore = SettingStore(application)
    val draftStore = DraftStore(application)
}
