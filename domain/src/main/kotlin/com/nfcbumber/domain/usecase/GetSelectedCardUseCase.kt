package com.nfcbumber.domain.usecase

import com.nfcbumber.domain.model.Card
import com.nfcbumber.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use case for getting the currently selected card for emulation.
 * Returns a Flow that emits the selected card whenever it changes.
 */
class GetSelectedCardUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {
    /**
     * Get the selected card as a Flow.
     * @param selectedCardId The ID of the currently selected card.
     * @return Flow of the selected Card, or null if no card is selected.
     */
    operator fun invoke(selectedCardId: Long?): Flow<Card?> {
        return if (selectedCardId != null && selectedCardId != -1L) {
            cardRepository.getAllCards().map { cards ->
                cards.firstOrNull { it.id == selectedCardId }
            }
        } else {
            kotlinx.coroutines.flow.flowOf(null)
        }
    }
}
