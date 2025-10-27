package com.nfcbumber.data.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service for encrypting and decrypting sensitive card data using Android Keystore.
 * Uses AES-256 encryption with GCM mode for authenticated encryption.
 */
@Singleton
class CryptoService @Inject constructor() {

    private val keyStore: KeyStore = KeyStore.getInstance(KEYSTORE_PROVIDER).apply {
        load(null)
    }

    /**
     * Encrypt data using AES-256-GCM.
     * @param data The data to encrypt
     * @return Encrypted data with IV prepended
     */
    fun encrypt(data: ByteArray): ByteArray {
        val key = getOrCreateKey()
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        
        val iv = cipher.iv
        val encrypted = cipher.doFinal(data)
        
        // Prepend IV to encrypted data
        return iv + encrypted
    }

    /**
     * Decrypt data using AES-256-GCM.
     * @param encryptedData Encrypted data with IV prepended
     * @return Decrypted data
     */
    fun decrypt(encryptedData: ByteArray): ByteArray {
        val key = getOrCreateKey()
        
        // Extract IV from the beginning
        val iv = encryptedData.copyOfRange(0, IV_SIZE)
        val encrypted = encryptedData.copyOfRange(IV_SIZE, encryptedData.size)
        
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(TAG_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, key, spec)
        
        return cipher.doFinal(encrypted)
    }

    /**
     * Securely overwrite data in memory.
     */
    fun securelyEraseData(data: ByteArray) {
        data.fill(0)
    }

    private fun getOrCreateKey(): SecretKey {
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            createKey()
        }
        
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }

    private fun createKey() {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            KEYSTORE_PROVIDER
        )
        
        val spec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(KEY_SIZE)
            .setUserAuthenticationRequired(false)
            .setRandomizedEncryptionRequired(true)
            .build()
        
        keyGenerator.init(spec)
        keyGenerator.generateKey()
    }

    companion object {
        private const val KEYSTORE_PROVIDER = "AndroidKeyStore"
        private const val KEY_ALIAS = "nfc_bumber_key"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val KEY_SIZE = 256
        private const val IV_SIZE = 12 // GCM standard IV size
        private const val TAG_LENGTH = 128 // GCM tag length in bits
    }
}
