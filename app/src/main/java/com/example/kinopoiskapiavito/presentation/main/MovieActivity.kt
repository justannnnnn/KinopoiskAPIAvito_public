package com.example.kinopoiskapiavito.presentation.main

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.kinopoiskapiavito.Constants.networkService
import com.example.kinopoiskapiavito.R
import com.example.kinopoiskapiavito.model.ItemName
import com.example.kinopoiskapiavito.model.Movie
import com.example.kinopoiskapiavito.model.PosterList
import com.example.kinopoiskapiavito.presentation.main.network.NetworkService
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.UncontainedCarouselStrategy
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Objects
import kotlin.math.round

class MovieActivity : AppCompatActivity() {
    lateinit var nService: NetworkService
    lateinit var rvActors: RecyclerView
    lateinit var postersCarousel: ImageCarousel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movie)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
        val mainIV = findViewById<ImageView>(R.id.image)
        val nameTV = findViewById<TextView>(R.id.nameTV)
        val ratkpTV = findViewById<TextView>(R.id.ratingKinopoiskTV)
        val enNameTV = findViewById<TextView>(R.id.enNameTV)
        val yearTV = findViewById<TextView>(R.id.yearTV)
        val genresTV = findViewById<TextView>(R.id.genresTV)
        val countriesTV = findViewById<TextView>(R.id.countriesTV)
        val lengthTV = findViewById<TextView>(R.id.lengthTV)
        val ageRatingTV = findViewById<TextView>(R.id.ageRatingTV)
        val descTV = findViewById<TextView>(R.id.descriptionTV)
        rvActors = findViewById(R.id.actorsRV)
        postersCarousel = findViewById(R.id.postersCarousel)
        val noActorsTV = findViewById<TextView>(R.id.no_actorsTV)
        val noPostersTV = findViewById<TextView>(R.id.no_postersTV)



        val id = this.intent.getIntExtra("id", -1)
        val context = mainIV.context
        val drawable = Objects.requireNonNull<Drawable?>(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_camera
            )
        )
        drawable.setTint(ContextCompat.getColor(context, R.color.color_light_grey))



        nService = networkService
        nService.getMovieInfo(id=id.toString()).enqueue(object: Callback<Movie>{
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful) {
                    val movie = response.body()

                    Glide.with(this@MovieActivity)
                        .load(movie!!.poster?.previewUrl)
                        .centerCrop()
                        .placeholder(drawable)
                        .fallback(drawable)
                        .error(drawable)
                        .into(mainIV)

                    nameTV.text = movie.name

                    ratkpTV.text = movie.rating?.kp?.let { round(it).toString() }
                    if (movie.rating?.kp!! > 7) ratkpTV.setTextColor(resources.getColor(R.color.color_green))
                    else if (movie.rating?.kp!! < 5) ratkpTV.setTextColor(resources.getColor(R.color.color_red))

                    if (movie.alternativeName != null) {
                        enNameTV.text = " " + movie.alternativeName
                    } else enNameTV.text = ""


                    yearTV.text = movie.year.toString()

                    var genresString = ""
                    for (genre in movie.genres as List<ItemName>)
                        genresString += genre.name + ", "
                    if (genresString != "") {
                        yearTV.text = yearTV.text as String + ", "
                        genresTV.text = genresString.dropLast(2)
                    }

                    var countriesString = ""
                    for (country in movie.countries as List<ItemName>)
                        countriesString += country.name + ", "
                    if (countriesString != "") countriesTV.text = countriesString
                    else countriesTV.text = ""

                    lengthTV.text =
                        (if (movie.isSeries) movie.seriesLength.toString() else movie.movieLength.toString()) + " мин"

                    if (movie.ratingMpaa != null) {
                        ageRatingTV.text = movie.ratingMpaa?.uppercase()
                        lengthTV.text = lengthTV.text as String + ", "
                    } else ageRatingTV.text = ""

                    descTV.text = movie.shortDescription

                    val actors = movie.persons
                    if (actors != null) {
                        noActorsTV.visibility = View.GONE
                        rvActors.adapter = PersonAdapter(this@MovieActivity, actors.toMutableList())
                        val layoutManager = GridLayoutManager(this@MovieActivity, 2)
                        layoutManager.orientation = RecyclerView.HORIZONTAL
                        rvActors.layoutManager = layoutManager
                    } else {
                        noActorsTV.visibility = View.VISIBLE
                    }
                    /*   rvPosters.apply {
                    clipChildren = false
                    clipToPadding = false
                    offscreenPageLimit = 3
                    (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                }*/
                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                noActorsTV.visibility = View.VISIBLE
            }
        })



            nService.getPosters(id=id).enqueue(object: Callback<PosterList>{
                    override fun onResponse(
                        call: Call<PosterList>,
                        response: Response<PosterList>
                    ) {
                        if (response.isSuccessful){
                            noPostersTV.visibility = View.GONE
                            val posters = (response.body() as PosterList).docs
                            val list = mutableListOf<CarouselItem>()
                            for (p in posters) list.add(CarouselItem(p.url))
                            postersCarousel.addData(list)
                        }
                        else noPostersTV.visibility = View.VISIBLE
                    }

                    override fun onFailure(call: Call<PosterList>, t: Throwable) {
                        noPostersTV.visibility = View.VISIBLE
                    }

                })
            }
}