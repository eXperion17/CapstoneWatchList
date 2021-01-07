package com.example.capstonewatchlist.model

import com.google.gson.annotations.SerializedName

data class GenreSearchResult (
    //Named results in an attempt to better parse the data coming from the API
    @SerializedName("genres")
    var results:List<Genre> )