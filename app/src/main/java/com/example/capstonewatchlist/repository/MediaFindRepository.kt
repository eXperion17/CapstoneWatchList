package com.example.capstonewatchlist.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstonewatchlist.api.MediaFindApi
import com.example.capstonewatchlist.api.MediaFindApiService
import com.example.capstonewatchlist.model.MediaSearch
import com.example.capstonewatchlist.model.MediaSearchResult
import kotlinx.coroutines.withTimeout

class MediaFindRepository {
    private val mediaFindApiService: MediaFindApiService = MediaFindApi.createApi()

    private val _medias: MutableLiveData<List<MediaSearch>> = MutableLiveData()

    val medias: LiveData<List<MediaSearch>>
        get() = _medias

    suspend fun findMediaWithTitle(title: String, isMovie:Boolean) {
        try {
            //timeout the request after 5 seconds
            val result = withTimeout(5_000) {
                if (isMovie)
                    mediaFindApiService.getMoviesWithTitle(title)
                else
                    mediaFindApiService.getShowsWithTitle(title)
            }
            _medias.value = result.results

        } catch (error: Throwable) {
            throw MediaFindError("Unable to get the list of tv shows/movies.", error)
        }
    }

    class MediaFindError(message: String, cause: Throwable) : Throwable(message, cause)
}