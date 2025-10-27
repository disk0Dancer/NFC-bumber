package com.nfcbumber.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for card operations.
 */
@Dao
interface CardDao {
    /**
     * Get all cards as a Flow for reactive updates.
     */
    @Query("SELECT * FROM cards ORDER BY lastUsedAt DESC, createdAt DESC")
    fun getAllCards(): Flow<List<CardEntity>>

    /**
     * Get a specific card by its ID.
     */
    @Query("SELECT * FROM cards WHERE id = :id")
    suspend fun getCardById(id: Long): CardEntity?

    /**
     * Insert a new card.
     * @return The ID of the inserted card.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: CardEntity): Long

    /**
     * Update an existing card.
     */
    @Update
    suspend fun updateCard(card: CardEntity)

    /**
     * Delete a card.
     */
    @Delete
    suspend fun deleteCard(card: CardEntity)

    /**
     * Delete a card by its ID.
     */
    @Query("DELETE FROM cards WHERE id = :id")
    suspend fun deleteCardById(id: Long)

    /**
     * Check if a card with the given UID already exists.
     */
    @Query("SELECT COUNT(*) FROM cards WHERE uid = :uid")
    suspend fun countCardsByUid(uid: ByteArray): Int

    /**
     * Update the last used timestamp and increment usage count for a card.
     */
    @Query("UPDATE cards SET lastUsedAt = :timestamp, usageCount = usageCount + 1 WHERE id = :id")
    suspend fun updateCardUsage(id: Long, timestamp: Long)
}
