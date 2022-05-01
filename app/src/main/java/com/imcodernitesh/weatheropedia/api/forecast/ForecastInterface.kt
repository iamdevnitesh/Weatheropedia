package com.imcodernitesh.weatheropedia.api.forecast

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastInterface {
    @GET("forecast?appid=7840d820c5dabf3db20b9fcdd7455065&units=metric")
    suspend fun getForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
    ): Response<ForecastDataClass>
}