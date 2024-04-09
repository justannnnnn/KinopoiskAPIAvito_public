package com.example.kinopoiskapiavito.presentation.main.network

import com.example.kinopoiskapiavito.Constants
import com.example.kinopoiskapiavito.Constants.X_API_KEY
import com.example.kinopoiskapiavito.Constants.accept
import com.example.kinopoiskapiavito.model.Country
import com.example.kinopoiskapiavito.model.Movie
import com.example.kinopoiskapiavito.model.MovieList
import com.example.kinopoiskapiavito.model.PosterList
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import java.lang.reflect.Parameter

interface NetworkService {

    @GET("movie")
    fun getMovies(
        @Header("X-API-KEY") key: String = X_API_KEY,
        @Header("accept") accept: String = Constants.accept,
        @Query("page") page: Int = 1,
        @Query("query") startsWith: String = ""
    ): Call<MovieList>

    @GET("movie/search")
    fun searchMovies(
        @Header("X-API-KEY") key: String = X_API_KEY,
        @Header("accept") accept: String = Constants.accept,
        @Query("page") page: Int = 1,
        @Query("query") startsWith: String = ""
    ): Call<MovieList>

    @GET("movie/possible-values-by-field")
    fun getCountries(
        @Header("X-API-KEY") key: String = X_API_KEY,
        @Header("accept") accept: String = Constants.accept,
        @Query("field") field: String = "countries.name"
    ): Call<List<Country>>

    @GET("movie")
    fun filterMovies(
        @Header("X-API-KEY") key: String = X_API_KEY,
        @Header("accept") accept: String = Constants.accept,
        @Query("page") page: String,
        @QueryMap parameters: MutableMap<String, String>
    ): Call<MovieList>

    @GET("movie/{id}")
    fun getMovieInfo(
        @Header("X-API-KEY") key: String = X_API_KEY,
        @Header("accept") accept: String = Constants.accept,
        @Path("id") id: String
    ): Call<Movie>

    @GET("image")
    fun getPosters(
        @Header("X-API-KEY") key: String = X_API_KEY,
        @Header("accept") accept: String = Constants.accept,
        @Query("movieId") id: Int
    ): Call<PosterList>

}