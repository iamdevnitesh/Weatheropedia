package com.imcodernitesh.weatheropedia.api.forecast

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ForecastUtilities {

    fun getInstance(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}