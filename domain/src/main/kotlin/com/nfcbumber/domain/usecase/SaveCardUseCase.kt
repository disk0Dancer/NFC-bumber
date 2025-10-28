package com.nfcbumber.domain.usecase

import com.nfcbumber.domain.model.Card
import com.nfcbumber.domain.repository.CardRepository
import javax.inject.Inject

/**
 * Use case for saving a new card to the repository.
 * Handles validation and duplicate checking.
 */
class SaveCardUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {
    /**
     * Save a new card.
     * @param card The card to save
     * @return Result containing the card ID if successful, or an error
     */
    suspend operator fun invoke(card: Card): Result<Long> {
        return try {
            // Check if card with same UID already exists
            if (cardRepository.cardExistsByUid(card.uid)) {
                return Result.failure(CardAlreadyExistsException("Card with this UID already exists"))
            }
            
            // Validate card data
            if (card.name.isBlank()) {
                return Result.failure(InvalidCardDataException("Card name cannot be empty"))
            }
            
            if (card.uid.isEmpty()) {
                return Result.failure(InvalidCardDataException("Card UID cannot be empty"))
            }
            
            // Insert the card
            val id = cardRepository.insertCard(card)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Exception thrown when a card with the same UID already exists.
 */
class CardAlreadyExistsException(message: String) : Exception(message)

/**
 * Exception thrown when card data is invalid.
 */
class InvalidCardDataException(message: String) : Exception(message)
