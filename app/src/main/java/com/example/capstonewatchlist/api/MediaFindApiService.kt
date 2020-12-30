package com.example.capstonewatchlist.api

import com.example.capstonewatchlist.model.MediaSearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface MediaFindApiService {
    @GET("search/movie?api_key=7d5c4def970e7f89c0b441a107534faf" +
            "&language=en-US&sort_by=popularity.desc&page=1&include_adult=false")
    suspend fun getMoviesWithTitle(@Query("query") title:String): MediaSearchResult

    @GET("search/tv?api_key=7d5c4def970e7f89c0b441a107534faf" +
            "&language=en-US&sort_by=popularity.desc&page=1&include_adult=false")
    suspend fun getShowsWithTitle(@Query("query") title:String): MediaSearchResult
}