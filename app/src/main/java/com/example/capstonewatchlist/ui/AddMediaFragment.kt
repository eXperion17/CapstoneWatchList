package com.example.capstonewatchlist.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.capstonewatchlist.R
import com.example.capstonewatchlist.model.Genre
import com.example.capstonewatchlist.model.MediaSearch
import com.example.capstonewatchlist.model.WatchItem
import com.example.capstonewatchlist.viewmodel.MediaFindViewModel
import com.example.capstonewatchlist.viewmodel.WatchListViewModel
import kotlinx.android.synthetic.main.fragment_add_media.*
import java.sql.Date

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

    private var mediaGenres = arrayListOf<Int>()
    private var uploadedOwnMedia:Boolean = false
    private var ownMediaLink:String = ""

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

        btn_upload.setOnClickListener {
            uploadImage()
        }

        btn_add.setOnClickListener {
            if (!checkIfFieldsAreEmpty()) {
                addMedia()
                findNavController().popBackStack()
            } else {
                Toast.makeText(context, getString(R.string.add_media_missing),
                    Toast.LENGTH_SHORT).show()
            }
        }

        observeSetup()
        showRelevantInputs()
    }

    private fun uploadImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Please select..."
            ),100)

        uploadedOwnMedia = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100) {
            Glide.with(viewContext).load(data?.data).into(iv_poster)
            ownMediaLink = data?.data.toString()
        }
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

        mediaFindViewModel.genreSearchResults.observe(viewLifecycleOwner, Observer {
            onGenresFound(it)
        })


    }

    private fun findMedia() {
        //No need to check whether the radio group has an active check, radio group always only has 1
        if (et_title.text.isNotBlank()) {
            mediaFindViewModel.findMediaWithTitle(et_title.text.toString(), rb_movie.isChecked)
        }
    }

    private fun onMediaFound(results:List<MediaSearch>) {
        if (rb_movie.isChecked)
            et_title.setText(results[0].title)
        else
            et_title.setText(results[0].showName)

        uploadedOwnMedia = false;

        et_summary.setText(results[0].overview)
        Glide.with(viewContext).load("https://image.tmdb.org/t/p/original" + results[0].poster).into(iv_poster)

        mediaGenres.clear()
        mediaGenres.addAll(results[0].genres)

        // Add the Media Search to an array that'll be reused to add additional information to
        // the media to the WatchList, see addMedia()
        currentItem.clear()
        currentItem.add(results[0])

        mediaFindViewModel.getAllGenres()
    }

    private fun onGenresFound(masterGenreList:List<Genre>) {
        var genresToString:String = ""

        //For each genre that we found in the Search, cross check it with the 'master list'
        //containing all the genres. If we have a match then add it to the string
        mediaGenres.forEach { genre ->
            masterGenreList.forEach {
                if (it.id == genre) {
                    genresToString += it.name + ", "
                }
            }
        }

        //Remove the last ", " we added
        genresToString = genresToString.removeRange(genresToString.length-2, genresToString.length)

        et_genres.setText(genresToString)
    }

    private fun checkIfFieldsAreEmpty():Boolean {
        var isEmpty = false

        if (et_title.text.isBlank()) {
            isEmpty = true;
        }

        if (rb_tv.isChecked && (et_currentepisode.text.isBlank() ||
                                et_episode_count.text.isBlank())) {
            isEmpty = true;
        }
        return isEmpty
    }

    private fun addMedia() {
        var date = "2018-01-01"
        if (currentItem.size > 0) {
            if (rb_movie.isChecked)
                currentItem[0].releaseDate
            else
                currentItem[0].firstAirDate
        }

        //Pre-define it so it doesn't give an input error when submitting an empty field
        var episodeCount = -1;
        if (et_episode_count.text.isNotBlank()) {
            episodeCount = String.format(et_episode_count.text.toString()).toInt()
        }
        var currentEpisode = 0
        if (et_currentepisode.text.isNotBlank()) {
            currentEpisode = et_currentepisode.text.toString().toInt()
        }

        var posterLink = ""
        var backdropLink = ""
        if (uploadedOwnMedia) {
            posterLink = ownMediaLink
            backdropLink = ownMediaLink
        } else {
            posterLink = currentItem[0].poster
            backdropLink = currentItem[0].backdrop
        }

        val media = WatchItem(
            et_title.text.toString(),
            et_summary.text.toString(),
            posterLink,
            backdropLink,
            et_genres.text.toString(),
            Date.valueOf(date),
            WatchListFragment.LIST_PLANNED,
            rb_movie.isChecked,
            false,
            episodeCount,
            currentEpisode,
            uploadedOwnMedia
        )

        watchListViewModel.insertMedia(media)
    }

}