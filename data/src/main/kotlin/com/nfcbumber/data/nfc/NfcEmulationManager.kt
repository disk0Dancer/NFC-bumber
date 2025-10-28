package com.nfcbumber.data.nfc

import android.content.Context
import com.nfcbumber.data.security.SecureStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages NFC emulation state.
 * Controls when the NfcEmulatorService should respond to terminals.
 */
@Singleton
class NfcEmulationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val secureStorage: SecureStorage
) {
    companion object {
        private const val KEY_SELECTED_CARD_ID = "selected_card_id"
        private const val KEY_EMULATION_ACTIVE = "nfc_emulation_active"
        private const val NO_CARD_SELECTED = -1L
    }

    /**
     * Activate NFC emulation for a specific card.
     * Called when CardEmulationActivity is opened or app is in foreground.
     */
    fun activateEmulation(cardId: Long) {
        secureStorage.putLong(KEY_SELECTED_CARD_ID, cardId)
        secureStorage.putBoolean(KEY_EMULATION_ACTIVE, true)
    }

    /**
     * Deactivate NFC emulation.
     * Called when CardEmulationActivity is closed and app is in background.
     */
    fun deactivateEmulation() {
        secureStorage.putBoolean(KEY_EMULATION_ACTIVE, false)
        secureStorage.putLong(KEY_SELECTED_CARD_ID, NO_CARD_SELECTED)
    }

    /**
     * Check if NFC emulation is currently active.
     */
    fun isEmulationActive(): Boolean {
        return secureStorage.getBoolean(KEY_EMULATION_ACTIVE, false)
    }

    /**
     * Get the currently selected card ID for emulation.
     * Returns -1 if no card is selected.
     */
    fun getSelectedCardId(): Long {
        return if (isEmulationActive()) {
            secureStorage.getLong(KEY_SELECTED_CARD_ID, NO_CARD_SELECTED)
        } else {
            NO_CARD_SELECTED
        }
    }

    /**
     * Set selected card without changing active state.
     * Useful for pre-selecting a card before activation.
     */
    fun setSelectedCard(cardId: Long) {
        secureStorage.putLong(KEY_SELECTED_CARD_ID, cardId)
    }
}

