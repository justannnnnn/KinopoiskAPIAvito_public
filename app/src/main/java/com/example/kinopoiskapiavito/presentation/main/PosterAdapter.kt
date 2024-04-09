package com.example.kinopoiskapiavito.presentation.main

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinopoiskapiavito.R
import com.example.kinopoiskapiavito.model.Person
import com.example.kinopoiskapiavito.model.Poster
import java.util.Objects

class PosterAdapter(private val context: Context, private var posterList: MutableList<Poster>) : RecyclerView.Adapter<PosterAdapter.ViewHolder>()  {

    /*val differ: AsyncListDiffer<MovieUi> = AsyncListDiffer<MovieUi>(
        AdapterListUpdateCallback(this),
        AsyncDifferConfig.Builder<MovieUi>(BaseListDiffCallback<MovieUi?>()).build()
    )*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val movieView = inflater.inflate(R.layout.item_poster, parent, false)
        return ViewHolder(movieView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = posterList[position]
        holder.bind(listItem)
    }

    override fun getItemCount(): Int = posterList.size

    /*fun filterList(filterList: ArrayList<Movie>){
        personList = filterList.toMutableList()
        notifyDataSetChanged()
    }*/



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.carouselIV)
        fun bind(listItem: Poster) {
            loadAvatar(listItem)
        }
        private fun loadAvatar(poster: Poster) {
            val context: Context = itemView.context
            val drawable = Objects.requireNonNull<Drawable?>(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_camera
                )
            )
            drawable.setTint(ContextCompat.getColor(context, R.color.color_light_grey))
            Glide.with(itemView)
                .load(poster.url)
                .centerCrop()
                .placeholder(drawable)
                .fallback(drawable)
                .error(drawable)
                .into(image)
        }

    }


}