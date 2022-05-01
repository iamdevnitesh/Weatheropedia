package com.imcodernitesh.weatheropedia.viewmodel.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcodernitesh.weatheropedia.api.weather.WeatherDataClass
import com.imcodernitesh.weatheropedia.repository.weather.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(private val weatherRepository: WeatherRepository,private val locality:String) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getWeather(locality)
        }
    }

    val weather: LiveData<WeatherDataClass>
        get() = weatherRepository.weather
}