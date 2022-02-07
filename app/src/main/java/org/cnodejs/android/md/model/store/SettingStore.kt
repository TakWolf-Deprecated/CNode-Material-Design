package org.cnodejs.android.md.model.store

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SettingStore(application: Application) : DataStoreWrapper(application, "setting") {
    companion object {
        private val KEY_NIGHT_MODE = booleanPreferencesKey("night_mode")
    }

    fun isNightMode(): Boolean = runBlocking {
        dataStore.data.first()[KEY_NIGHT_MODE] ?: false
    }

    fun setNightMode(enabled: Boolean) = runBlocking {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[KEY_NIGHT_MODE] = enabled
        }
    }
}
