package com.imcodernitesh.weatheropedia.repository.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imcodernitesh.weatheropedia.api.forecast.ForecastDataClass
import com.imcodernitesh.weatheropedia.api.forecast.ForecastInterface

class ForecastRepository(private val forecastInterface: ForecastInterface) {
    private val forecastData = MutableLiveData<ForecastDataClass>()

    val forecast : LiveData<ForecastDataClass>
        get() = forecastData

    suspend fun getForecast(lat: Double, lon: Double) {
        val result = forecastInterface.getForecast(lat, lon)
        if(result.body()!=null){
            forecastData.postValue(result.body())
        }
    }
}