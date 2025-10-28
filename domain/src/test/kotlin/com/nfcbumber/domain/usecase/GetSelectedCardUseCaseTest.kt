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

class GetSelectedCardUseCaseTest {

    private val cardRepository: CardRepository = mockk()
    private val useCase = GetSelectedCardUseCase(cardRepository)

    private val testCard1 = Card(
        id = 1L,
        name = "Test Card 1",
        uid = byteArrayOf(0x01, 0x02, 0x03, 0x04),
        ats = null,
        historicalBytes = null,
        cardType = CardType.ISO_DEP,
        color = 0xFF0000,
        createdAt = LocalDateTime.now(),
        lastUsedAt = null,
        usageCount = 0
    )

    private val testCard2 = Card(
        id = 2L,
        name = "Test Card 2",
        uid = byteArrayOf(0x05, 0x06, 0x07, 0x08),
        ats = null,
        historicalBytes = null,
        cardType = CardType.MIFARE_CLASSIC,
        color = 0x00FF00,
        createdAt = LocalDateTime.now(),
        lastUsedAt = null,
        usageCount = 0
    )

    @Test
    fun `invoke returns null when selectedCardId is null`() = runTest {
        // Given
        val selectedCardId: Long? = null
        every { cardRepository.getAllCards() } returns flowOf(listOf(testCard1, testCard2))

        // When
        val result = useCase(selectedCardId).first()

        // Then
        assertNull(result)
    }

    @Test
    fun `invoke returns null when selectedCardId is -1`() = runTest {
        // Given
        val selectedCardId = -1L
        every { cardRepository.getAllCards() } returns flowOf(listOf(testCard1, testCard2))

        // When
        val result = useCase(selectedCardId).first()

        // Then
        assertNull(result)
    }

    @Test
    fun `invoke returns correct card when selectedCardId matches`() = runTest {
        // Given
        val selectedCardId = 2L
        every { cardRepository.getAllCards() } returns flowOf(listOf(testCard1, testCard2))

        // When
        val result = useCase(selectedCardId).first()

        // Then
        assertNotNull(result)
        assertEquals(testCard2.id, result?.id)
        assertEquals(testCard2.name, result?.name)
    }

    @Test
    fun `invoke returns null when selectedCardId does not exist`() = runTest {
        // Given
        val selectedCardId = 999L
        every { cardRepository.getAllCards() } returns flowOf(listOf(testCard1, testCard2))

        // When
        val result = useCase(selectedCardId).first()

        // Then
        assertNull(result)
    }

    @Test
    fun `invoke returns null when card list is empty`() = runTest {
        // Given
        val selectedCardId = 1L
        every { cardRepository.getAllCards() } returns flowOf(emptyList())

        // When
        val result = useCase(selectedCardId).first()

        // Then
        assertNull(result)
    }
}
