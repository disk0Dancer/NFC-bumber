package com.nfcbumber.data.repository

import com.nfcbumber.data.database.CardDao
import com.nfcbumber.data.mapper.CardMapper
import com.nfcbumber.domain.model.Card
import com.nfcbumber.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of CardRepository using Room database.
 */
class CardRepositoryImpl @Inject constructor(
    private val cardDao: CardDao
) : CardRepository {

    override fun getAllCards(): Flow<List<Card>> {
        return cardDao.getAllCards().map { entities ->
            entities.map { CardMapper.toDomain(it) }
        }
    }

    override suspend fun getCardById(id: Long): Card? {
        return cardDao.getCardById(id)?.let { CardMapper.toDomain(it) }
    }

    override suspend fun insertCard(card: Card): Long {
        return cardDao.insertCard(CardMapper.toEntity(card))
    }

    override suspend fun updateCard(card: Card) {
        cardDao.updateCard(CardMapper.toEntity(card))
    }

    override suspend fun deleteCard(card: Card) {
        cardDao.deleteCard(CardMapper.toEntity(card))
    }

    override suspend fun deleteCardById(id: Long) {
        cardDao.deleteCardById(id)
    }

    override suspend fun cardExistsByUid(uid: ByteArray): Boolean {
        return cardDao.countCardsByUid(uid) > 0
    }

    override suspend fun updateCardUsage(id: Long) {
        val timestamp = System.currentTimeMillis()
        cardDao.updateCardUsage(id, timestamp)
    }
}
