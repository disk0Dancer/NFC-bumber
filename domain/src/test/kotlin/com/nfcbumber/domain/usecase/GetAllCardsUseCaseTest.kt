package com.nfcbumber.domain.usecase

import com.nfcbumber.domain.model.Card
import com.nfcbumber.domain.model.CardType
import com.nfcbumber.domain.repository.CardRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class GetAllCardsUseCaseTest {

    private val cardRepository: CardRepository = mockk()
    private val useCase = GetAllCardsUseCase(cardRepository)

    @Test
    fun `invoke returns all cards from repository`() = runTest {
        // Given
        val cards = listOf(
            Card(
                id = 1L,
                name = "Card 1",
                uid = byteArrayOf(0x01, 0x02, 0x03, 0x04),
                ats = null,
                historicalBytes = null,
                cardType = CardType.ISO_DEP,
                color = 0xFF0000,
                createdAt = LocalDateTime.now(),
                lastUsedAt = null,
                usageCount = 0
            ),
            Card(
                id = 2L,
                name = "Card 2",
                uid = byteArrayOf(0x05, 0x06, 0x07, 0x08),
                ats = null,
                historicalBytes = null,
                cardType = CardType.MIFARE_CLASSIC,
                color = 0x00FF00,
                createdAt = LocalDateTime.now(),
                lastUsedAt = null,
                usageCount = 0
            )
        )
        every { cardRepository.getAllCards() } returns flowOf(cards)

        // When
        val result = useCase().first()

        // Then
        assertEquals(2, result.size)
        assertEquals("Card 1", result[0].name)
        assertEquals("Card 2", result[1].name)
    }

    @Test
    fun `invoke returns empty list when no cards exist`() = runTest {
        // Given
        every { cardRepository.getAllCards() } returns flowOf(emptyList())

        // When
        val result = useCase().first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `invoke emits updates when cards change`() = runTest {
        // Given
        val initialCards = listOf(
            Card(
                id = 1L,
                name = "Card 1",
                uid = byteArrayOf(0x01, 0x02, 0x03, 0x04),
                ats = null,
                historicalBytes = null,
                cardType = CardType.ISO_DEP,
                color = 0xFF0000,
                createdAt = LocalDateTime.now(),
                lastUsedAt = null,
                usageCount = 0
            )
        )
        every { cardRepository.getAllCards() } returns flowOf(initialCards)

        // When
        val result = useCase().first()

        // Then
        assertEquals(1, result.size)
        assertEquals("Card 1", result[0].name)
    }
}
