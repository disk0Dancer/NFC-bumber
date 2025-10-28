package com.nfcbumber.presentation.cardlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nfcbumber.domain.model.Card
import com.nfcbumber.domain.usecase.DeleteCardUseCase
import com.nfcbumber.domain.usecase.GetAllCardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the card list screen.
 * Manages the list of saved cards and user interactions.
 */
@HiltViewModel
class CardListViewModel @Inject constructor(
    private val getAllCardsUseCase: GetAllCardsUseCase,
    private val deleteCardUseCase: DeleteCardUseCase,
    private val secureStorage: com.nfcbumber.data.security.SecureStorage
) : ViewModel() {

    companion object {
        private const val KEY_SELECTED_CARD_ID = "selected_card_id"
    }

    private val _uiState = MutableStateFlow<CardListUiState>(CardListUiState.Loading)
    val uiState: StateFlow<CardListUiState> = _uiState.asStateFlow()

    private val _selectedCardId = MutableStateFlow<Long?>(null)
    val selectedCardId: StateFlow<Long?> = _selectedCardId.asStateFlow()

    init {
        // Load previously selected card ID
        val savedCardId = secureStorage.getString(KEY_SELECTED_CARD_ID)?.toLongOrNull()
        if (savedCardId != null) {
            _selectedCardId.value = savedCardId
        }
        loadCards()
    }

    private fun loadCards() {
        viewModelScope.launch {
            getAllCardsUseCase()
                .catch { error ->
                    _uiState.value = CardListUiState.Error(
                        error.message ?: "Failed to load cards"
                    )
                }
                .collect { cards ->
                    _uiState.value = if (cards.isEmpty()) {
                        CardListUiState.Empty
                    } else {
                        // Auto-select first card if none selected or selected card doesn't exist
                        val currentSelectedId = _selectedCardId.value
                        val cardExists = cards.any { it.id == currentSelectedId }
                        
                        if (currentSelectedId == null || !cardExists) {
                            val firstCardId = cards.first().id
                            _selectedCardId.value = firstCardId
                            secureStorage.putString(KEY_SELECTED_CARD_ID, firstCardId.toString())
                        }
                        CardListUiState.Success(cards)
                    }
                }
        }
    }

    fun selectCard(cardId: Long) {
        _selectedCardId.value = cardId
        // Save to secure storage for HCE service
        secureStorage.putString(KEY_SELECTED_CARD_ID, cardId.toString())
    }

    fun deleteCard(cardId: Long) {
        viewModelScope.launch {
            deleteCardUseCase(cardId).fold(
                onSuccess = {
                    // If deleted card was selected, clear selection
                    if (_selectedCardId.value == cardId) {
                        _selectedCardId.value = null
                        secureStorage.remove(KEY_SELECTED_CARD_ID)
                    }
                },
                onFailure = { error ->
                    // Handle error - could show a snackbar
                    _uiState.value = CardListUiState.Error(
                        error.message ?: "Failed to delete card"
                    )
                }
            )
        }
    }

    fun refresh() {
        loadCards()
    }
}

/**
 * UI state for the card list screen.
 */
sealed interface CardListUiState {
    /**
     * Loading cards from database.
     */
    object Loading : CardListUiState

    /**
     * Successfully loaded cards.
     */
    data class Success(val cards: List<Card>) : CardListUiState

    /**
     * No cards in database.
     */
    object Empty : CardListUiState

    /**
     * Error loading cards.
     */
    data class Error(val message: String) : CardListUiState
}
