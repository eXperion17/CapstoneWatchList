package com.example.capstonewatchlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstonewatchlist.adapter.WatchListAdapter
import com.example.capstonewatchlist.model.WatchItem
import com.example.capstonewatchlist.viewmodel.WatchListViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_watch_list.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class WatchListFragment : Fragment() {
    private val watchListViewModel: WatchListViewModel by activityViewModels();

    private var medias = arrayListOf<WatchItem>()
    private var currentTab = -1
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

        initViews()
    }

    private fun initViews() {
        //RecyclerView Setup
        rv_watchlist.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

        rv_watchlist.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        rv_watchlist.adapter = watchListAdapter

        tab_base.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (currentTab != tab?.position) {
                    medias.clear()
                    when (tab?.position) {
                        0 -> watchListViewModel.getWatchListInProgress().value?.toCollection(medias)
                        1 -> watchListViewModel.getWatchListPlanned().value?.toCollection(medias)
                        2 -> watchListViewModel.getWatchListCompleted().value?.toCollection(medias)
                    }
                    watchListAdapter.notifyDataSetChanged()
                }
            }
            //These overrides are necessary to be able to implement this listener
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

    }
}