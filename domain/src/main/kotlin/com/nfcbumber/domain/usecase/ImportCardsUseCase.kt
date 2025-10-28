package com.nfcbumber.domain.usecase

import com.nfcbumber.domain.model.RestoreResult
import com.nfcbumber.domain.repository.BackupRepository
import javax.inject.Inject

/**
 * Use case for importing cards from a backup file.
 */
class ImportCardsUseCase @Inject constructor(
    private val backupRepository: BackupRepository
) {
    suspend operator fun invoke(filePath: String): RestoreResult {
        return try {
            backupRepository.importCards(filePath)
        } catch (e: Exception) {
            RestoreResult.Error(e.message ?: "Failed to import cards")
        }
    }
}
