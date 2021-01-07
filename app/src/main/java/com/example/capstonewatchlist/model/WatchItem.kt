package com.example.capstonewatchlist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "watchlist_table")
data class WatchItem (
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "overview")
    var overview: String,
    @ColumnInfo(name = "poster_path")
    var poster: String,
    @ColumnInfo(name = "backdrop_path")
    var backdrop: String,
    @ColumnInfo(name = "genres")
    var genres: String,
    @ColumnInfo(name = "release_date")
    var release_date: Date,

    @ColumnInfo(name = "list_id")
    var listId: Int,
    @ColumnInfo(name = "is_movie")
    var isMovie: Boolean,
    @ColumnInfo(name = "favorite")
    var favorite: Boolean,
    //TODO: Add the progress of episodes

    @ColumnInfo(name = "total_episodes")
    var totalEpisodes: Int,
    @ColumnInfo(name = "episodes_watched")
    var episodesWatched: Int,


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id:Long? = null
)