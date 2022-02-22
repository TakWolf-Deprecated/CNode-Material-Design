package org.cnodejs.android.md.model.store

import android.content.Context
import androidx.annotation.GuardedBy

class AppStoreHolder private constructor(context: Context) {
    companion object {
        private val lock = Any()

        @GuardedBy("lock")
        @Volatile
        private var instance: AppStoreHolder? = null

        fun getInstance(context: Context) = instance ?: synchronized(lock) {
            instance ?: AppStoreHolder(context.applicationContext).also { instance = it }
        }
    }

    val accountStore = AccountStore(context)
    val settingStore = SettingStore(context)
    val draftStore = DraftStore(context)
}
