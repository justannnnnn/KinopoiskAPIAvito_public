package com.example.kinopoiskapiavito

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.kinopoiskapiavito.presentation.main.network.NetworkService
import com.example.kinopoiskapiavito.presentation.main.network.Networking
import java.time.LocalDate

object Constants {
    private val BASE_URL = "https://api.kinopoisk.dev/v1.4/"
    private val BASE_URL_add = "https://api.kinopoisk.dev/v1/"
    //TODO: hide
    val X_API_KEY = "WF76VQQ-HQB4P5G-JFJH8DF-CRKDP1M"
    val accept = "application/json"

    val networkService: NetworkService
        get() = Networking.create(BASE_URL).create(NetworkService::class.java)
    val networkServiceAdd: NetworkService
        get() = Networking.create(BASE_URL_add, forAdd = true).create(NetworkService::class.java)

    val RESULT_ACCESS = 1
    val RESULT_DENIED = -1

    //filter constants
    var rb_default: Int? = null
    var atv_default: String = ""
    @RequiresApi(Build.VERSION_CODES.O)
    var y1_default: Int? = null
    @RequiresApi(Build.VERSION_CODES.O)
    var y2_default: Int? = null
}