package com.nfcbumber.domain.model

import java.time.LocalDateTime

/**
 * Model for card backup/export.
 * Contains all card information in a serializable format.
 */
data class CardBackup(
    val version: Int = 1,
    val exportedAt: LocalDateTime,
    val cards: List<Card>
)

/**
 * Result of a backup operation.
 */
sealed class BackupResult {
    data class Success(val filePath: String, val cardsCount: Int) : BackupResult()
    data class Error(val message: String) : BackupResult()
}

/**
 * Result of a restore operation.
 */
sealed class RestoreResult {
    data class Success(val cardsImported: Int, val cardsSkipped: Int) : RestoreResult()
    data class Error(val message: String) : RestoreResult()
}
