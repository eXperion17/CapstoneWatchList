package com.example.capstonewatchlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstonewatchlist.R
import com.example.capstonewatchlist.model.WatchItem
import kotlinx.android.synthetic.main.item_media.view.*

class WatchListAdapter(private val medias:List<WatchItem>,
                       private val onCardUpdate: (WatchItem) -> Unit,
                       private val onMoveItem: (WatchItem) -> Unit)
    : RecyclerView.Adapter<WatchListAdapter.ViewHolder>() {

    val onItemChanged: () -> Unit = { Unit }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun databind(item: WatchItem) {
            Glide.with(itemView.context).load(
                "https://image.tmdb.org/t/p/original" + item.poster).into(itemView.iv_poster)
            itemView.tv_title.text = item.title
            itemView.tv_genres.text = item.genres
            itemView.tv_summary.text = item.overview
            itemView.tv_releasedate.text = item.release_date.toString()

            when (item.listId) {
                0 -> itemView.tv_type.text = itemView.context.getText(R.string.info_in_progress)
                1 -> itemView.tv_type.text = itemView.context.getText(R.string.info_planned)
                2 -> itemView.tv_type.text = itemView.context.getText(R.string.info_completed)
            }

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


            //                      Setting Click Listeners here!


            itemView.btn_expand.setOnClickListener {
                //Toggle between the visibility states
                if (itemView.layout_expanded.visibility == View.VISIBLE) {
                    itemView.layout_expanded.visibility = View.GONE
                    itemView.btn_expand.text = itemView.context.getString(R.string.item_expand)
                } else {
                    itemView.layout_expanded.visibility = View.VISIBLE
                    itemView.btn_expand.text = itemView.context.getString(R.string.item_expand_open)
                }
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
                updateCard(item)
            }

            itemView.btn_moveto.setOnClickListener {
                onMoveItem(item)
            }
        }

        private fun updateCard(item: WatchItem) {
            if (item.favorite) {
                itemView.ib_favorite.setColorFilter(ContextCompat.getColor(itemView.context,R.color.colorPrimary))
            } else {
                itemView.ib_favorite.setColorFilter(ContextCompat.getColor(itemView.context,R.color.colorNonFavorite))
            }

            if (!item.isMovie) {
                itemView.tv_watchprogress.text =
                    item.episodesWatched.toString() + "/" + item.totalEpisodes.toString()
            }

            //Send to database?
            onCardUpdate(item)
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