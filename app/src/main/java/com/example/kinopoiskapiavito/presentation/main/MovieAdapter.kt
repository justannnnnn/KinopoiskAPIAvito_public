package com.example.kinopoiskapiavito.presentation.main

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinopoiskapiavito.R
import com.example.kinopoiskapiavito.model.Movie
import java.util.Objects

class MovieAdapter(private val context: Context, private var movieList: MutableList<Movie>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>()  {

    /*val differ: AsyncListDiffer<MovieUi> = AsyncListDiffer<MovieUi>(
        AdapterListUpdateCallback(this),
        AsyncDifferConfig.Builder<MovieUi>(BaseListDiffCallback<MovieUi?>()).build()
    )*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val movieView = inflater.inflate(R.layout.item_movie, parent, false)
        return ViewHolder(movieView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = movieList[position]
        holder.bind(listItem)
        holder.nameTextView.text = listItem.name
        holder.enNameTextView.text = if (listItem.alternativeName != null) listItem.alternativeName + ", " else ""
        holder.year.text = listItem.year.toString()

        var countries = ""
        for (country in listItem.countries!!){
            countries += country.name + ", "
        }
        holder.countries.text = (if (countries != "") countries.dropLast(2) else "Нет информации") + " • "

        if (listItem.genres != null && listItem.genres.size > 0) {
            var genres = listItem.genres?.get(0)?.name
            if (genres != null) {
                holder.genres.text = if (genres != "") genres else "Нет информации"
            }
        }

    }

    override fun getItemCount(): Int = movieList.size

    fun filterList(filterList: ArrayList<Movie>){
        movieList = filterList.toMutableList()
        notifyDataSetChanged()
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.name)
        val enNameTextView = itemView.findViewById<TextView>(R.id.enName)
        val year = itemView.findViewById<TextView>(R.id.year)
        val countries = itemView.findViewById<TextView>(R.id.countries)
        val genres = itemView.findViewById<TextView>(R.id.genres)
        val image = itemView.findViewById<ImageView>(R.id.movie_photo)
        fun bind(listItem: Movie) {
            loadAvatar(listItem)
            itemView.setOnClickListener {
                val intent = Intent(it.context, MovieActivity::class.java)
                intent.putExtra("id", listItem.id)
                startActivity(it.context, intent, null)

                Toast.makeText(it.context, "Нажал на ${listItem.name}", Toast.LENGTH_SHORT).show()
            }
        }
        private fun loadAvatar(movie: Movie) {
            val context: Context = itemView.context
            val drawable = Objects.requireNonNull<Drawable?>(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_camera
                )
            )
            drawable.setTint(ContextCompat.getColor(context, R.color.color_light_grey))
            Glide.with(itemView)
                .load(movie.poster?.previewUrl)
                .centerCrop()
                .placeholder(drawable)
                .fallback(drawable)
                .error(drawable)
                .into(image)
        }

    }


}

/*class MovieAdapter(private val context: Context, private val movieList: MutableList<Movie>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    /*val differ: AsyncListDiffer<MovieUi> = AsyncListDiffer<MovieUi>(
        AdapterListUpdateCallback(this),
        AsyncDifferConfig.Builder<MovieUi>(BaseListDiffCallback<MovieUi?>()).build()
    )*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val movieView = inflater.inflate(R.layout.item_movie, parent, false)
        return ViewHolder(movieView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val listItem = movieList[position]
        holder.bind(listItem)
        holder.nameTextView.text = listItem.name
        holder.enNameTextView.text = listItem.enName
        holder.year.text = listItem.year.toString()

        var countries = ""
        for (country in listItem.countries!!){
            countries += country.name + ", "
        }
        holder.countries.text = if (countries != "") countries.dropLast(2) else "Нет информации"

        var genres = ""
        for (genre in listItem.countries!!){
            genres += genre.name + ", "
        }
        holder.genres.text = if (genres != "") genres.dropLast(2) else "Нет информации"
    }

    override fun getItemCount(): Int = movieList.size

    /*fun setItems(items: List<MovieUi>?) {
        differ.submitList(items)
    }

    fun setItems(items: List<MovieUi>?, callback: Runnable) {
        differ.submitList(items, callback)
    }*/


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.name)
        val enNameTextView = itemView.findViewById<TextView>(R.id.enName)
        val year = itemView.findViewById<TextView>(R.id.year)
        val countries = itemView.findViewById<TextView>(R.id.countries)
        val genres = itemView.findViewById<TextView>(R.id.genres)
        val image = itemView.findViewById<ImageView>(R.id.movie_photo)
        fun bind(listItem: Movie) {
            loadAvatar(listItem)
            itemView.setOnClickListener {
                Toast.makeText(it.context, "нажал на ${listItem.name}", Toast.LENGTH_SHORT).show()
            }
        }
        private fun loadAvatar(movie: Movie) {
            val context: Context = itemView.context
            val drawable = Objects.requireNonNull<Drawable?>(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_camera
                )
            )
            drawable.setTint(ContextCompat.getColor(context, R.color.color_light_grey))
            Glide.with(itemView)
                .load(movie.poster?.previewUrl)
                .centerCrop()
                .placeholder(drawable)
                .fallback(drawable)
                .error(drawable)
                .into(image)
        }

    }


}*/
