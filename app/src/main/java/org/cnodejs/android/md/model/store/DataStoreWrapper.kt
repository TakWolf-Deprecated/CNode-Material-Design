package org.cnodejs.android.md.model.store

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class DataStoreWrapper protected constructor(
    application: Application,
    name: String,
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
    produceMigrations: (Context) -> List<DataMigration<Preferences>> = { listOf() },
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
) {
    protected val dataStore = PreferenceDataStoreFactory.create(
        corruptionHandler = corruptionHandler,
        migrations = produceMigrations(application),
        scope = scope,
    ) {
        application.preferencesDataStoreFile(name)
    }
}
