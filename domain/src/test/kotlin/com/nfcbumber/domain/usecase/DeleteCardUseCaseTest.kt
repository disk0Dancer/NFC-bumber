package com.nfcbumber.domain.usecase

import com.nfcbumber.domain.repository.CardRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DeleteCardUseCaseTest {

    private val cardRepository: CardRepository = mockk(relaxed = true)
    private val useCase = DeleteCardUseCase(cardRepository)

    @Test
    fun `invoke deletes card successfully`() = runTest {
        // Given
        val cardId = 123L
        coEvery { cardRepository.deleteCardById(cardId) } just runs

        // When
        val result = useCase(cardId)

        // Then
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { cardRepository.deleteCardById(cardId) }
    }

    @Test
    fun `invoke handles repository exceptions`() = runTest {
        // Given
        val cardId = 123L
        coEvery { cardRepository.deleteCardById(cardId) } throws RuntimeException("Delete failed")

        // When
        val result = useCase(cardId)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
        assertEquals("Delete failed", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke can delete multiple cards`() = runTest {
        // Given
        val cardId1 = 1L
        val cardId2 = 2L
        coEvery { cardRepository.deleteCardById(any()) } just runs

        // When
        val result1 = useCase(cardId1)
        val result2 = useCase(cardId2)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        coVerify(exactly = 1) { cardRepository.deleteCardById(cardId1) }
        coVerify(exactly = 1) { cardRepository.deleteCardById(cardId2) }
    }
}
