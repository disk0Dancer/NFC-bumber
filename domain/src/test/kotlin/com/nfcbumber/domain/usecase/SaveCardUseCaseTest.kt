package com.nfcbumber.domain.usecase

import com.nfcbumber.domain.model.Card
import com.nfcbumber.domain.model.CardType
import com.nfcbumber.domain.repository.CardRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class SaveCardUseCaseTest {

    private val cardRepository: CardRepository = mockk(relaxed = true)
    private val useCase = SaveCardUseCase(cardRepository)

    private val validCard = Card(
        id = 0L,
        name = "Test Card",
        uid = byteArrayOf(0x01, 0x02, 0x03, 0x04),
        ats = null,
        historicalBytes = null,
        aids = emptyList(),
        cardType = CardType.ISO_DEP,
        color = 0xFF0000,
        createdAt = LocalDateTime.now(),
        lastUsedAt = null,
        usageCount = 0
    )

    @Test
    fun `invoke saves valid card successfully`() = runTest {
        // Given
        coEvery { cardRepository.cardExistsByUid(any()) } returns false
        coEvery { cardRepository.insertCard(any()) } returns 123L

        // When
        val result = useCase(validCard)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(123L, result.getOrNull())
        coVerify(exactly = 1) { cardRepository.insertCard(validCard) }
    }

    @Test
    fun `invoke fails when card with same UID exists`() = runTest {
        // Given
        coEvery { cardRepository.cardExistsByUid(validCard.uid) } returns true

        // When
        val result = useCase(validCard)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is CardAlreadyExistsException)
        coVerify(exactly = 0) { cardRepository.insertCard(any()) }
    }

    @Test
    fun `invoke fails when card name is blank`() = runTest {
        // Given
        val cardWithBlankName = validCard.copy(name = "   ")
        coEvery { cardRepository.cardExistsByUid(any()) } returns false

        // When
        val result = useCase(cardWithBlankName)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is InvalidCardDataException)
        assertEquals("Card name cannot be empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke fails when card name is empty`() = runTest {
        // Given
        val cardWithEmptyName = validCard.copy(name = "")
        coEvery { cardRepository.cardExistsByUid(any()) } returns false

        // When
        val result = useCase(cardWithEmptyName)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is InvalidCardDataException)
    }

    @Test
    fun `invoke fails when UID is empty`() = runTest {
        // Given
        val cardWithEmptyUid = validCard.copy(uid = byteArrayOf())
        coEvery { cardRepository.cardExistsByUid(any()) } returns false

        // When
        val result = useCase(cardWithEmptyUid)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is InvalidCardDataException)
        assertEquals("Card UID cannot be empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke handles repository exceptions`() = runTest {
        // Given
        coEvery { cardRepository.cardExistsByUid(any()) } returns false
        coEvery { cardRepository.insertCard(any()) } throws RuntimeException("Database error")

        // When
        val result = useCase(validCard)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
        assertEquals("Database error", result.exceptionOrNull()?.message)
    }
}
