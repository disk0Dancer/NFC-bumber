package com.nfcbumber.presentation.scan

import android.nfc.Tag
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nfcbumber.data.nfc.NfcCardData
import com.nfcbumber.data.nfc.NfcReaderService
import com.nfcbumber.domain.model.Card
import com.nfcbumber.domain.model.CardType
import com.nfcbumber.domain.usecase.SaveCardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.random.Random

/**
 * ViewModel for the card scanning screen.
 * Manages NFC card scanning and saving state.
 */
@HiltViewModel
class ScanCardViewModel @Inject constructor(
    private val nfcReaderService: NfcReaderService,
    private val saveCardUseCase: SaveCardUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ScanCardUiState>(ScanCardUiState.Idle)
    val uiState: StateFlow<ScanCardUiState> = _uiState.asStateFlow()

    /**
     * Handle NFC tag detection.
     * Reads the card data and updates the UI state.
     */
    fun onTagDetected(tag: Tag) {
        viewModelScope.launch {
            _uiState.update { ScanCardUiState.Scanning }
            
            try {
                val cardData = nfcReaderService.readCard(tag)
                _uiState.update { 
                    ScanCardUiState.Scanned(
                        cardData = cardData,
                        suggestedName = generateCardName(cardData.cardType)
                    ) 
                }
            } catch (e: Exception) {
                _uiState.update { 
                    ScanCardUiState.Error("Failed to read card: ${e.message}") 
                }
            }
        }
    }

    /**
     * Save the scanned card with the given name.
     */
    fun saveCard(cardData: NfcCardData, name: String, color: Int) {
        viewModelScope.launch {
            _uiState.update { ScanCardUiState.Saving }
            
            val card = Card(
                name = name.trim(),
                uid = cardData.uid,
                ats = cardData.ats,
                historicalBytes = cardData.historicalBytes,
                aids = cardData.aids,
                cardType = cardData.cardType,
                color = color,
                createdAt = LocalDateTime.now(),
                lastUsedAt = null,
                usageCount = 0
            )
            
            val result = saveCardUseCase(card)
            
            result.fold(
                onSuccess = { id ->
                    _uiState.update { ScanCardUiState.Saved(id) }
                },
                onFailure = { error ->
                    _uiState.update { 
                        ScanCardUiState.Error("Failed to save card: ${error.message}") 
                    }
                }
            )
        }
    }

    /**
     * Reset the UI state to idle.
     */
    fun resetState() {
        _uiState.update { ScanCardUiState.Idle }
    }

    private fun generateCardName(cardType: CardType): String {
        val prefix = when (cardType) {
            CardType.MIFARE_CLASSIC -> "MIFARE Classic"
            CardType.MIFARE_ULTRALIGHT -> "MIFARE Ultralight"
            CardType.ISO_DEP -> "ISO-DEP Card"
            CardType.NFC_A -> "NFC-A Card"
            CardType.NFC_B -> "NFC-B Card"
            CardType.NFC_F -> "NFC-F Card"
            CardType.NFC_V -> "NFC-V Card"
            CardType.UNKNOWN -> "NFC Card"
        }
        return "$prefix ${Random.nextInt(1000, 9999)}"
    }
}

/**
 * UI state for the scan card screen.
 */
sealed interface ScanCardUiState {
    /**
     * Initial idle state, waiting for user to scan a card.
     */
    object Idle : ScanCardUiState

    /**
     * Scanning a card in progress.
     */
    object Scanning : ScanCardUiState

    /**
     * Card successfully scanned, ready to be saved.
     */
    data class Scanned(
        val cardData: NfcCardData,
        val suggestedName: String
    ) : ScanCardUiState

    /**
     * Saving card to database.
     */
    object Saving : ScanCardUiState

    /**
     * Card successfully saved.
     */
    data class Saved(val cardId: Long) : ScanCardUiState

    /**
     * Error occurred during scanning or saving.
     */
    data class Error(val message: String) : ScanCardUiState
}
