package org.cnodejs.android.md.vm

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import org.cnodejs.android.md.model.store.AppStoreHolder

class SettingViewModel(application: Application) : AndroidViewModel(application) {
    private val settingStore = AppStoreHolder.getInstance(application).settingStore

    val isNightModeData = MutableLiveData<Boolean>()
    val isDisplayTabDevData = MutableLiveData(settingStore.isDisplayTabDev())

    fun loadNightModeConfig() {
        isNightModeData.value = settingStore.isNightMode()
    }

    fun toggleNightMode() {
        val isNightMode = !settingStore.isNightMode()
        settingStore.setNightMode(isNightMode)
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun toggleDisplayTabDev() {
        val isDisplayTabDev = !settingStore.isDisplayTabDev()
        settingStore.setDisplayTabDev(isDisplayTabDev)
        isDisplayTabDevData.value = isDisplayTabDev
    }
}
