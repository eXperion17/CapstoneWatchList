package com.example.capstonewatchlist.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.capstonewatchlist.model.WatchItem

@Dao
interface WatchListDao {

    //Separated into three methods for now, just to make sure the query works first.

    @Query("SELECT * FROM watchlist_table WHERE list_id = 0 ORDER BY  favorite ASC ")
    fun getWatchListInProgress(): LiveData<List<WatchItem>>

    @Query("SELECT * FROM watchlist_table WHERE list_id = 1 ORDER BY favorite ASC")
    //@Query("SELECT * FROM watchlist_table ORDER BY favorite ASC")
    fun getWatchListPlanned(): LiveData<List<WatchItem>>

    @Query("SELECT * FROM watchlist_table WHERE list_id = 2 ORDER BY favorite ASC")
    fun getWatchListCompleted(): LiveData<List<WatchItem>>

    @Query("SELECT * FROM watchlist_table")
    fun getWatchList(): LiveData<List<WatchItem>>

    @Delete
    suspend fun deleteItem(item:WatchItem)

    @Insert
    suspend fun insertItem(item:WatchItem)

}