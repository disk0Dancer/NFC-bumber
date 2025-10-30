package com.nfcbumber.data.nfc

import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.nfc.tech.MifareClassic
import android.nfc.tech.MifareUltralight
import android.nfc.tech.NfcA
import android.nfc.tech.NfcB
import android.nfc.tech.NfcF
import android.nfc.tech.NfcV
import android.util.Log
import com.nfcbumber.domain.model.CardType
import com.nfcbumber.domain.util.toHexString
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service for reading NFC card data.
 * Handles tag detection, connection, and data extraction.
 */
@Singleton
class NfcReaderService @Inject constructor() {

    companion object {
        private const val TAG = "NfcReaderService"
        private const val CONNECTION_TIMEOUT_MS = 5000
    }

    /**
     * Read card data from an NFC tag.
     * @param tag The NFC tag detected by the system
     * @return NfcCardData containing the card information
     * @throws NfcReadException if reading fails
     */
    suspend fun readCard(tag: Tag): NfcCardData {
        return try {
            Log.d(TAG, "Reading NFC card...")
            
            val uid = tag.id
            Log.d(TAG, "UID: ${uid.toHexString()}")
            
            val techList = tag.techList
            Log.d(TAG, "Available technologies: ${techList.joinToString()}")
            
            // Try to read as ISO-DEP first (most common for smart cards)
            val (cardData, aids) = when {
                techList.contains(IsoDep::class.java.name) -> readIsoDep(tag)
                techList.contains(MifareClassic::class.java.name) -> Pair(readMifareClassic(tag), emptyList())
                techList.contains(MifareUltralight::class.java.name) -> Pair(readMifareUltralight(tag), emptyList())
                techList.contains(NfcA::class.java.name) -> Pair(readNfcA(tag), emptyList())
                techList.contains(NfcB::class.java.name) -> Pair(readNfcB(tag), emptyList())
                techList.contains(NfcF::class.java.name) -> Pair(readNfcF(tag), emptyList())
                techList.contains(NfcV::class.java.name) -> Pair(readNfcV(tag), emptyList())
                else -> Pair(Triple(null, null, CardType.UNKNOWN), emptyList())
            }
            
            val (ats, historicalBytes, cardType) = cardData
            
            Log.d(TAG, "Card type: $cardType")
            Log.d(TAG, "ATS: ${ats?.toHexString() ?: "N/A"}")
            Log.d(TAG, "Historical bytes: ${historicalBytes?.toHexString() ?: "N/A"}")
            Log.d(TAG, "Discovered AIDs: ${aids.joinToString()}")
            
            NfcCardData(
                uid = uid,
                ats = ats,
                historicalBytes = historicalBytes,
                aids = aids,
                cardType = cardType
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error reading NFC card", e)
            throw NfcReadException("Failed to read NFC card: ${e.message}", e)
        }
    }

    private fun readIsoDep(tag: Tag): Pair<Triple<ByteArray?, ByteArray?, CardType>, List<String>> {
        val isoDep = IsoDep.get(tag)
        return try {
            isoDep.connect()
            isoDep.timeout = CONNECTION_TIMEOUT_MS
            
            val ats = isoDep.historicalBytes ?: isoDep.hiLayerResponse
            val historicalBytes = isoDep.historicalBytes
            
            // Try to discover AIDs by sending SELECT commands for common AIDs
            val aids = discoverAids(isoDep)
            
            Pair(Triple(ats, historicalBytes, CardType.ISO_DEP), aids)
        } finally {
            try {
                if (isoDep.isConnected) {
                    isoDep.close()
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error closing ISO-DEP connection", e)
            }
        }
    }
    
    /**
     * Discover AIDs supported by the card by probing common AIDs.
     * This helps identify what applications the card supports.
     */
    private fun discoverAids(isoDep: IsoDep): List<String> {
        val discoveredAids = mutableListOf<String>()
        
        // Common AIDs for access control, transit, and payment systems
        val commonAids = listOf(
            "F0010203040506",      // Generic/Default AID
            "A0000000031010",      // Visa
            "A0000000041010",      // Mastercard
            "A0000000032010",      // Visa Electron
            "A0000000999999",      // Generic payment
            "D2760000850100",      // MIFARE DESFire
            "D2760000850101",      // MIFARE DESFire EV1
            "315449432E494341",    // Transit card (STIC.ICA)
            "A000000618",          // Access control
            "A00000061701",        // Access control (HID)
            "F04E4643424D42455200" // NFCBUMBER (our app AID)
        )
        
        for (aid in commonAids) {
            try {
                val selectCommand = buildSelectApdu(aid)
                val response = isoDep.transceive(selectCommand)
                
                // Check if response indicates success (SW1 SW2 = 90 00)
                if (response.size >= 2 && 
                    response[response.size - 2] == 0x90.toByte() && 
                    response[response.size - 1] == 0x00.toByte()) {
                    Log.d(TAG, "Discovered AID: $aid")
                    discoveredAids.add(aid)
                }
            } catch (e: Exception) {
                // Card doesn't support this AID, continue
                Log.v(TAG, "AID $aid not supported: ${e.message}")
            }
        }
        
        return discoveredAids
    }
    
    /**
     * Build a SELECT APDU command for a given AID.
     */
    private fun buildSelectApdu(aid: String): ByteArray {
        val aidBytes = aid.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
        return byteArrayOf(
            0x00.toByte(), // CLA
            0xA4.toByte(), // INS (SELECT)
            0x04.toByte(), // P1 (Select by name)
            0x00.toByte(), // P2
            aidBytes.size.toByte() // Lc (length of AID)
        ) + aidBytes
    }

    private fun readMifareClassic(tag: Tag): Triple<ByteArray?, ByteArray?, CardType> {
        val mifare = MifareClassic.get(tag)
        return try {
            mifare.connect()
            mifare.setTimeout(CONNECTION_TIMEOUT_MS)
            
            // For MIFARE Classic, we don't have ATS/historical bytes
            // Just return the card type
            Triple(null, null, CardType.MIFARE_CLASSIC)
        } finally {
            try {
                if (mifare.isConnected) {
                    mifare.close()
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error closing MIFARE Classic connection", e)
            }
        }
    }

    private fun readMifareUltralight(tag: Tag): Triple<ByteArray?, ByteArray?, CardType> {
        val mifare = MifareUltralight.get(tag)
        return try {
            mifare.connect()
            mifare.setTimeout(CONNECTION_TIMEOUT_MS)
            
            // For MIFARE Ultralight, we don't have ATS/historical bytes
            Triple(null, null, CardType.MIFARE_ULTRALIGHT)
        } finally {
            try {
                if (mifare.isConnected) {
                    mifare.close()
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error closing MIFARE Ultralight connection", e)
            }
        }
    }

    private fun readNfcA(tag: Tag): Triple<ByteArray?, ByteArray?, CardType> {
        val nfcA = NfcA.get(tag)
        return try {
            nfcA.connect()
            nfcA.timeout = CONNECTION_TIMEOUT_MS
            
            val atqa = nfcA.atqa
            val sak = nfcA.sak
            
            Log.d(TAG, "NFC-A ATQA: ${atqa.toHexString()}")
            Log.d(TAG, "NFC-A SAK: ${sak.toByte().toString(16)}")
            
            Triple(atqa, null, CardType.NFC_A)
        } finally {
            try {
                if (nfcA.isConnected) {
                    nfcA.close()
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error closing NFC-A connection", e)
            }
        }
    }

    private fun readNfcB(tag: Tag): Triple<ByteArray?, ByteArray?, CardType> {
        val nfcB = NfcB.get(tag)
        return try {
            nfcB.connect()
            nfcB.maxTransceiveLength
            
            val appData = nfcB.applicationData
            val protocolInfo = nfcB.protocolInfo
            
            Log.d(TAG, "NFC-B App Data: ${appData.toHexString()}")
            Log.d(TAG, "NFC-B Protocol Info: ${protocolInfo.toHexString()}")
            
            Triple(appData, protocolInfo, CardType.NFC_B)
        } finally {
            try {
                if (nfcB.isConnected) {
                    nfcB.close()
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error closing NFC-B connection", e)
            }
        }
    }

    private fun readNfcF(tag: Tag): Triple<ByteArray?, ByteArray?, CardType> {
        val nfcF = NfcF.get(tag)
        return try {
            nfcF.connect()
            nfcF.timeout = CONNECTION_TIMEOUT_MS
            
            val manufacturer = nfcF.manufacturer
            val systemCode = nfcF.systemCode
            
            Log.d(TAG, "NFC-F Manufacturer: ${manufacturer.toHexString()}")
            Log.d(TAG, "NFC-F System Code: ${systemCode.toHexString()}")
            
            Triple(manufacturer, systemCode, CardType.NFC_F)
        } finally {
            try {
                if (nfcF.isConnected) {
                    nfcF.close()
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error closing NFC-F connection", e)
            }
        }
    }

    private fun readNfcV(tag: Tag): Triple<ByteArray?, ByteArray?, CardType> {
        val nfcV = NfcV.get(tag)
        return try {
            nfcV.connect()
            
            val dsfId = nfcV.dsfId
            val responseFlags = nfcV.responseFlags
            
            Log.d(TAG, "NFC-V DSF ID: ${dsfId.toByte().toString(16)}")
            Log.d(TAG, "NFC-V Response Flags: ${responseFlags.toByte().toString(16)}")
            
            Triple(null, null, CardType.NFC_V)
        } finally {
            try {
                if (nfcV.isConnected) {
                    nfcV.close()
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error closing NFC-V connection", e)
            }
        }
    }
}

/**
 * Data class containing NFC card information.
 */
data class NfcCardData(
    val uid: ByteArray,
    val ats: ByteArray?,
    val historicalBytes: ByteArray?,
    val aids: List<String> = emptyList(),
    val cardType: CardType
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NfcCardData

        if (!uid.contentEquals(other.uid)) return false
        if (ats != null) {
            if (other.ats == null) return false
            if (!ats.contentEquals(other.ats)) return false
        } else if (other.ats != null) return false
        if (historicalBytes != null) {
            if (other.historicalBytes == null) return false
            if (!historicalBytes.contentEquals(other.historicalBytes)) return false
        } else if (other.historicalBytes != null) return false
        if (aids != other.aids) return false
        if (cardType != other.cardType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uid.contentHashCode()
        result = 31 * result + (ats?.contentHashCode() ?: 0)
        result = 31 * result + (historicalBytes?.contentHashCode() ?: 0)
        result = 31 * result + aids.hashCode()
        result = 31 * result + cardType.hashCode()
        return result
    }
}

/**
 * Exception thrown when NFC card reading fails.
 */
class NfcReadException(message: String, cause: Throwable? = null) : Exception(message, cause)
