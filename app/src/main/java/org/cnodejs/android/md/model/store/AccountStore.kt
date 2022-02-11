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
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_ID = stringPreferencesKey("id")
        private val KEY_LOGIN_NAME = stringPreferencesKey("login_name")
        private val KEY_AVATAR_URL = stringPreferencesKey("avatar_url")
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
            id = loginResult.id,
            loginName = loginResult.loginName,
            avatarUrl = loginResult.avatarUrl,
            score = 0,
        )
        EventBus.getDefault().post(AccountChangedEvent(account))
        account
    }

    fun update(user: User): Account? = runBlocking {
        val preferences = dataStore.data.first()
        if (preferences[KEY_ACCESS_TOKEN] == null || preferences[KEY_LOGIN_NAME] != user.loginName) {
            null
        } else {
            dataStore.edit { mutablePreferences ->
                mutablePreferences[KEY_AVATAR_URL] = user.avatarUrl
                mutablePreferences[KEY_SCORE] = user.score
            }
            val account = Account(
                id = dataStore.data.first()[KEY_ID] ?: "",
                loginName = user.loginName,
                avatarUrl = user.avatarUrl,
                score = user.score,
            )
            EventBus.getDefault().post(AccountUpdatedEvent(account))
            account
        }
    }

    fun getAccessToken(): String? = runBlocking {
        dataStore.data.first()[KEY_ACCESS_TOKEN]
    }

    fun requireAccessToken(): String {
        return getAccessToken() ?: ""
    }

    fun getAccount(): Account? = runBlocking {
        val preferences = dataStore.data.first()
        if (preferences[KEY_ACCESS_TOKEN] == null) {
            null
        } else {
            Account(
                id = preferences[KEY_ID] ?: "",
                loginName = preferences[KEY_LOGIN_NAME] ?: "",
                avatarUrl = preferences[KEY_AVATAR_URL],
                score = preferences[KEY_SCORE] ?: 0,
            )
        }
    }

    fun logout() = runBlocking {
        dataStore.edit { mutablePreferences ->
            mutablePreferences.clear()
        }
        EventBus.getDefault().post(AccountChangedEvent(null))
    }
}
