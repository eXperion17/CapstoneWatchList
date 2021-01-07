package com.example.capstonewatchlist.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.capstonewatchlist.dao.WatchListDao
import com.example.capstonewatchlist.database.WatchListDatabase
import com.example.capstonewatchlist.model.WatchItem

class WatchListRepository (context: Context) {
    private val watchListDao: WatchListDao
    init {
        val database = WatchListDatabase.getDatabase(context)
        watchListDao = database!!.watchListDao()
    }

    fun getWatchList(): LiveData<List<WatchItem>> {
        return watchListDao.getWatchList()
    }

    suspend fun insertItem(item:WatchItem) {
        watchListDao.insertItem(item)
    }

    suspend fun deleteItem(item: WatchItem) {
        watchListDao.deleteItem(item)
    }

    suspend fun updateItem(item: WatchItem) {
        watchListDao.updateItem(item)
    }
}