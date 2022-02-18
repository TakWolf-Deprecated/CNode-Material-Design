package org.cnodejs.android.md.model.store

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SettingStore(application: Application) : DataStoreWrapper(application, "setting") {
    companion object {
        private val KEY_IS_THEME_DARK_MODE = booleanPreferencesKey("isThemeDarkMode")
        private val KEY_IS_TOPIC_SAVE_DRAFT_ENABLED = booleanPreferencesKey("isTopicSaveDraftEnabled")
        private val KEY_IS_TOPIC_SIGN_ENABLED = booleanPreferencesKey("isTopicSignEnabled")
        private val KEY_TOPIC_SIGN = stringPreferencesKey("topicSign")
        private val KEY_IS_TOPIC_DISPLAY_TAB_DEB = booleanPreferencesKey("isTopicDisplayTabDev")

        val DEFAULT_TOPIC_SIGN = "来自酷炫的 [CNodeMD](https://github.com/TakWolf/CNode-Material-Design)"
    }

    fun isThemeDarkMode(): Boolean = runBlocking {
        dataStore.data.first()[KEY_IS_THEME_DARK_MODE] ?: false
    }

    fun setThemeDarkMode(value: Boolean) = runBlocking {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[KEY_IS_THEME_DARK_MODE] = value
        }
    }

    fun isTopicSaveDraftEnabled(): Boolean = runBlocking {
        dataStore.data.first()[KEY_IS_TOPIC_SAVE_DRAFT_ENABLED] ?: true
    }

    fun setTopicSaveDraftEnabled(value: Boolean) = runBlocking {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[KEY_IS_TOPIC_SAVE_DRAFT_ENABLED] = value
        }
    }

    fun isTopicSignEnabled(): Boolean = runBlocking {
        dataStore.data.first()[KEY_IS_TOPIC_SIGN_ENABLED] ?: true
    }

    fun setTopicSignEnabled(value: Boolean) = runBlocking {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[KEY_IS_TOPIC_SIGN_ENABLED] = value
        }
    }

    fun getTopicSign(): String = runBlocking {
        dataStore.data.first()[KEY_TOPIC_SIGN] ?: DEFAULT_TOPIC_SIGN
    }

    fun isTopicDisplayTabDev(): Boolean = runBlocking {
        dataStore.data.first()[KEY_IS_TOPIC_DISPLAY_TAB_DEB] ?: false
    }

    fun setTopicDisplayTabDev(value: Boolean) = runBlocking {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[KEY_IS_TOPIC_DISPLAY_TAB_DEB] = value
        }
    }
}
