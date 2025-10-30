package com.nfcbumber.domain.model

import java.time.LocalDateTime

/**
 * Domain model representing an NFC card.
 */
data class Card(
    val id: Long = 0,
    val name: String,
    val uid: ByteArray,
    val ats: ByteArray?,
    val historicalBytes: ByteArray?,
    val aids: List<String> = emptyList(), // Application Identifiers discovered from the card
    val cardType: CardType,
    val color: Int,
    val createdAt: LocalDateTime,
    val lastUsedAt: LocalDateTime?,
    val usageCount: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Card

        if (id != other.id) return false
        if (name != other.name) return false
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
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + uid.contentHashCode()
        result = 31 * result + (ats?.contentHashCode() ?: 0)
        result = 31 * result + (historicalBytes?.contentHashCode() ?: 0)
        result = 31 * result + aids.hashCode()
        result = 31 * result + cardType.hashCode()
        return result
    }
}

/**
 * Types of NFC cards supported by the application.
 */
enum class CardType {
    MIFARE_CLASSIC,
    MIFARE_ULTRALIGHT,
    ISO_DEP,
    NFC_A,
    NFC_B,
    NFC_F,
    NFC_V,
    UNKNOWN
}
