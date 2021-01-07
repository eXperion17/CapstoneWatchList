package com.example.capstonewatchlist.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstonewatchlist.repository.MediaFindRepository
import kotlinx.coroutines.launch

class MediaFindViewModel(application: Application) : AndroidViewModel(application) {
    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String>
        get() = _errorText

    private val mediaFindRepository:MediaFindRepository = MediaFindRepository()
    val mediaSearchResults = mediaFindRepository.medias
    val genreSearchResults = mediaFindRepository.genres

    fun findMediaWithTitle(title: String, isMovie:Boolean) {
        viewModelScope.launch {
            try {
                mediaFindRepository.findMediaWithTitle(title, isMovie)
            } catch (error:Throwable) {
                _errorText.value = error.message
                Log.e("Error finding media: ", error.cause.toString())
            }
        }
    }

    fun getAllGenres() {
        viewModelScope.launch {
            try {
                mediaFindRepository.getAllGenres()
            } catch (error:Throwable) {
                _errorText.value = error.message
                Log.e("Error finding genres: ", error.cause.toString())
            }
        }
    }

}