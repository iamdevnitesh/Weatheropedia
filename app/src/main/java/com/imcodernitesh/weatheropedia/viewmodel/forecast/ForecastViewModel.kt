package com.imcodernitesh.weatheropedia.viewmodel.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcodernitesh.weatheropedia.api.forecast.ForecastDataClass
import com.imcodernitesh.weatheropedia.repository.forecast.ForecastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForecastViewModel(private val forecastRepository: ForecastRepository,private val latitude: Double,private val longitude: Double) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO){
            forecastRepository.getForecast(latitude,longitude)
        }
    }

    val forecast: LiveData<ForecastDataClass>
        get() = forecastRepository.forecast

}