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
    private val deleteCardUseCase: DeleteCardUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CardListUiState>(CardListUiState.Loading)
    val uiState: StateFlow<CardListUiState> = _uiState.asStateFlow()

    private val _selectedCardId = MutableStateFlow<Long?>(null)
    val selectedCardId: StateFlow<Long?> = _selectedCardId.asStateFlow()

    init {
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
                        // Auto-select first card if none selected
                        if (_selectedCardId.value == null && cards.isNotEmpty()) {
                            _selectedCardId.value = cards.first().id
                        }
                        CardListUiState.Success(cards)
                    }
                }
        }
    }

    fun selectCard(cardId: Long) {
        _selectedCardId.value = cardId
    }

    fun deleteCard(cardId: Long) {
        viewModelScope.launch {
            deleteCardUseCase(cardId).fold(
                onSuccess = {
                    // If deleted card was selected, clear selection
                    if (_selectedCardId.value == cardId) {
                        _selectedCardId.value = null
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
