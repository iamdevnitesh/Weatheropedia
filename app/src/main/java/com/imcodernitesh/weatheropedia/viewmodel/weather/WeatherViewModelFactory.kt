package com.imcodernitesh.weatheropedia.viewmodel.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.imcodernitesh.weatheropedia.repository.weather.WeatherRepository

class WeatherViewModelFactory(private var weatherRepository: WeatherRepository,private var locality:String):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return WeatherViewModel(weatherRepository,locality) as T
    }
}