package com.example.capstonewatchlist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.capstonewatchlist.model.MediaSearch
import com.example.capstonewatchlist.model.WatchItem
import com.example.capstonewatchlist.viewmodel.MediaFindViewModel
import com.example.capstonewatchlist.viewmodel.WatchListViewModel
import kotlinx.android.synthetic.main.fragment_add_media.*
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddMediaFragment : Fragment() {
    //Using regular viewModels because the viewmodel will not be used by any other fragment
    private val mediaFindViewModel: MediaFindViewModel by viewModels();
    //WatchList however, needs to be shared through fragments
    private val watchListViewModel: WatchListViewModel by activityViewModels();
    private lateinit var viewContext: Context

    private var currentItem = arrayListOf<MediaSearch>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewContext = view.context

        showRelevantInputs()

        btn_search.setOnClickListener {
            findMedia()
        }
        rb_tv.setOnClickListener {
            showRelevantInputs()
        }
        rb_movie.setOnClickListener {
            showRelevantInputs()
        }

        btn_add.setOnClickListener {
            addMedia()
            findNavController().navigate(R.id.action_addMediaFragment_to_watchListFragment)
        }

        observeSetup()
        showRelevantInputs()
    }

    private fun showRelevantInputs() {
        if (rb_movie.isChecked) {
            et_currentepisode.isVisible = false
            et_episode_count.isVisible = false
        } else {
            et_currentepisode.isVisible = true
            et_episode_count.isVisible = true
        }

    }

    private fun observeSetup() {
        mediaFindViewModel.mediaSearchResults.observe(viewLifecycleOwner, Observer {
            onMediaFound(it)
        })
    }

    private fun findMedia() {
        //No need to check whether the radio group has an active check, radio group always only has 1
        if (et_title.text.isNotBlank()) {
            mediaFindViewModel.findMediaWithTitle(et_title.text.toString(), rb_movie.isChecked)
        }
    }

    private fun onMediaFound(results:List<MediaSearch>) {
        et_title.setText(results[0].title)
        et_summary.setText(results[0].overview)
        Glide.with(viewContext).load("https://image.tmdb.org/t/p/original" + results[0].poster).into(iv_poster)
        et_genres.setText(results[0].genres.toString())

        // Add the Media Search to an array that'll be reused to add additional information to
        // the media to the WatchList, see addMedia()
        currentItem.clear()
        currentItem.add(results[0])
    }

    private fun addMedia() {
        //TODO: Add cancel or stop if things are left empty

        var date = if(rb_movie.isChecked)
            currentItem[0].releaseDate else currentItem[0].firstAirDate

        //Pre define it so it doesn't give an input error when submitting an empty field
        var episodeCount = -1;
        if (et_episode_count.text.isNotBlank()) {
            episodeCount = String.format(et_episode_count.text.toString()).toInt()
        }


        val media = WatchItem(
            et_title.text.toString(),
            et_summary.text.toString(),
            currentItem[0].poster,
            currentItem[0].backdrop,
            et_genres.text.toString(),
            Date.valueOf(date),
            //List ID/Type is 1, and 1 = Planned to watch
            1,
            rb_movie.isChecked,
            false,
            episodeCount
        )

        watchListViewModel.insertMedia(media)
    }

}