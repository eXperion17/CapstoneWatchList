package com.example.capstonewatchlist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstonewatchlist.model.WatchItem
import com.example.capstonewatchlist.repository.WatchListRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WatchListViewModel(application: Application) : AndroidViewModel(application) {
    private val listRepository =  WatchListRepository(application.applicationContext)
    private val mainScope = CoroutineScope(Dispatchers.Main)

    //Consolidated from what is normally in MovieRepository
    /*private val _watchList: MutableLiveData<List<WatchItem>> = MutableLiveData()
    val watchList: LiveData<List<WatchItem>>
        get() = _watchList*/

    val watchList = listRepository.getWatchList()

    val error = MutableLiveData<String>()
    val success = MutableLiveData<Boolean>()

    fun insertMedia(item: WatchItem) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                listRepository.insertItem(item)
            }
            success.value = true
        }
    }

    fun deleteMedia(item:WatchItem) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                listRepository.deleteItem(item)
            }
            success.value = true
        }
    }


    fun updateMedia(item:WatchItem) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                listRepository.updateItem(item)
            }
            success.value = true
        }
    }

    fun getWatchListAll(): LiveData<List<WatchItem>> {
        return listRepository.getWatchList()
    }
}