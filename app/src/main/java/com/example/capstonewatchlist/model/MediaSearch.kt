package com.example.capstonewatchlist.model

import com.google.gson.annotations.SerializedName
import java.sql.Date

data class MediaSearch(
    @SerializedName("title") var title: String,
    @SerializedName("overview") var overview: String,
    @SerializedName("vote_average") var rating: Float,
    @SerializedName("poster_path") var poster: String,
    @SerializedName("backdrop_path") var backdrop: String,
    @SerializedName("genre_ids") var genres: List<Int>,

    //Movie Exclusives
    @SerializedName("release_date") var releaseDate: String,

    //Tv Show Exclusives
    @SerializedName("first_air_date") var firstAirDate: String,

)
