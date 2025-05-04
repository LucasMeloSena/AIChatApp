package br.dev.lucasena.rocketia.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val USER_SETTINGS_DATA_STORE_NAME = "user_settings"
private const val SELECTED_STACK_KEY = "selected_stack_key"
private val SELECTED_STACK_PREFERENCES_KEY = stringPreferencesKey(name = SELECTED_STACK_KEY)

class UserSettingsPreferencesImpl @Inject constructor (
    @ApplicationContext private val context: Context,
): UserSettingsPreferences {
    val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = USER_SETTINGS_DATA_STORE_NAME)

    override val selectedStack: Flow<String?>
        get() = context.datastore.data.map { preferences ->
            preferences[SELECTED_STACK_PREFERENCES_KEY]
        }

    override suspend fun changeSelectedStack(stack: String) {
            context.datastore.edit { settings ->
                settings[SELECTED_STACK_PREFERENCES_KEY] = stack
            }
    }
}