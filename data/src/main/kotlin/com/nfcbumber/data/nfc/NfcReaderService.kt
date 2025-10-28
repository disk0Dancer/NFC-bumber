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
            val (ats, historicalBytes, cardType) = when {
                techList.contains(IsoDep::class.java.name) -> readIsoDep(tag)
                techList.contains(MifareClassic::class.java.name) -> readMifareClassic(tag)
                techList.contains(MifareUltralight::class.java.name) -> readMifareUltralight(tag)
                techList.contains(NfcA::class.java.name) -> readNfcA(tag)
                techList.contains(NfcB::class.java.name) -> readNfcB(tag)
                techList.contains(NfcF::class.java.name) -> readNfcF(tag)
                techList.contains(NfcV::class.java.name) -> readNfcV(tag)
                else -> Triple(null, null, CardType.UNKNOWN)
            }
            
            Log.d(TAG, "Card type: $cardType")
            Log.d(TAG, "ATS: ${ats?.toHexString() ?: "N/A"}")
            Log.d(TAG, "Historical bytes: ${historicalBytes?.toHexString() ?: "N/A"}")
            
            NfcCardData(
                uid = uid,
                ats = ats,
                historicalBytes = historicalBytes,
                cardType = cardType
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error reading NFC card", e)
            throw NfcReadException("Failed to read NFC card: ${e.message}", e)
        }
    }

    private fun readIsoDep(tag: Tag): Triple<ByteArray?, ByteArray?, CardType> {
        val isoDep = IsoDep.get(tag)
        return try {
            isoDep.connect()
            isoDep.timeout = CONNECTION_TIMEOUT_MS
            
            val ats = isoDep.historicalBytes ?: isoDep.hiLayerResponse
            val historicalBytes = isoDep.historicalBytes
            
            Triple(ats, historicalBytes, CardType.ISO_DEP)
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
        if (cardType != other.cardType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uid.contentHashCode()
        result = 31 * result + (ats?.contentHashCode() ?: 0)
        result = 31 * result + (historicalBytes?.contentHashCode() ?: 0)
        result = 31 * result + cardType.hashCode()
        return result
    }
}

/**
 * Exception thrown when NFC card reading fails.
 */
class NfcReadException(message: String, cause: Throwable? = null) : Exception(message, cause)
