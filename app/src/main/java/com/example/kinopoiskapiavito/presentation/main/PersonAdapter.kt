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
import java.util.Objects

class PersonAdapter(private val context: Context, private var personList: MutableList<Person>) : RecyclerView.Adapter<PersonAdapter.ViewHolder>()  {

    /*val differ: AsyncListDiffer<MovieUi> = AsyncListDiffer<MovieUi>(
        AdapterListUpdateCallback(this),
        AsyncDifferConfig.Builder<MovieUi>(BaseListDiffCallback<MovieUi?>()).build()
    )*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val movieView = inflater.inflate(R.layout.item_actor, parent, false)
        return ViewHolder(movieView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = personList[position]
        holder.bind(listItem)
        holder.nameTV.text = listItem.name
        holder.roleTV.text = listItem.profession
    }

    override fun getItemCount(): Int = personList.size

    /*fun filterList(filterList: ArrayList<Movie>){
        personList = filterList.toMutableList()
        notifyDataSetChanged()
    }*/



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTV = itemView.findViewById<TextView>(R.id.nameTV)
        val roleTV = itemView.findViewById<TextView>(R.id.roleTV)
        val image = itemView.findViewById<ImageView>(R.id.photoIV)
        fun bind(listItem: Person) {
            loadAvatar(listItem)
        }
        private fun loadAvatar(person: Person) {
            val context: Context = itemView.context
            val drawable = Objects.requireNonNull<Drawable?>(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_camera
                )
            )
            drawable.setTint(ContextCompat.getColor(context, R.color.color_light_grey))
            Glide.with(itemView)
                .load(person.photo)
                .centerCrop()
                .placeholder(drawable)
                .fallback(drawable)
                .error(drawable)
                .into(image)
        }

    }


}