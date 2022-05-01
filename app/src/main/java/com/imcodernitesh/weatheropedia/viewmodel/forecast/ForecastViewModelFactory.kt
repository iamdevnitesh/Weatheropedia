package com.imcodernitesh.weatheropedia.viewmodel.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.imcodernitesh.weatheropedia.repository.forecast.ForecastRepository

class ForecastViewModelFactory(private var forecastRepository: ForecastRepository,private var latitude: Double,private var longitude: Double):ViewModelProvider.Factory {

    override fun<T: ViewModel> create(modelClass: Class<T>): T {
        return ForecastViewModel(forecastRepository,latitude,longitude) as T
    }
}