package com.example.chand.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.chand.DataBase.watchlist.WatchlistItemEntity
import com.example.retrofit_exersice.utils.Constants

@Database(entities = [WatchlistItemEntity::class, ConverterPriceEntity::class, AlertEntity::class], version = 3)
abstract class ChandDatabase : RoomDatabase() {
    abstract fun dao(): CurrencyDao

    companion object {
        @Volatile
        private var INSTANCE: ChandDatabase? = null

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE alerts (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        symbol TEXT NOT NULL,
                        upperLimit REAL NOT NULL,
                        lowerLimit REAL NOT NULL,
                        isActive INTEGER NOT NULL DEFAULT 1
                    )
                """)
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                CREATE TABLE converter_prices (
                    symbol TEXT PRIMARY KEY NOT NULL,
                    name TEXT,
                    nameEn TEXT,
                    price TEXT,
                    changePercent REAL,
                    unit TEXT,
                    date TEXT,
                    time TEXT,
                    type TEXT NOT NULL
                )
            """)
            }
        }

        fun getDatabase(context: Context): ChandDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChandDatabase::class.java,
                    Constants.DB_NAME
                )
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_1_2,MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}