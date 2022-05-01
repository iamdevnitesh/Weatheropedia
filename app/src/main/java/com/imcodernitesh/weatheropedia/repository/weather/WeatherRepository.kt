package com.imcodernitesh.weatheropedia.repository.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imcodernitesh.weatheropedia.api.weather.WeatherDataClass
import com.imcodernitesh.weatheropedia.api.weather.WeatherInterface

class WeatherRepository(private val weatherInterface: WeatherInterface) {
    private val weatherData = MutableLiveData<WeatherDataClass>()

    val weather : LiveData<WeatherDataClass>
        get() = weatherData

    suspend fun getWeather(locality: String){
        val result = weatherInterface.getData(locality)
        if(result.body()!=null){
            weatherData.postValue(result.body())
        }
    }

}