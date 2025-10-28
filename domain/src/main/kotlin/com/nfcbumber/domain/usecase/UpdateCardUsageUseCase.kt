package com.nfcbumber.domain.usecase

import javax.inject.Inject

/**
 * Use case for updating the card usage statistics.
 * Called when a card is used for emulation.
 */
class UpdateCardUsageUseCase @Inject constructor(
    private val cardRepository: com.nfcbumber.domain.repository.CardRepository
) {
    /**
     * Update the last used timestamp and increment usage count.
     * @param cardId The ID of the card that was used.
     */
    suspend operator fun invoke(cardId: Long) {
        cardRepository.updateCardUsage(cardId)
    }
}
