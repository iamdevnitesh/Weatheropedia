package com.imcodernitesh.weatheropedia.viewmodel.airpollution

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.imcodernitesh.weatheropedia.repository.airquality.AirPollutionRepository
import com.imcodernitesh.weatheropedia.viewmodel.forecast.ForecastViewModel

class AirPollutionViewModelFactory(private var airPollutionRepository: AirPollutionRepository, private var latitude: Double, private var longitude: Double): ViewModelProvider.Factory {

    override fun<T: ViewModel> create(modelClass: Class<T>): T {
        return AirPollutionViewModel(airPollutionRepository,latitude,longitude) as T
    }
}