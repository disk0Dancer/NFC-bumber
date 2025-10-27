package com.nfcbumber.data.mapper

import com.nfcbumber.data.database.CardEntity
import com.nfcbumber.domain.model.Card
import com.nfcbumber.domain.model.CardType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneId

class CardMapperTest {

    @Test
    fun `toDomain converts entity to domain model correctly`() {
        // Given
        val entity = CardEntity(
            id = 1L,
            name = "Test Card",
            uid = byteArrayOf(0x01, 0x02, 0x03, 0x04),
            ats = byteArrayOf(0x78, 0x77, 0x71, 0x02),
            historicalBytes = null,
            cardType = CardType.ISO_DEP.name,
            color = 0xFF0000,
            createdAt = System.currentTimeMillis(),
            lastUsedAt = null,
            usageCount = 0
        )

        // When
        val card = CardMapper.toDomain(entity)

        // Then
        assertEquals(entity.id, card.id)
        assertEquals(entity.name, card.name)
        assertArrayEquals(entity.uid, card.uid)
        assertArrayEquals(entity.ats, card.ats)
        assertEquals(CardType.ISO_DEP, card.cardType)
        assertEquals(entity.color, card.color)
        assertEquals(entity.usageCount, card.usageCount)
    }

    @Test
    fun `toEntity converts domain model to entity correctly`() {
        // Given
        val card = Card(
            id = 1L,
            name = "Test Card",
            uid = byteArrayOf(0x01, 0x02, 0x03, 0x04),
            ats = byteArrayOf(0x78, 0x77, 0x71, 0x02),
            historicalBytes = null,
            cardType = CardType.ISO_DEP,
            color = 0xFF0000,
            createdAt = LocalDateTime.now(),
            lastUsedAt = null,
            usageCount = 0
        )

        // When
        val entity = CardMapper.toEntity(card)

        // Then
        assertEquals(card.id, entity.id)
        assertEquals(card.name, entity.name)
        assertArrayEquals(card.uid, entity.uid)
        assertArrayEquals(card.ats, entity.ats)
        assertEquals(card.cardType.name, entity.cardType)
        assertEquals(card.color, entity.color)
        assertEquals(card.usageCount, entity.usageCount)
    }

    @Test
    fun `roundtrip conversion preserves data`() {
        // Given
        val originalCard = Card(
            id = 1L,
            name = "Test Card",
            uid = byteArrayOf(0x01, 0x02, 0x03, 0x04),
            ats = byteArrayOf(0x78, 0x77, 0x71, 0x02),
            historicalBytes = byteArrayOf(0x12, 0x34),
            cardType = CardType.MIFARE_CLASSIC,
            color = 0x00FF00,
            createdAt = LocalDateTime.now(),
            lastUsedAt = LocalDateTime.now(),
            usageCount = 5
        )

        // When
        val entity = CardMapper.toEntity(originalCard)
        val convertedCard = CardMapper.toDomain(entity)

        // Then
        assertEquals(originalCard.id, convertedCard.id)
        assertEquals(originalCard.name, convertedCard.name)
        assertArrayEquals(originalCard.uid, convertedCard.uid)
        assertArrayEquals(originalCard.ats, convertedCard.ats)
        assertArrayEquals(originalCard.historicalBytes, convertedCard.historicalBytes)
        assertEquals(originalCard.cardType, convertedCard.cardType)
        assertEquals(originalCard.color, convertedCard.color)
        assertEquals(originalCard.usageCount, convertedCard.usageCount)
    }
}
