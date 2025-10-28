package com.nfcbumber.data.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Secure storage for sensitive application settings using EncryptedSharedPreferences.
 */
@Singleton
class SecureStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    /**
     * Store a string value securely.
     */
    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    /**
     * Retrieve a string value.
     */
    fun getString(key: String, defaultValue: String? = null): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    /**
     * Store a boolean value securely.
     */
    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    /**
     * Retrieve a boolean value.
     */
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    /**
     * Store an integer value securely.
     */
    fun putInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    /**
     * Retrieve an integer value.
     */
    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    /**
     * Remove a specific key.
     */
    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    /**
     * Clear all stored data.
     */
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    /**
     * Store a long value securely.
     */
    fun putLong(key: String, value: Long) {
        putString(key, value.toString())
    }

    /**
     * Retrieve a long value.
     */
    fun getLong(key: String, defaultValue: Long = 0): Long {
        return getString(key)?.toLongOrNull() ?: defaultValue
    }

    companion object {
        private const val PREFS_NAME = "nfc_bumber_secure_prefs"
        
        // Keys for common settings
        const val KEY_BIOMETRIC_ENABLED = "biometric_enabled"
        const val KEY_AUTO_LOCK_TIMEOUT = "auto_lock_timeout"
        const val KEY_HAPTIC_FEEDBACK = "haptic_feedback"
        const val KEY_SOUND_FEEDBACK = "sound_feedback"
    }
}
