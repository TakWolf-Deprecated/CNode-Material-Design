package org.cnodejs.android.md.vm

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import org.cnodejs.android.md.model.store.AppStoreHolder

class SettingViewModel(application: Application) : AndroidViewModel(application) {
    private val settingStore = AppStoreHolder.getInstance(application).settingStore

    val isThemeDarkModeData = MutableLiveData<Boolean>()
    val isTopicSaveDraftEnabledData = MutableLiveData(settingStore.isTopicSaveDraftEnabled())
    val isTopicSignEnabledData = MutableLiveData(settingStore.isTopicSignEnabled())
    val isTopicDisplayTabDevData = MutableLiveData(settingStore.isTopicDisplayTabDev())

    fun loadThemeConfig() {
        isThemeDarkModeData.value = settingStore.isThemeDarkMode()
    }

    fun toggleThemeDarkMode() {
        val isThemeDarkMode = !settingStore.isThemeDarkMode()
        settingStore.setThemeDarkMode(isThemeDarkMode)
        if (isThemeDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun toggleTopicSaveDraftEnabled() {
        val isTopicSaveDraftEnabled = !settingStore.isTopicSaveDraftEnabled()
        settingStore.setTopicSaveDraftEnabled(isTopicSaveDraftEnabled)
        isTopicSaveDraftEnabledData.value = isTopicSaveDraftEnabled
    }

    fun toggleTopicSignEnabled() {
        val isTopicSignEnabled = !settingStore.isTopicSignEnabled()
        settingStore.setTopicSignEnabled(isTopicSignEnabled)
        isTopicSignEnabledData.value = isTopicSignEnabled
    }

    fun toggleTopicDisplayTabDev() {
        val isTopicDisplayTabDev = !settingStore.isTopicDisplayTabDev()
        settingStore.setTopicDisplayTabDev(isTopicDisplayTabDev)
        isTopicDisplayTabDevData.value = isTopicDisplayTabDev
    }
}
