package com.imcodernitesh.weatheropedia.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.datastore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = "airquality_preferences")
class AirPollutionDataStoreManager(context: Context) {

    // datastore creation
    private val mDataStore: DataStore<Preferences> = context.datastore

    companion object{
        val aqi = stringPreferencesKey("AQI")
        val co = stringPreferencesKey("CO")
        val no = stringPreferencesKey("NO")
        val no2 = stringPreferencesKey("NO2")
        val o3 = stringPreferencesKey("O3")
        val so2 = stringPreferencesKey("SO2")
        val pm25 = stringPreferencesKey("PM25")
        val pm10 = stringPreferencesKey("PM10")
        val nh3 = stringPreferencesKey("NH3")
    }

    suspend fun storeAirPollution(airqualityIndex: String, CO: String,NO: String, NO2: String, O3: String, SO2: String, PM25: String, PM10: String, NH3: String) {
        mDataStore.edit {
            it[aqi] = airqualityIndex
            it[co] = CO
            it[no] = NO
            it[no2] = NO2
            it[o3] = O3
            it[so2] = SO2
            it[pm25] = PM25
            it[pm10] = PM10
            it[nh3] = NH3
        }
    }

    val aquiflow: Flow<String> = mDataStore.data.map { it[aqi] ?: "" }
    val coflow: Flow<String> = mDataStore.data.map { it[co] ?: "" }
    val noflow: Flow<String> = mDataStore.data.map { it[no] ?: "" }
    val no2flow: Flow<String> = mDataStore.data.map { it[no2] ?: "" }
    val o3flow: Flow<String> = mDataStore.data.map { it[o3] ?: "" }
    val so2flow: Flow<String> = mDataStore.data.map { it[so2] ?: "" }
    val pm25flow: Flow<String> = mDataStore.data.map { it[pm25] ?: "" }
    val pm10flow: Flow<String> = mDataStore.data.map { it[pm10] ?: "" }
    val nh3flow: Flow<String> = mDataStore.data.map { it[nh3] ?: "" }
}