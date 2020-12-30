package com.example.capstonewatchlist.model

import com.google.gson.annotations.SerializedName

data class MediaSearchResult (
    //Named results in an attempt to better parse the data coming from the API
    @SerializedName("results")
    var results:List<MediaSearch> )

