package com.example.capstonewatchlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstonewatchlist.adapter.WatchListAdapter
import com.example.capstonewatchlist.model.WatchItem
import com.example.capstonewatchlist.viewmodel.WatchListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_watch_list.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class WatchListFragment : Fragment() {
    private val watchListViewModel: WatchListViewModel by activityViewModels();

    private var medias = arrayListOf<WatchItem>()
    private var currentTab = -1
    private val watchListAdapter = WatchListAdapter(medias,
        :: onAdapterCardUpdate,
        :: createDialogMovingItem)

    //A 'middle man' that contains all of the watch items, medias is the filtered variant
    private var allWatchItems = arrayListOf<WatchItem>()

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
                    currentTab = tab?.position!!

                    loadWatchList(currentTab)
                    watchListAdapter.notifyDataSetChanged()
                }
            }
            //These overrides are necessary to be able to implement this listener
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        observeChanges()
    }

    private fun onAdapterCardUpdate(item:WatchItem) {
        if (item.isMovie || (!item.isMovie && item.episodesWatched == item.totalEpisodes)) {
            createDialogAutoMoveCompletion(item)
        } else {
            watchListViewModel.updateMedia(item)
        }

        //watchListAdapter.notifyDataSetChanged()
    }

    private fun createDialogAutoMoveCompletion(item: WatchItem) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.dialog_autoadd_title))
            .setMessage(String.format(resources.getString(R.string.dialog_autoadd_message), item.title))
            .setNeutralButton(resources.getString(R.string.dialog_autoadd_decline)) { _ , _ ->
                //cancel
            }
            .setPositiveButton(resources.getString(R.string.dialog_autoadd_accept)) { _ , _ ->
                //accept
                //TODO: Constants?
                item.listId = 2;
                watchListViewModel.updateMedia(item)
                watchListAdapter.notifyDataSetChanged()
                loadWatchList(currentTab)
            }
            .show()
    }

    private fun createDialogMovingItem(item: WatchItem) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.dialog_autoadd_title))
            .setMessage(String.format(resources.getString(R.string.dialog_move_message), item.title))
            .setNeutralButton(resources.getString(R.string.tab_in_progress)) { _ , _ ->
                item.listId = 0
                watchListViewModel.updateMedia(item)
                watchListAdapter.notifyDataSetChanged()
                loadWatchList(currentTab)
            }
            .setNegativeButton(resources.getString(R.string.tab_planned)) { _ , _ ->
                item.listId = 1
                watchListViewModel.updateMedia(item)
                watchListAdapter.notifyDataSetChanged()
                loadWatchList(currentTab)
            }
            .setPositiveButton(resources.getString(R.string.tab_completed)) { _ , _ ->
                item.listId = 2
                watchListViewModel.updateMedia(item)
                watchListAdapter.notifyDataSetChanged()
                loadWatchList(currentTab)
            }
            .show()
    }

    private fun loadWatchList(listID:Int) {
        medias.clear()
        //TODO: Filter on favorite and then merge them together
        allWatchItems.forEach {
            if (it.listId == listID) {
                medias.add(it)
            }
        }
    }

    private fun observeChanges() {
        watchListViewModel.watchList.observe(viewLifecycleOwner, Observer { list -> list?.let {
            allWatchItems.clear()
            allWatchItems.addAll(list)
            }
        })
    }
}