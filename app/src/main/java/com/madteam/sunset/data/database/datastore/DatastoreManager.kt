package com.madteam.sunset.data.database.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val LAST_UPDATED_SPOT_ATTR = "LAST_UPDATED_SPOT_ATTR"

class DatastoreManager @Inject constructor(
    private val datastore: DataStore<Preferences>
) : DatastoreContract {

    override suspend fun saveLastSpotAttrUpdated(timestamp: Long) {
        datastore.edit { preferences ->
            preferences[longPreferencesKey(LAST_UPDATED_SPOT_ATTR)] = timestamp
        }
    }

    override fun getLastSpotAttrUpdated(): Flow<Long> {
        return datastore.data.map { preferences ->
            preferences[longPreferencesKey(LAST_UPDATED_SPOT_ATTR)] ?: 0L
        }
    }

}

interface DatastoreContract {
    suspend fun saveLastSpotAttrUpdated(timestamp: Long)
    fun getLastSpotAttrUpdated(): Flow<Long>
}