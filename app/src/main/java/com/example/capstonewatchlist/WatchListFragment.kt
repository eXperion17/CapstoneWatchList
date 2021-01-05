package com.example.capstonewatchlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstonewatchlist.adapter.WatchListAdapter
import com.example.capstonewatchlist.model.WatchItem
import com.example.capstonewatchlist.viewmodel.WatchListViewModel
import kotlinx.android.synthetic.main.fragment_watch_list.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class WatchListFragment : Fragment() {
    private val watchListViewModel: WatchListViewModel by activityViewModels();

    private var medias = arrayListOf<WatchItem>()
    private val watchListAdapter = WatchListAdapter(medias)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watch_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.fab).setOnClickListener {
            findNavController().navigate(R.id.action_watchListFragment_to_addMediaFragment)
        }

        initViews()
    }

    private fun initViews() {
        //RecyclerView Setup
        rv_watchlist.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

        rv_watchlist.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        rv_watchlist.adapter = watchListAdapter

        tab_progress.setOnClickListener {
            //Ignore if we're already on that tab
            //TODO: Check if this prevention is already built in & less magic numbers
            if (tab_base.tabMode == 0) {
                medias.clear()
                watchListViewModel.getWatchListInProgress().value?.toCollection(medias)
                watchListAdapter.notifyDataSetChanged()
            }
        }

        tab_planned.setOnClickListener {
            if (tab_base.tabMode == 1) {
                medias.clear()
                watchListViewModel.getWatchListPlanned().value?.toCollection(medias)
                watchListAdapter.notifyDataSetChanged()
            }
        }

        tab_completed.setOnClickListener {
            if (tab_base.tabMode == 2) {
                medias.clear()
                watchListViewModel.getWatchListCompleted().value?.toCollection(medias)
                watchListAdapter.notifyDataSetChanged()
            }
        }
    }
}