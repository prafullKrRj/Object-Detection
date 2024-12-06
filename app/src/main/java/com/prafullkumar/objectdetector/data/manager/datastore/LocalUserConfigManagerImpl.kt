package com.prafullkumar.objectdetector.data.manager.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.prafullkumar.objectdetector.domain.manager.datastore.LocalUserConfigManager
import com.prafullkumar.objectdetector.data.utils.PreferenceDatastoreKeys
import com.prafullkumar.objectdetector.utils.extensions.datastore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Defines an implementation of LocalUserConfigManager for managing user configuration data.
 * This implementation injects a Context to interact with DataStore preferences.
 */
class LocalUserConfigManagerImpl(
    private val context: Context
) : LocalUserConfigManager {

    /**
     * Writes a user configuration to the DataStore. This function is suspendable to handle
     * the potential delay of file I/O operations asynchronously.
     */
    override suspend fun writeUserConfig() {
        // Using extension function obtain instance of 'userConfigDatastore' to write value
        context.datastore.edit { userConfigDatastore ->
            userConfigDatastore[PreferenceDatastoreKeys.USER_CONFIG] = true
        }
    }

    /**
     * Reads the user configuration from the DataStore as a Flow. The Flow is then used here for
     * asynchronous stream processing, allowing the caller to collect updates to the user configuration as they occur.
     */
    override fun readUserConfig(): Flow<Boolean> {
        // Using extension function obtain instance of 'userConfigDatastore' to read keys
        return context.datastore.data
            .map { preferences ->
                // Initializing value to 'false' if key does-not-exist
                preferences[PreferenceDatastoreKeys.USER_CONFIG] ?: false
            }
    }
}