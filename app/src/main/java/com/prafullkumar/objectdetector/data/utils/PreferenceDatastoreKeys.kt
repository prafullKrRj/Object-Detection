package com.prafullkumar.objectdetector.data.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import com.prafullkumar.objectdetector.utils.Constants

/**
 * Defines keys for accessing data in the preferences DataStore.
 */
object PreferenceDatastoreKeys {
    val USER_CONFIG = booleanPreferencesKey(name = Constants.USER_CONFIG)
}