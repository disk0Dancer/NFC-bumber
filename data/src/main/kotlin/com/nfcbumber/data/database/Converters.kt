package com.nfcbumber.data.database

import androidx.room.TypeConverter

/**
 * Type converters for Room database.
 */
class Converters {
    @TypeConverter
    fun fromByteArray(value: ByteArray?): String? {
        return value?.let { bytesToHex(it) }
    }

    @TypeConverter
    fun toByteArray(value: String?): ByteArray? {
        return value?.let { hexToBytes(it) }
    }

    private fun bytesToHex(bytes: ByteArray): String {
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun hexToBytes(hex: String): ByteArray {
        return hex.chunked(2)
            .map { it.toInt(16).toByte() }
            .toByteArray()
    }
}
