package org.cnodejs.android.md

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import org.cnodejs.android.md.model.store.AppStoreHolder

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val settingStore = AppStoreHolder.getInstance(this).settingStore
        if (settingStore.isThemeDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
