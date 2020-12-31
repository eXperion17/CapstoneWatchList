package com.example.capstonewatchlist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.capstonewatchlist.model.MediaSearch
import com.example.capstonewatchlist.viewmodel.MediaFindViewModel
import kotlinx.android.synthetic.main.fragment_add_media.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddMediaFragment : Fragment() {
    //Using regular viewModels because the viewmodel will not be used by any other fragment
    private val mediaFindViewModel: MediaFindViewModel by viewModels();
    private lateinit var viewContext: Context

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
    }

}