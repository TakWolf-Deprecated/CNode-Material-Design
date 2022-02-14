package org.cnodejs.android.md.model.store

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.cnodejs.android.md.bus.AccountChangedEvent
import org.cnodejs.android.md.bus.AccountUpdatedEvent
import org.cnodejs.android.md.model.entity.Account
import org.cnodejs.android.md.model.entity.LoginResult
import org.cnodejs.android.md.model.entity.User
import org.greenrobot.eventbus.EventBus

class AccountStore(application: Application) : DataStoreWrapper(application, "account") {
    companion object {
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("accessToken")
        private val KEY_ID = stringPreferencesKey("id")
        private val KEY_LOGIN_NAME = stringPreferencesKey("loginName")
        private val KEY_AVATAR_URL = stringPreferencesKey("avatarUrl")
        private val KEY_SCORE = intPreferencesKey("score")
    }

    fun login(accessToken: String, loginResult: LoginResult): Account = runBlocking {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[KEY_ACCESS_TOKEN] = accessToken
            mutablePreferences[KEY_ID] = loginResult.id
            mutablePreferences[KEY_LOGIN_NAME] = loginResult.loginName
            mutablePreferences[KEY_AVATAR_URL] = loginResult.avatarUrl
            mutablePreferences[KEY_SCORE] = 0
        }
        val account = Account(
            accessToken,
            loginResult.id,
            loginResult.loginName,
            loginResult.avatarUrl,
            0,
        )
        EventBus.getDefault().post(AccountChangedEvent(account))
        account
    }

    fun update(user: User) = runBlocking {
        dataStore.edit { mutablePreferences ->
            if (mutablePreferences[KEY_ACCESS_TOKEN] == null || mutablePreferences[KEY_LOGIN_NAME] != user.loginName) {
                return@edit
            }
            mutablePreferences[KEY_AVATAR_URL] = user.avatarUrl
            mutablePreferences[KEY_SCORE] = user.score

            val account = Account(
                mutablePreferences[KEY_ACCESS_TOKEN]!!,
                mutablePreferences[KEY_ID]!!,
                user.loginName,
                user.avatarUrl,
                user.score,
            )
            EventBus.getDefault().post(AccountUpdatedEvent(account))
        }
    }

    fun getAccount(): Account? = runBlocking {
        val preferences = dataStore.data.first()
        if (preferences[KEY_ACCESS_TOKEN] == null) {
            null
        } else {
            Account(
                preferences[KEY_ACCESS_TOKEN]!!,
                preferences[KEY_ID]!!,
                preferences[KEY_LOGIN_NAME] ?: "",
                preferences[KEY_AVATAR_URL],
                preferences[KEY_SCORE] ?: 0,
            )
        }
    }

    fun getAccessToken(): String? = runBlocking {
        dataStore.data.first()[KEY_ACCESS_TOKEN]
    }

    fun getLoginName(): String? = runBlocking {
        dataStore.data.first()[KEY_LOGIN_NAME]
    }

    fun logout() = runBlocking {
        dataStore.edit { mutablePreferences ->
            mutablePreferences.clear()
        }
        EventBus.getDefault().post(AccountChangedEvent(null))
    }
}
