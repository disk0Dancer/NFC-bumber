package com.nfcbumber.domain.usecase

import com.nfcbumber.domain.model.Card
import com.nfcbumber.domain.model.CardType
import com.nfcbumber.domain.repository.CardRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class UpdateCardUsageUseCaseTest {

    private val cardRepository: CardRepository = mockk(relaxed = true)
    private val useCase = UpdateCardUsageUseCase(cardRepository)

    @Test
    fun `invoke should call updateCardUsage on repository`() = runTest {
        // Given
        val cardId = 123L

        // When
        useCase(cardId)

        // Then
        coVerify(exactly = 1) { cardRepository.updateCardUsage(cardId) }
    }

    @Test
    fun `invoke should handle multiple updates correctly`() = runTest {
        // Given
        val cardId1 = 1L
        val cardId2 = 2L

        // When
        useCase(cardId1)
        useCase(cardId2)
        useCase(cardId1)

        // Then
        coVerify(exactly = 2) { cardRepository.updateCardUsage(cardId1) }
        coVerify(exactly = 1) { cardRepository.updateCardUsage(cardId2) }
    }
}
