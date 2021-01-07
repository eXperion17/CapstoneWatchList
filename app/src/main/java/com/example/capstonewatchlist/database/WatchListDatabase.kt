package com.example.capstonewatchlist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.capstonewatchlist.dao.WatchListDao
import com.example.capstonewatchlist.model.WatchItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [WatchItem::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WatchListDatabase : RoomDatabase() {
    abstract fun watchListDao(): WatchListDao

    companion object {
        private const val DATABASE_NAME = "WATCHLIST_DATABASE"

        @Volatile
        private var INSTANCE: WatchListDatabase? = null

        fun getDatabase(context: Context): WatchListDatabase? {
            if (INSTANCE == null) {
                synchronized(WatchListDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder( context.applicationContext, WatchListDatabase::class.java, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(object : RoomDatabase.Callback() {})
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}