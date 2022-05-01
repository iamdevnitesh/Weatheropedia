package com.imcodernitesh.weatheropedia.repository.airquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imcodernitesh.weatheropedia.api.airquality.AirPollutionDataClass
import com.imcodernitesh.weatheropedia.api.airquality.AirPollutionInterface

class AirPollutionRepository(private val airPollutionInterface: AirPollutionInterface) {
    private val airPollutionData = MutableLiveData<AirPollutionDataClass>()

    val airpollution: LiveData<AirPollutionDataClass>
        get() = airPollutionData

    suspend fun getAirPollution(lat: Double, lon: Double){
        val result = airPollutionInterface.getAirPollution(lat, lon)
        if(result.body()!=null){
            airPollutionData.postValue(result.body())
        }
    }
}