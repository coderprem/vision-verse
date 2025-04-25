package com.example.visionverse.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

    companion object {
        private val IS_USER_LOGGED_IN = booleanPreferencesKey("is_user_logged_in")
        private val LOGGED_IN_EMAIL = stringPreferencesKey("logged_in_email")
    }

    suspend fun setLoggedIn(email: String) {
        context.dataStore.edit { prefs ->
            prefs[IS_USER_LOGGED_IN] = true
            prefs[LOGGED_IN_EMAIL] = email
        }
    }

    suspend fun setLoggedOut() {
        context.dataStore.edit { prefs ->
            prefs[IS_USER_LOGGED_IN] = false
            prefs.remove(LOGGED_IN_EMAIL)
        }
    }

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[IS_USER_LOGGED_IN] ?: false }

    val loggedInEmailFlow: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[LOGGED_IN_EMAIL] }
}
