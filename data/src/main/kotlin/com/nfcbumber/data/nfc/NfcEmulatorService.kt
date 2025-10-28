package com.nfcbumber.data.nfc

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import com.nfcbumber.data.database.CardDao
import com.nfcbumber.data.security.SecureStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * NFC Host Card Emulation service.
 * Emulates the selected NFC card when the device is near a terminal.
 */
@AndroidEntryPoint
class NfcEmulatorService : HostApduService() {

    @Inject
    lateinit var cardDao: CardDao

    @Inject
    lateinit var secureStorage: SecureStorage

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    companion object {
        private const val TAG = "NfcEmulatorService"
        
        // APDU Commands
        private const val SELECT_APDU_HEADER = "00A40400"
        private const val READ_BINARY_COMMAND: Byte = 0xB0.toByte()
        
        // Status Words (ISO 7816-4)
        private val SW_SUCCESS = hexStringToByteArray("9000")
        private val SW_FILE_NOT_FOUND = hexStringToByteArray("6A82")
        private val SW_WRONG_LENGTH = hexStringToByteArray("6700")
        private val SW_COMMAND_NOT_ALLOWED = hexStringToByteArray("6986")
        private val SW_UNKNOWN = hexStringToByteArray("6F00")
        
        // AID (Application Identifier) - Default for generic HCE
        private const val DEFAULT_AID = "F0010203040506"
        
        private const val KEY_SELECTED_CARD_ID = "selected_card_id"
    }

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        if (commandApdu == null) {
            Log.w(TAG, "Received null APDU command")
            return SW_UNKNOWN
        }

        val commandHex = commandApdu.toHexString()
        Log.d(TAG, "Received APDU: $commandHex")

        return try {
            when {
                // SELECT command (00 A4 04 00)
                commandHex.startsWith(SELECT_APDU_HEADER) -> {
                    handleSelectCommand(commandApdu)
                }
                // READ BINARY command (00 B0)
                commandApdu.size >= 2 && 
                commandApdu[0] == 0x00.toByte() && 
                commandApdu[1] == READ_BINARY_COMMAND -> {
                    handleReadBinaryCommand(commandApdu)
                }
                // GET DATA command (00 CA) - for UID
                commandApdu.size >= 2 && 
                commandApdu[0] == 0x00.toByte() && 
                commandApdu[1] == 0xCA.toByte() -> {
                    handleGetDataCommand(commandApdu)
                }
                else -> {
                    Log.w(TAG, "Unknown APDU command: $commandHex")
                    SW_COMMAND_NOT_ALLOWED
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error processing APDU: ${e.message}", e)
            SW_UNKNOWN
        }
    }

    override fun onDeactivated(reason: Int) {
        val reasonText = when (reason) {
            DEACTIVATION_LINK_LOSS -> "Link lost"
            DEACTIVATION_DESELECTED -> "Deselected"
            else -> "Unknown ($reason)"
        }
        Log.d(TAG, "NFC service deactivated: $reasonText")
        
        // Update usage count if a card was used
        updateCardUsage()
    }

    /**
     * Handle SELECT command (00 A4 04 00).
     * Returns ATS (Answer To Select) if available.
     */
    private fun handleSelectCommand(commandApdu: ByteArray): ByteArray {
        Log.d(TAG, "Processing SELECT command")
        
        val card = getSelectedCard()
        
        return if (card != null) {
            Log.d(TAG, "Card selected: ${card.name}, UID: ${card.uid.toHexString()}")
            
            // Return ATS if available, otherwise just success
            if (card.ats != null && card.ats.isNotEmpty()) {
                Log.d(TAG, "Returning ATS: ${card.ats.toHexString()}")
                card.ats + SW_SUCCESS
            } else {
                Log.d(TAG, "No ATS available, returning success")
                SW_SUCCESS
            }
        } else {
            Log.w(TAG, "No card selected for emulation")
            SW_FILE_NOT_FOUND
        }
    }

    /**
     * Handle READ BINARY command (00 B0 P1 P2 Le).
     * Returns historical bytes or card data.
     */
    private fun handleReadBinaryCommand(commandApdu: ByteArray): ByteArray {
        Log.d(TAG, "Processing READ BINARY command")
        
        if (commandApdu.size < 5) {
            Log.w(TAG, "READ BINARY command too short")
            return SW_WRONG_LENGTH
        }
        
        val card = getSelectedCard()
        
        return if (card != null) {
            // Return historical bytes if available
            if (card.historicalBytes != null && card.historicalBytes.isNotEmpty()) {
                Log.d(TAG, "Returning historical bytes: ${card.historicalBytes.toHexString()}")
                card.historicalBytes + SW_SUCCESS
            } else {
                Log.d(TAG, "No historical bytes available")
                SW_SUCCESS
            }
        } else {
            Log.w(TAG, "No card selected for READ BINARY")
            SW_FILE_NOT_FOUND
        }
    }

    /**
     * Handle GET DATA command (00 CA) to return UID.
     */
    private fun handleGetDataCommand(commandApdu: ByteArray): ByteArray {
        Log.d(TAG, "Processing GET DATA command")
        
        val card = getSelectedCard()
        
        return if (card != null) {
            Log.d(TAG, "Returning UID: ${card.uid.toHexString()}")
            card.uid + SW_SUCCESS
        } else {
            Log.w(TAG, "No card selected for GET DATA")
            SW_FILE_NOT_FOUND
        }
    }

    /**
     * Get the currently selected card for emulation.
     */
    private fun getSelectedCard(): EmulatedCardData? {
        return try {
            val selectedCardId = secureStorage.getLong(KEY_SELECTED_CARD_ID, -1)
            
            if (selectedCardId == -1L) {
                Log.w(TAG, "No card selected")
                return null
            }
            
            // Use runBlocking here as we're in a synchronous callback
            runBlocking {
                val cardEntity = cardDao.getCardById(selectedCardId)
                if (cardEntity != null) {
                    EmulatedCardData(
                        uid = cardEntity.uid,
                        ats = cardEntity.ats,
                        historicalBytes = cardEntity.historicalBytes,
                        name = cardEntity.name
                    )
                } else {
                    Log.w(TAG, "Card not found: $selectedCardId")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting selected card: ${e.message}", e)
            null
        }
    }

    /**
     * Update the usage count and last used timestamp for the selected card.
     */
    private fun updateCardUsage() {
        serviceScope.launch {
            try {
                val selectedCardId = secureStorage.getLong(KEY_SELECTED_CARD_ID, -1)
                if (selectedCardId != -1L) {
                    val timestamp = System.currentTimeMillis()
                    cardDao.updateCardUsage(selectedCardId, timestamp)
                    Log.d(TAG, "Updated usage for card: $selectedCardId")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating card usage: ${e.message}", e)
            }
        }
    }

    // Extension functions for byte array conversions
    private fun ByteArray.toHexString(): String {
        return joinToString("") { "%02X".format(it) }
    }

}

/**
 * Extension function to convert a hex string to a byte array.
 */
fun String.hexToByteArray(): ByteArray {
    val len = this.length
    val data = ByteArray(len / 2)
    var i = 0
    while (i < len) {
        data[i / 2] = ((Character.digit(this[i], 16) shl 4) + Character.digit(this[i + 1], 16)).toByte()
        i += 2
    }
    return data
}

/**
 * Extension to SecureStorage for Long values.
 */
private fun SecureStorage.getLong(key: String, defaultValue: Long): Long {
    return getString(key)?.toLongOrNull() ?: defaultValue
}

private fun SecureStorage.putLong(key: String, value: Long) {
    putString(key, value.toString())
}

/**
 * Simple data class for emulated card information.
 */
private data class EmulatedCardData(
    val uid: ByteArray,
    val ats: ByteArray?,
    val historicalBytes: ByteArray?,
    val name: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EmulatedCardData) return false

        if (!uid.contentEquals(other.uid)) return false
        if (ats != null) {
            if (other.ats == null || !ats.contentEquals(other.ats)) return false
        } else if (other.ats != null) return false
        if (historicalBytes != null) {
            if (other.historicalBytes == null || !historicalBytes.contentEquals(other.historicalBytes)) return false
        } else if (other.historicalBytes != null) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uid.contentHashCode()
        result = 31 * result + (ats?.contentHashCode() ?: 0)
        result = 31 * result + (historicalBytes?.contentHashCode() ?: 0)
        result = 31 * result + name.hashCode()
        return result
    }
}
