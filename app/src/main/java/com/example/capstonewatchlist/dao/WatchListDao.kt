package com.example.capstonewatchlist.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.capstonewatchlist.model.WatchItem

@Dao
interface WatchListDao {

    @Query("SELECT * FROM watchlist_table")
    fun getWatchList(): LiveData<List<WatchItem>>

    @Delete
    suspend fun deleteItem(item:WatchItem)

    @Insert
    suspend fun insertItem(item:WatchItem)

    @Update
    suspend fun updateItem(item:WatchItem)

}