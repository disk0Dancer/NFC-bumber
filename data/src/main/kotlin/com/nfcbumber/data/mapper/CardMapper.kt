package com.nfcbumber.data.mapper

import com.nfcbumber.data.database.CardEntity
import com.nfcbumber.domain.model.Card
import com.nfcbumber.domain.model.CardType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Mapper between domain Card model and data CardEntity.
 */
object CardMapper {
    fun toDomain(entity: CardEntity): Card {
        return Card(
            id = entity.id,
            name = entity.name,
            uid = entity.uid,
            ats = entity.ats,
            historicalBytes = entity.historicalBytes,
            cardType = CardType.valueOf(entity.cardType),
            color = entity.color,
            createdAt = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(entity.createdAt),
                ZoneId.systemDefault()
            ),
            lastUsedAt = entity.lastUsedAt?.let {
                LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(it),
                    ZoneId.systemDefault()
                )
            },
            usageCount = entity.usageCount
        )
    }

    fun toEntity(card: Card): CardEntity {
        return CardEntity(
            id = card.id,
            name = card.name,
            uid = card.uid,
            ats = card.ats,
            historicalBytes = card.historicalBytes,
            cardType = card.cardType.name,
            color = card.color,
            createdAt = card.createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            lastUsedAt = card.lastUsedAt?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli(),
            usageCount = card.usageCount
        )
    }
}
