package com.nfcbumber.domain.usecase

import com.nfcbumber.domain.model.BackupResult
import com.nfcbumber.domain.repository.BackupRepository
import javax.inject.Inject

/**
 * Use case for exporting cards to a backup file.
 */
class ExportCardsUseCase @Inject constructor(
    private val backupRepository: BackupRepository
) {
    suspend operator fun invoke(filePath: String): BackupResult {
        return try {
            backupRepository.exportCards(filePath)
        } catch (e: Exception) {
            BackupResult.Error(e.message ?: "Failed to export cards")
        }
    }
}
