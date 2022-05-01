package com.imcodernitesh.weatheropedia.viewmodel.airpollution

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcodernitesh.weatheropedia.api.airquality.AirPollutionDataClass
import com.imcodernitesh.weatheropedia.repository.airquality.AirPollutionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AirPollutionViewModel(private val airPollutionRepository: AirPollutionRepository,private val latitude: Double,private val longitude: Double): ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO){
            airPollutionRepository.getAirPollution(latitude,longitude)
        }
    }

    val airPollution:  LiveData<AirPollutionDataClass>
        get() = airPollutionRepository.airpollution

}