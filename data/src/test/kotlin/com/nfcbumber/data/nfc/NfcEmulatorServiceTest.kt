package com.nfcbumber.data.nfc

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Unit tests for NFC Emulator Service APDU processing logic.
 * Note: These tests verify the byte array conversion utilities and APDU command formats.
 */
class NfcEmulatorServiceTest {

    @Test
    fun `hexStringToByteArray converts correctly`() {
        // Given
        val hex = "9000"
        
        // When
        val result = hexStringToByteArray(hex)
        
        // Then
        assertArrayEquals(byteArrayOf(0x90.toByte(), 0x00), result)
    }

    @Test
    fun `hexStringToByteArray handles longer strings`() {
        // Given
        val hex = "00A40400"
        
        // When
        val result = hexStringToByteArray(hex)
        
        // Then
        assertArrayEquals(
            byteArrayOf(0x00, 0xA4.toByte(), 0x04, 0x00), 
            result
        )
    }

    @Test
    fun `toHexString converts byte array correctly`() {
        // Given
        val bytes = byteArrayOf(0x01, 0x02, 0x03, 0x04)
        
        // When
        val result = bytes.toHexString()
        
        // Then
        assertEquals("01020304", result)
    }

    @Test
    fun `toHexString handles negative bytes`() {
        // Given
        val bytes = byteArrayOf(0x90.toByte(), 0x00, 0xFF.toByte())
        
        // When
        val result = bytes.toHexString()
        
        // Then
        assertEquals("9000FF", result)
    }

    @Test
    fun `toHexString handles empty array`() {
        // Given
        val bytes = byteArrayOf()
        
        // When
        val result = bytes.toHexString()
        
        // Then
        assertEquals("", result)
    }

    @Test
    fun `SELECT command format is correct`() {
        // Given - SELECT command format: 00 A4 04 00
        val selectCommand = byteArrayOf(0x00, 0xA4.toByte(), 0x04, 0x00)
        
        // When
        val hex = selectCommand.toHexString()
        
        // Then
        assertTrue(hex.startsWith("00A40400"))
    }

    @Test
    fun `READ BINARY command format is correct`() {
        // Given - READ BINARY command format: 00 B0 P1 P2 Le
        val readCommand = byteArrayOf(0x00, 0xB0.toByte(), 0x00, 0x00, 0x10)
        
        // When
        val cla = readCommand[0]
        val ins = readCommand[1]
        
        // Then
        assertEquals(0x00.toByte(), cla)
        assertEquals(0xB0.toByte(), ins)
        assertEquals(5, readCommand.size)
    }

    @Test
    fun `GET DATA command format is correct`() {
        // Given - GET DATA command format: 00 CA
        val getDataCommand = byteArrayOf(0x00, 0xCA.toByte(), 0x00, 0x00)
        
        // When
        val cla = getDataCommand[0]
        val ins = getDataCommand[1]
        
        // Then
        assertEquals(0x00.toByte(), cla)
        assertEquals(0xCA.toByte(), ins)
    }

    @Test
    fun `Status word SUCCESS is 9000`() {
        // Given
        val swSuccess = hexStringToByteArray("9000")
        
        // Then
        assertArrayEquals(byteArrayOf(0x90.toByte(), 0x00), swSuccess)
    }

    @Test
    fun `Status word FILE_NOT_FOUND is 6A82`() {
        // Given
        val swFileNotFound = hexStringToByteArray("6A82")
        
        // Then
        assertArrayEquals(
            byteArrayOf(0x6A.toByte(), 0x82.toByte()), 
            swFileNotFound
        )
    }

    @Test
    fun `UID concatenation with status word works correctly`() {
        // Given
        val uid = byteArrayOf(0x01, 0x02, 0x03, 0x04)
        val swSuccess = hexStringToByteArray("9000")
        
        // When
        val response = uid + swSuccess
        
        // Then
        assertEquals(6, response.size)
        assertArrayEquals(
            byteArrayOf(0x01, 0x02, 0x03, 0x04, 0x90.toByte(), 0x00),
            response
        )
    }

    @Test
    fun `ATS concatenation with status word works correctly`() {
        // Given
        val ats = byteArrayOf(0x78, 0x77, 0x71, 0x02)
        val swSuccess = hexStringToByteArray("9000")
        
        // When
        val response = ats + swSuccess
        
        // Then
        assertEquals(6, response.size)
        // Last two bytes should be status word
        assertEquals(0x90.toByte(), response[response.size - 2])
        assertEquals(0x00.toByte(), response[response.size - 1])
    }

    // Helper functions (these would normally be private in the service)
    private fun hexStringToByteArray(hex: String): ByteArray {
        val len = hex.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(hex[i], 16) shl 4) + 
                          Character.digit(hex[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

    private fun ByteArray.toHexString(): String {
        return joinToString("") { "%02X".format(it) }
    }
}
