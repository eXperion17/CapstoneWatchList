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


    //Separated into three methods for now, just to make sure the query works first.
    /*fun getWatchListInProgress(): LiveData<List<WatchItem>> {
        return watchListDao.getWatchListInProgress()
    }

    fun getWatchListPlanned(): LiveData<List<WatchItem>> {
        return watchListDao.getWatchListPlanned()
    }

    fun getWatchListCompleted(): LiveData<List<WatchItem>> {
        return watchListDao.getWatchListCompleted()
    }*/

    fun getWatchList(): LiveData<List<WatchItem>> {
        return watchListDao.getWatchList()
    }


    suspend fun insertItem(item:WatchItem) {
        watchListDao.insertItem(item)
    }

    suspend fun deleteItem(item: WatchItem) {
        watchListDao.deleteItem(item)
    }
}