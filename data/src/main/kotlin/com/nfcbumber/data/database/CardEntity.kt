package com.nfcbumber.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing an NFC card in the database.
 */
@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val uid: ByteArray,
    val ats: ByteArray?,
    val historicalBytes: ByteArray?,
    val aids: String = "", // Comma-separated Application Identifiers
    val cardType: String,
    val color: Int,
    val createdAt: Long,
    val lastUsedAt: Long?,
    val usageCount: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CardEntity

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

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + uid.contentHashCode()
        result = 31 * result + (ats?.contentHashCode() ?: 0)
        result = 31 * result + (historicalBytes?.contentHashCode() ?: 0)
        return result
    }
}
