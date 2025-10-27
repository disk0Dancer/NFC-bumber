package com.nfcbumber.domain.repository

import com.nfcbumber.domain.model.Card
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for card data operations.
 * Defines the contract for accessing and managing NFC card data.
 */
interface CardRepository {
    /**
     * Get all cards as a Flow for reactive updates.
     */
    fun getAllCards(): Flow<List<Card>>

    /**
     * Get a specific card by its ID.
     */
    suspend fun getCardById(id: Long): Card?

    /**
     * Insert a new card.
     * @return The ID of the inserted card.
     */
    suspend fun insertCard(card: Card): Long

    /**
     * Update an existing card.
     */
    suspend fun updateCard(card: Card)

    /**
     * Delete a card.
     */
    suspend fun deleteCard(card: Card)

    /**
     * Delete a card by its ID.
     */
    suspend fun deleteCardById(id: Long)

    /**
     * Check if a card with the given UID already exists.
     */
    suspend fun cardExistsByUid(uid: ByteArray): Boolean

    /**
     * Update the last used timestamp and increment usage count for a card.
     */
    suspend fun updateCardUsage(id: Long)
}
