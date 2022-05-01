package com.imcodernitesh.weatheropedia.api.airquality

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AirPollutionInterface {
    @GET("air_pollution?appid=7840d820c5dabf3db20b9fcdd7455065")
    suspend fun getAirPollution(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
    ): Response<AirPollutionDataClass>
}