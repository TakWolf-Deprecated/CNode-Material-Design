package org.cnodejs.android.md.model.store

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SettingStore(application: Application) : DataStoreWrapper(application, "setting") {
    companion object {
        private val KEY_IS_NIGHT_MODE = booleanPreferencesKey("is_night_mode")
        private val KEY_IS_DISPLAY_TAB_DEB = booleanPreferencesKey("is_display_tab_dev")
    }

    fun isNightMode(): Boolean = runBlocking {
        dataStore.data.first()[KEY_IS_NIGHT_MODE] ?: false
    }

    fun setNightMode(value: Boolean) = runBlocking {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[KEY_IS_NIGHT_MODE] = value
        }
    }

    fun isDisplayTabDev(): Boolean = runBlocking {
        dataStore.data.first()[KEY_IS_DISPLAY_TAB_DEB] ?: false
    }

    fun setDisplayTabDev(value: Boolean) = runBlocking {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[KEY_IS_DISPLAY_TAB_DEB] = value
        }
    }
}
