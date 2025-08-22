package com.example.chand.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.retrofit_exersice.utils.Constants

@Database(entities = [WatchlistItemEntity::class], version = 1) // نسخه را به ۲ افزایش دهید
abstract class ChandDatabase: RoomDatabase(){
    abstract fun dao() : WatchlistDao

    companion object {
        @Volatile
        private var INSTANCE: ChandDatabase? = null

        fun getDatabase(context: Context): ChandDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChandDatabase::class.java,
                    Constants.DB_NAME
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}