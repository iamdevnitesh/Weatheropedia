package com.imcodernitesh.weatheropedia.api.airquality

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AirPollutionUtilities {

    fun getInstance(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}