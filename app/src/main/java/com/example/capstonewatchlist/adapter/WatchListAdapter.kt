package com.example.capstonewatchlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstonewatchlist.R
import com.example.capstonewatchlist.model.WatchItem
import java.text.SimpleDateFormat
import java.util.*

class WatchListAdapter(private val medias:List<WatchItem>) : RecyclerView.Adapter<WatchListAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun databind(item: WatchItem) {
            //TODO: Databind this to the item, but make the item layout better first!!


            //itemView.tv_releaseDate.text = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(game.releaseDate)
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