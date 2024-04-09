package com.example.kinopoiskapiavito.presentation.main.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Networking {
    private const val NETWORK_CALL_TIMEOUT = 60L
    private var retrofit: Retrofit? = null
    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS)
        .connectTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS)
        .build()
    /*private val okHttpClient_add = OkHttpClient.Builder()
        .readTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS)
        .connectTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS)
        .build()*/

    fun create(baseUrl: String, forAdd: Boolean = false): Retrofit {
         retrofit =  Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        return retrofit!!
    }
}