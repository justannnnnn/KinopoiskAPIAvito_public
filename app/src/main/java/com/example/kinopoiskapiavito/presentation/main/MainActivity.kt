package com.example.kinopoiskapiavito.presentation.main


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinopoiskapiavito.Constants.RESULT_ACCESS
import com.example.kinopoiskapiavito.Constants.RESULT_DENIED
import com.example.kinopoiskapiavito.Constants.networkService
import com.example.kinopoiskapiavito.R
import com.example.kinopoiskapiavito.model.Movie
import com.example.kinopoiskapiavito.model.MovieList
import com.example.kinopoiskapiavito.presentation.main.filter.FilterActivity
import com.example.kinopoiskapiavito.presentation.main.network.NetworkService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    lateinit var mService: NetworkService
    lateinit var rvMovies: RecyclerView
    var movies: MutableList<Movie> = mutableListOf()
    var searchMovies: MutableList<Movie> = mutableListOf()
    lateinit var nothingTextView: TextView
    lateinit var pb: ProgressBar
    lateinit var nestedSV: NestedScrollView

    var page: Int = 1
    var searchPage: Int = 1
    var searching: String = ""
    var filtering = false
    lateinit var filteringParams: MutableMap<String, String>

    val dr = DataReceiver()



    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        mService = networkService

        nothingTextView = findViewById(R.id.nothing_found)
        nothingTextView.visibility = View.GONE

        pb = findViewById(R.id.progress_bar)

        dr.getAllMovies(page)

        nestedSV = findViewById(R.id.content_layout)
        nestedSV.setOnScrollChangeListener(object: NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight){
                    pb.visibility = View.VISIBLE
                    page++
                    if (filtering) dr.filterMovies()
                    else dr.getAllMovies(page)
                }
            }

        })

        val mStartForResult: ActivityResultLauncher<Intent> =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == RESULT_ACCESS){
                    filtering = true
                    page = 1
                    val result = it.data
                    if (result != null) {
                        filteringParams = mutableMapOf<String, String>()
                        val rating = result.getIntExtra("rating", -1)
                        val country = result.getStringExtra("country")
                        val year1 = result.getIntExtra("year1", -1)
                        val year2 = result.getIntExtra("year2", -1)
                        if (rating != -1) filteringParams.put("ageRating", "0" + (if (rating != 0) "-$rating" else ""))
                        if (country != null && country != "") filteringParams.put("countries.name", country)
                        if (year1 != -1 && year2 != -1) filteringParams.put("year", "$year1-$year2")
                        dr.filterMovies()
                    }
                }
                if (it.resultCode == RESULT_DENIED){
                    page = 1
                    filtering = false
                    dr.getAllMovies()
                }
            }

        val fab = findViewById<FloatingActionButton>(R.id.filterFAB)
        fab.setOnClickListener {
            val intent = Intent(this, FilterActivity::class.java)
            mStartForResult.launch(intent)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.actionSearch)
        val searchView: SearchView = searchItem.actionView as SearchView



        searchItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                nestedSV = findViewById(R.id.content_layout)
                nestedSV.setOnScrollChangeListener(object: NestedScrollView.OnScrollChangeListener{
                    override fun onScrollChange(
                        v: NestedScrollView,
                        scrollX: Int,
                        scrollY: Int,
                        oldScrollX: Int,
                        oldScrollY: Int
                    ) {
                        if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight){
                            pb.visibility = View.VISIBLE
                            dr.searchMoviesAdd(++searchPage, searching)
                        }
                    }

                })
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                (rvMovies.adapter as MovieAdapter).filterList(ArrayList(movies))
                filtering = false
                return true
            }

        })

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(msg: String?): Boolean {
                if (msg != null) {
                    searching = msg
                    dr.searchMovies(msg)
                }
                else nothingTextView.visibility = View.GONE
                return false
            }
            })
        return false
    }

    inner class DataReceiver {
        fun getAllMovies(p: Int = 1){

            mService.getMovies(page = p).enqueue(object: Callback<MovieList>{
                override fun onResponse(
                    call: Call<MovieList>,
                    response: Response<MovieList>
                ) {
                    if (response.isSuccessful && response != null){
                        if (page == 1) movies = (response.body() as MovieList).docs
                        else movies.addAll((response.body() as MovieList).docs)
                        //movies = movies.toSet().toMutableList()
                        rvMovies = findViewById<RecyclerView?>(R.id.recycler).apply {
                            adapter = MovieAdapter(this@MainActivity, movies)
                            layoutManager = LinearLayoutManager(this@MainActivity)
                            setHasFixedSize(true)
                        }
                    }
                }
                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    Log.i("JSON", "Error: " + t.message)
                }
            })
        }
        fun searchMoviesAdd(page: Int = 1, search: String){
            mService.searchMovies(page = page, startsWith = search).enqueue(object: Callback<MovieList>{
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.isSuccessful && response != null){
                        var filteredList = (response.body() as MovieList).docs
                        searchMovies.addAll(filteredList)
                        searchMovies.toSet().toMutableList()
                        (rvMovies.adapter as MovieAdapter).filterList(ArrayList(searchMovies))
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })

        }
        fun searchMovies(search: String){
            mService.searchMovies(page = 1, startsWith = search).enqueue(object: Callback<MovieList>{
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.isSuccessful && response != null){
                        var filteredList = (response.body() as MovieList).docs
                        searchMovies = filteredList
                        searchMovies.toSet().toMutableList()
                        (rvMovies.adapter as MovieAdapter).filterList(ArrayList(searchMovies))
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })

        }
        fun filterMovies(){
            mService.filterMovies(page = page.toString(), parameters = filteringParams).enqueue(object: Callback<MovieList>{
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.isSuccessful) {
                        var filterList = (response.body() as MovieList).docs
                        if (page == 1) movies = filterList
                        else movies.addAll(filterList)
                        (rvMovies.adapter as MovieAdapter).filterList(ArrayList(movies))
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}