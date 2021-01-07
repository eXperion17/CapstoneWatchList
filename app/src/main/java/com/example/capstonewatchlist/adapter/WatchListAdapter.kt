package com.example.capstonewatchlist.adapter

import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstonewatchlist.R
import com.example.capstonewatchlist.model.WatchItem
import kotlinx.android.synthetic.main.item_media.view.*

class WatchListAdapter(private val medias:List<WatchItem>) : RecyclerView.Adapter<WatchListAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun databind(item: WatchItem) {
            //TODO: Databind this to the item, but make the item layout better first!!

            itemView.tv_title.text = item.title
            itemView.tv_genres.text = item.genres
            itemView.tv_runtime.text = item.mediaLength.toString()
            itemView.tv_summary.text = item.overview

            itemView.layout_expanded.visibility = View.GONE

            //TODO: Switch between the two states of colors
            //itemView.ib_favorite


            //Pre-hide things that aren't visible in the Movie variant
            if (item.isMovie)
                itemView.btn_minus.visibility = View.GONE

            Glide.with(itemView.context).load("https://image.tmdb.org/t/p/original" + item.poster).into(itemView.iv_poster)

            //itemView.tv_releaseDate.text = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(game.releaseDate)

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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate((R.layout.item_media), parent, false)
        );
    }

    override fun getItemCount(): Int {
        return medias.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(medias[position])
    }
}