package com.nfcbumber.domain.model

/**
 * Card compatibility type for different systems
 */
enum class CardCompatibility {
    /**
     * Full emulation support (ISO-DEP cards with APDU)
     * Works with: bank cards, transport cards with APDU
     */
    FULL_HCE,

    /**
     * Limited support - only UID emulation
     * Works with: access control systems that use APDU + UID
     * May work with: some intercom systems (requires testing)
     */
    UID_ONLY,

    /**
     * Not supported via standard Android HCE
     * Requires: UID at anti-collision level (before SELECT)
     * Common in: intercoms, old access control
     * Alternative: Use physical card, NFC card emulator apps with root
     */
    NOT_SUPPORTED,

    /**
     * Unknown - requires testing
     */
    UNKNOWN
}

/**
 * Determines card compatibility with Android HCE
 */
fun Card.getCompatibility(): CardCompatibility {
    return when (cardType) {
        CardType.ISO_DEP -> {
            // ISO-DEP cards can use HCE if they rely on APDU
            if (ats != null && ats.isNotEmpty()) {
                CardCompatibility.FULL_HCE
            } else {
                CardCompatibility.UID_ONLY
            }
        }
        CardType.MIFARE_CLASSIC,
        CardType.MIFARE_ULTRALIGHT -> {
            // Mifare cards typically use low-level anti-collision UID
            // which Android HCE cannot emulate
            CardCompatibility.NOT_SUPPORTED
        }
        CardType.NFC_A,
        CardType.NFC_B,
        CardType.NFC_F,
        CardType.NFC_V -> {
            // Generic NFC - depends on implementation
            CardCompatibility.UNKNOWN
        }
        CardType.UNKNOWN -> CardCompatibility.UNKNOWN
    }
}

/**
 * Get user-friendly compatibility message
 */
fun CardCompatibility.getMessage(): String {
    return when (this) {
        CardCompatibility.FULL_HCE ->
            "✅ Полная поддержка эмуляции"
        CardCompatibility.UID_ONLY ->
            "⚠️ Ограниченная поддержка - только UID (требуется тестирование)"
        CardCompatibility.NOT_SUPPORTED ->
            "❌ Не поддерживается Android HCE (нужны root права или физическая карта)"
        CardCompatibility.UNKNOWN ->
            "❓ Неизвестно - требуется тестирование"
    }
}

/**
 * Get detailed compatibility description
 */
fun CardCompatibility.getDescription(): String {
    return when (this) {
        CardCompatibility.FULL_HCE ->
            "Эта карта использует ISO-DEP протокол и может быть полностью эмулирована через Android HCE. Работает с большинством современных систем."

        CardCompatibility.UID_ONLY ->
            "Карта может эмулировать только UID. Работает с системами доступа, которые используют APDU команды. Некоторые домофоны могут работать, но требуется тестирование на конкретном устройстве."

        CardCompatibility.NOT_SUPPORTED ->
            """
            Эта карта (${this}) использует низкоуровневый протокол, который Android HCE не может эмулировать.
            
            Почему не работает:
            • Домофоны читают UID на этапе антиколлизии (до SELECT)
            • Android HCE работает только с APDU командами (после SELECT)
            • UID в HCE генерируется системой и не может быть изменён
            
            Альтернативы:
            1. Использовать физическую карту
            2. Приложения с root правами (например, NFC Card Emulator Pro)
            3. Программируемые NFC метки/кольца
            4. Обратиться к администратору для добавления телефона отдельно
            """.trimIndent()

        CardCompatibility.UNKNOWN ->
            "Совместимость неизвестна. Попробуйте протестировать карту с вашей системой. Если работает - отлично! Если нет - скорее всего карта использует низкоуровневый протокол."
    }
}

