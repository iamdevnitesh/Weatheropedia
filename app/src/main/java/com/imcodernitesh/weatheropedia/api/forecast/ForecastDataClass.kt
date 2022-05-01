package com.imcodernitesh.weatheropedia.api.forecast

data class ForecastDataClass(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Forecast>,
    val message: Int
)