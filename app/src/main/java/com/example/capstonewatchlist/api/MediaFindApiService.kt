package com.example.capstonewatchlist.api

import com.example.capstonewatchlist.BuildConfig
import com.example.capstonewatchlist.model.MediaSearchResult
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY: String = BuildConfig.API_KEY

interface MediaFindApiService {
    @GET("search/movie?api_key=$API_KEY" +
            "&language=en-US&sort_by=popularity.desc&page=1&include_adult=false")
    suspend fun getMoviesWithTitle(@Query("query") title:String): MediaSearchResult

    @GET("search/tv?api_key=$API_KEY" +
            "&language=en-US&sort_by=popularity.desc&page=1&include_adult=false")
    suspend fun getShowsWithTitle(@Query("query") title:String): MediaSearchResult
}