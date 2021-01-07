package com.example.capstonewatchlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstonewatchlist.R
import com.example.capstonewatchlist.WatchListFragment
import com.example.capstonewatchlist.model.WatchItem
import kotlinx.android.synthetic.main.item_media.view.*

class WatchListAdapter(private val medias:List<WatchItem>,
                       private val onCardUpdate: (WatchItem, Boolean) -> Unit,
                       private val onMoveItem: (WatchItem) -> Unit)
    : RecyclerView.Adapter<WatchListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun databind(item: WatchItem) {
            Glide.with(itemView.context).load(
                "https://image.tmdb.org/t/p/original" + item.poster).into(itemView.iv_poster)
            itemView.tv_title.text = item.title
            itemView.tv_genres.text = item.genres
            itemView.tv_summary.text = item.overview
            itemView.tv_releasedate.text = item.release_date.toString()
            updateFavorite(item.favorite)

            when (item.listId) {
                WatchListFragment.LIST_IN_PROGRESS  -> itemView.tv_type.text = itemView.context.getText(R.string.info_in_progress)
                WatchListFragment.LIST_PLANNED      -> itemView.tv_type.text = itemView.context.getText(R.string.info_planned)
                WatchListFragment.LIST_COMPLETED    -> itemView.tv_type.text = itemView.context.getText(R.string.info_completed)
            }

            //Switching tabs without retracting/closing the expanded tab causes the text to
            //stay on Close, this updates the text upon loading so that doesn't happen
            if (itemView.layout_expanded.visibility == View.VISIBLE)
                updateExpandedLayout(false)
            else
                updateExpandedLayout(true)

            itemView.layout_expanded.visibility = View.GONE

            //Pre-hide things that aren't visible in the Movie variant
            if (item.isMovie) {
                itemView.btn_minus.visibility = View.GONE
                itemView.tv_watchprogress.visibility = View.GONE
                itemView.btn_watched.text = itemView.context.getString(R.string.item_watched_movie)
            } else {
                itemView.tv_watchprogress.text =
                    item.episodesWatched.toString() + "/" + item.totalEpisodes.toString()
                itemView.btn_watched.text = itemView.context.getString(R.string.item_watched_tv)
            }

            if (item.listId == WatchListFragment.LIST_COMPLETED) {
                itemView.btn_watched.visibility = View.GONE
            } else {
                itemView.btn_watched.visibility = View.VISIBLE
            }



            //                      Setting Click Listeners here!

            itemView.btn_expand.setOnClickListener {
                if (itemView.layout_expanded.visibility == View.VISIBLE)
                    updateExpandedLayout(false)
                else
                    updateExpandedLayout(true)
            }

            itemView.btn_watched.setOnClickListener {
                if (item.episodesWatched != item.totalEpisodes) {
                    item.episodesWatched++
                }

                updateCard(item)
            }

            itemView.btn_minus.setOnClickListener {
                if (item.episodesWatched > 0)
                    item.episodesWatched--

                updateCard(item)
            }

            itemView.ib_favorite.setOnClickListener {
                item.favorite = !item.favorite
                updateCard(item, true)
            }

            itemView.btn_moveto.setOnClickListener {
                onMoveItem(item)
            }
        }

        private fun updateCard(item: WatchItem, favoriteChanged:Boolean = false) {
            updateFavorite(item.favorite)

            if (!item.isMovie) {
                itemView.tv_watchprogress.text =
                    item.episodesWatched.toString() + "/" + item.totalEpisodes.toString()
            }

            //Send to database
            onCardUpdate(item, favoriteChanged)
        }

        private fun updateFavorite(state:Boolean) {
            if (state) {
                itemView.ib_favorite.setColorFilter(ContextCompat.getColor(itemView.context,R.color.colorPrimary))
            } else {
                itemView.ib_favorite.setColorFilter(ContextCompat.getColor(itemView.context,R.color.colorNonFavorite))
            }
        }

        private fun updateExpandedLayout(setVisibilityTo:Boolean) {
            if (setVisibilityTo) {
                itemView.layout_expanded.visibility = View.VISIBLE
                itemView.btn_expand.text = itemView.context.getString(R.string.item_expand_open)
            } else {
                itemView.layout_expanded.visibility = View.GONE
                itemView.btn_expand.text = itemView.context.getString(R.string.item_expand)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate((R.layout.item_media), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return medias.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(medias[position])
    }
}