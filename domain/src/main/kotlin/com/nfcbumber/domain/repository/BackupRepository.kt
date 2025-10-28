package com.nfcbumber.domain.repository

import com.nfcbumber.domain.model.BackupResult
import com.nfcbumber.domain.model.RestoreResult

/**
 * Repository interface for backup and restore operations.
 */
interface BackupRepository {
    /**
     * Export all cards to a backup file.
     */
    suspend fun exportCards(filePath: String): BackupResult

    /**
     * Import cards from a backup file.
     */
    suspend fun importCards(filePath: String): RestoreResult
}
