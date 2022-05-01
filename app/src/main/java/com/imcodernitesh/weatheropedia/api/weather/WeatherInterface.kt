package com.imcodernitesh.weatheropedia.api.weather

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInterface {
    @GET( "weather?APPID=7840d820c5dabf3db20b9fcdd7455065&units=metric&")
    suspend fun getData(
        @Query("q") localityName: String // this here adds q=localityName at the end of our URL
    ): Response<WeatherDataClass>
}