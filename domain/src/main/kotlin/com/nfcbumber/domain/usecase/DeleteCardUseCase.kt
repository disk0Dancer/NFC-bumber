package com.nfcbumber.domain.usecase

import com.nfcbumber.domain.repository.CardRepository
import javax.inject.Inject

/**
 * Use case for deleting a card from the repository.
 */
class DeleteCardUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(cardId: Long): Result<Unit> {
        return try {
            cardRepository.deleteCardById(cardId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
