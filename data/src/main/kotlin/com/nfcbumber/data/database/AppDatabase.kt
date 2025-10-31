package com.nfcbumber.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Main Room database for the NFC Bumber application.
 */
@Database(
    entities = [CardEntity::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao

    companion object {
        const val DATABASE_NAME = "nfc_bumber.db"
    }
}
