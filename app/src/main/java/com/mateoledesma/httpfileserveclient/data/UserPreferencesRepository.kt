package com.mateoledesma.httpfileserveclient.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

enum class SortBy {
    NAME,
    DATE,
    SIZE,
    TYPE,
}

class UserPreferencesRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {
    val isLinearLayout: Flow<Boolean> = dataStore.data.catch {
        if (it is IOException) {
            Log.e(TAG, "Error reading preferences.", it)
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map {
        it[IS_LINEAR_LAYOUT] ?: true
    }

    val sortBy: Flow<SortBy> = dataStore.data.catch {
        if (it is IOException) {
            Log.e(TAG, "Error reading preferences.", it)
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map {
        SortBy.valueOf(it[SORT_BY] ?: SortBy.NAME.name)
    }

    val isSortAscending: Flow<Boolean> = dataStore.data.catch {
        if (it is IOException) {
            Log.e(TAG, "Error reading preferences.", it)
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map {
        it[SORT_ASCENDING] ?: true
    }

    companion object {
        private val IS_LINEAR_LAYOUT = booleanPreferencesKey("is_linear_layout")
        private val SORT_BY = stringPreferencesKey("sort_by")
        private val SORT_ASCENDING = booleanPreferencesKey("sort_ascending")
        private const val TAG = "UserPreferencesRepo"
    }

    suspend fun saveLayoutPreference(isLinearLayout: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LINEAR_LAYOUT] = isLinearLayout
        }
    }

    suspend fun saveSortByPreference(sortBy: SortBy) {
        dataStore.edit { preferences ->
            preferences[SORT_BY] = sortBy.name
        }
    }

    suspend fun saveSortAscendingPreference(isSortAscending: Boolean) {
        dataStore.edit { preferences ->
            preferences[SORT_ASCENDING] = isSortAscending
        }
    }
}