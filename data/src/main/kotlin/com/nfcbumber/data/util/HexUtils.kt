package com.nfcbumber.data.util

/**
 * Utility functions for hex string and byte array conversions.
 * Used for NFC APDU command processing and card data handling.
 */

/**
 * Extension function to convert a byte array to a hex string.
 * Each byte is formatted as a two-character uppercase hex value.
 *
 * @return Hex string representation (e.g., "01020304" for [0x01, 0x02, 0x03, 0x04])
 */
fun ByteArray.toHexString(): String {
    return joinToString("") { "%02X".format(it) }
}

/**
 * Extension function to convert a hex string to a byte array.
 * The string must have an even length (two hex characters per byte).
 *
 * @return ByteArray representation of the hex string
 * @throws IllegalArgumentException if the string has odd length or invalid hex characters
 */
fun String.hexToByteArray(): ByteArray {
    val len = this.length
    require(len % 2 == 0) { "Hex string must have even length" }
    
    val data = ByteArray(len / 2)
    var i = 0
    while (i < len) {
        data[i / 2] = ((Character.digit(this[i], 16) shl 4) + Character.digit(this[i + 1], 16)).toByte()
        i += 2
    }
    return data
}
