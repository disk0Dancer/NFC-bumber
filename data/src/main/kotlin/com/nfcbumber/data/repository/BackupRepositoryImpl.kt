package com.nfcbumber.data.repository

import com.nfcbumber.data.database.CardDao
import com.nfcbumber.data.mapper.CardMapper
import com.nfcbumber.domain.model.BackupResult
import com.nfcbumber.domain.model.Card
import com.nfcbumber.domain.model.CardBackup
import com.nfcbumber.domain.model.RestoreResult
import com.nfcbumber.domain.repository.BackupRepository
import kotlinx.coroutines.flow.first
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Base64
import javax.inject.Inject

/**
 * Implementation of BackupRepository using JSON file format.
 */
class BackupRepositoryImpl @Inject constructor(
    private val cardDao: CardDao
) : BackupRepository {

    companion object {
        private const val VERSION = 1
        private const val KEY_VERSION = "version"
        private const val KEY_EXPORTED_AT = "exportedAt"
        private const val KEY_CARDS = "cards"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_UID = "uid"
        private const val KEY_ATS = "ats"
        private const val KEY_HISTORICAL_BYTES = "historicalBytes"
        private const val KEY_CARD_TYPE = "cardType"
        private const val KEY_COLOR = "color"
        private const val KEY_CREATED_AT = "createdAt"
        private const val KEY_LAST_USED_AT = "lastUsedAt"
        private const val KEY_USAGE_COUNT = "usageCount"
    }

    override suspend fun exportCards(filePath: String): BackupResult {
        return try {
            val cards = cardDao.getAllCards().first().map { CardMapper.toDomain(it) }
            
            if (cards.isEmpty()) {
                return BackupResult.Error("No cards to export")
            }

            val json = JSONObject().apply {
                put(KEY_VERSION, VERSION)
                put(KEY_EXPORTED_AT, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                put(KEY_CARDS, JSONArray().apply {
                    cards.forEach { card ->
                        put(cardToJson(card))
                    }
                })
            }

            val file = File(filePath)
            file.writeText(json.toString(2))

            BackupResult.Success(filePath, cards.size)
        } catch (e: Exception) {
            BackupResult.Error("Failed to export cards: ${e.message}")
        }
    }

    override suspend fun importCards(filePath: String): RestoreResult {
        return try {
            val file = File(filePath)
            if (!file.exists()) {
                return RestoreResult.Error("Backup file not found")
            }

            val json = JSONObject(file.readText())
            val version = json.optInt(KEY_VERSION, 1)
            
            if (version > VERSION) {
                return RestoreResult.Error("Backup file version is not supported")
            }

            val cardsArray = json.getJSONArray(KEY_CARDS)
            var imported = 0
            var skipped = 0

            for (i in 0 until cardsArray.length()) {
                val cardJson = cardsArray.getJSONObject(i)
                val card = jsonToCard(cardJson)
                
                // Check if card already exists by UID
                val exists = cardDao.countCardsByUid(card.uid) > 0
                if (exists) {
                    skipped++
                } else {
                    cardDao.insertCard(CardMapper.toEntity(card))
                    imported++
                }
            }

            RestoreResult.Success(imported, skipped)
        } catch (e: Exception) {
            RestoreResult.Error("Failed to import cards: ${e.message}")
        }
    }

    private fun cardToJson(card: Card): JSONObject {
        return JSONObject().apply {
            put(KEY_ID, card.id)
            put(KEY_NAME, card.name)
            put(KEY_UID, Base64.getEncoder().encodeToString(card.uid))
            card.ats?.let { put(KEY_ATS, Base64.getEncoder().encodeToString(it)) }
            card.historicalBytes?.let { put(KEY_HISTORICAL_BYTES, Base64.getEncoder().encodeToString(it)) }
            put(KEY_CARD_TYPE, card.cardType.name)
            put(KEY_COLOR, card.color)
            put(KEY_CREATED_AT, card.createdAt.format(DateTimeFormatter.ISO_DATE_TIME))
            card.lastUsedAt?.let { put(KEY_LAST_USED_AT, it.format(DateTimeFormatter.ISO_DATE_TIME)) }
            put(KEY_USAGE_COUNT, card.usageCount)
        }
    }

    private fun jsonToCard(json: JSONObject): Card {
        return Card(
            id = 0, // Reset ID to let database assign new one
            name = json.getString(KEY_NAME),
            uid = Base64.getDecoder().decode(json.getString(KEY_UID)),
            ats = if (json.has(KEY_ATS)) Base64.getDecoder().decode(json.getString(KEY_ATS)) else null,
            historicalBytes = if (json.has(KEY_HISTORICAL_BYTES)) Base64.getDecoder().decode(json.getString(KEY_HISTORICAL_BYTES)) else null,
            cardType = com.nfcbumber.domain.model.CardType.valueOf(json.getString(KEY_CARD_TYPE)),
            color = json.getInt(KEY_COLOR),
            createdAt = LocalDateTime.parse(json.getString(KEY_CREATED_AT), DateTimeFormatter.ISO_DATE_TIME),
            lastUsedAt = if (json.has(KEY_LAST_USED_AT)) LocalDateTime.parse(json.getString(KEY_LAST_USED_AT), DateTimeFormatter.ISO_DATE_TIME) else null,
            usageCount = json.optInt(KEY_USAGE_COUNT, 0)
        )
    }
}
