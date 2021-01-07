package com.example.capstonewatchlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstonewatchlist.adapter.WatchListAdapter
import com.example.capstonewatchlist.model.WatchItem
import com.example.capstonewatchlist.viewmodel.WatchListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_watch_list.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class WatchListFragment : Fragment() {

    private val watchListViewModel: WatchListViewModel by activityViewModels()

    private var medias = arrayListOf<WatchItem>()
    private var currentTab = 1
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

        createItemTouchHelper().attachToRecyclerView(rv_watchlist)

        //Load the WatchItems onto the current tab
        tab_base.getTabAt(currentTab)?.select()
        loadWatchList(currentTab)
    }

    private fun onAdapterCardUpdate(item:WatchItem, changedFavorite:Boolean) {
        if (!changedFavorite) {
            //Currently changing anything of the item/card triggers this function, hence why we
            //check if favorite changed first, as the only exception to preventing this dialog
            if (item.isMovie || (!item.isMovie && item.episodesWatched == item.totalEpisodes)) {
                createDialogAutoMoveCompletion(item)
            }
        }
        watchListViewModel.updateMedia(item)
    }

    private fun createDialogAutoMoveCompletion(item: WatchItem) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.dialog_autoadd_title))
            .setMessage(String.format(resources.getString(R.string.dialog_autoadd_message), item.title))
            .setNeutralButton(resources.getString(R.string.dialog_autoadd_decline)) { _ , _ -> }
            .setPositiveButton(resources.getString(R.string.dialog_autoadd_accept)) { _ , _ ->
                item.listId = LIST_COMPLETED
                watchListViewModel.updateMedia(item)
                loadWatchList(currentTab)
            }
            .show()
    }

    private fun createDialogMovingItem(item: WatchItem) {
        val options = arrayOf(  resources.getString(R.string.tab_in_progress),
                                resources.getString(R.string.tab_planned),
                                resources.getString(R.string.tab_completed))
        val selected = item.listId

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.dialog_autoadd_title))
            //.setMessage(String.format(resources.getString(R.string.dialog_move_message), item.title))
            .setNeutralButton(resources.getString(R.string.dialog_autoadd_decline)){ _, _ ->

            }
            .setPositiveButton(resources.getString(R.string.dialog_autoadd_accept)){ _, _ ->
                item.listId = selected
                watchListViewModel.updateMedia(item)
                loadWatchList(currentTab)
            }
            .setSingleChoiceItems(options,selected) { _, _ ->

            }
            .show()
    }

    private fun loadWatchList(listID:Int) {
        medias.clear()

        //Use a local variable to sort the watchItems by favorite status
        val sortedList = arrayListOf<WatchItem>()
        sortedList.addAll(allWatchItems.sortedWith(compareBy { !it.favorite }))
        allWatchItems.clear()
        allWatchItems.addAll(sortedList)

        allWatchItems.forEach {
            //First all favorites
            if (it.listId == listID) {
                medias.add(it)
            }
        }

        watchListAdapter.notifyDataSetChanged()
    }

    private fun observeChanges() {
        watchListViewModel.watchList.observe(viewLifecycleOwner, { list -> list?.let {
            allWatchItems.clear()
            allWatchItems.addAll(list)

            loadWatchList(currentTab)
            }
        })
    }

    private fun createItemTouchHelper(): ItemTouchHelper {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                watchListViewModel.deleteMedia(medias[position])
                showUndoOption(medias[position])
            }
        }
        return ItemTouchHelper(callback)
    }

    private fun showUndoOption(item:WatchItem) {
        val undoMessage = Snackbar.make(
            requireView(),
            String.format(getString(R.string.success_delete), item.title),
            Snackbar.LENGTH_LONG
        )
        undoMessage.setAction(getString(R.string.undo_delete)) {
            watchListViewModel.insertMedia(item)
        }
        undoMessage.show()
    }

    companion object {
        const val LIST_IN_PROGRESS = 0
        const val LIST_PLANNED = 1
        const val LIST_COMPLETED = 2
    }
}