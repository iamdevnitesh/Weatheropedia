package com.imcodernitesh.weatheropedia.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "sharedpreferences")
class WeatherDataStoreManager(context: Context) {

    // datastore creation
    private val mDataStore: DataStore<Preferences> = context.datastore

    companion object{
        val main_key = stringPreferencesKey("main")
        val description_key = stringPreferencesKey("description")
        val icon_key = stringPreferencesKey("icon")
        val temp_key = stringPreferencesKey("temp")
        val feels_like_key = stringPreferencesKey("feels_like")
        val temp_min_key = stringPreferencesKey("temp_min")
        val temp_max_key = stringPreferencesKey("temp_max")
        val pressure_key = stringPreferencesKey("pressure")
        val humidity_key = stringPreferencesKey("humidity")
        val sea_level_key = stringPreferencesKey("sea_level")
        val grnd_level_key = stringPreferencesKey("grnd_level")
        val visibilty_key = stringPreferencesKey("visibilty")
        val wind_speed_key = stringPreferencesKey("wind_speed")
        val wind_deg_key = stringPreferencesKey("wind_deg")
        val wind_gust_key = stringPreferencesKey("wind_gust")
        val country_key = stringPreferencesKey("country")
        val city_key = stringPreferencesKey("city")

    }


    suspend fun storeWeatherData(main:String,description:String,icon:String,temp:Double,feels_like:Double,temp_min:Double,temp_max:Double,pressure:Int,humidity:Int,sea_level:Int,grnd_level:Int,visibility:Int,wind_speed:Double,wind_deg:Int,wind_gust:Double,country:String,city:String){
        mDataStore.edit { preferences->
            preferences[main_key] = main
            preferences[description_key] = description
            preferences[icon_key] = icon
            preferences[temp_key] = temp.toString()
            preferences[feels_like_key] = feels_like.toString()
            preferences[temp_min_key] = temp_min.toString()
            preferences[temp_max_key] = temp_max.toString()
            preferences[pressure_key] = pressure.toString()
            preferences[humidity_key] = humidity.toString()
            preferences[sea_level_key] = sea_level.toString()
            preferences[grnd_level_key] = grnd_level.toString()
            preferences[visibilty_key] = visibility.toString()
            preferences[wind_speed_key] = wind_speed.toString()
            preferences[wind_deg_key] = wind_deg.toString()
            preferences[wind_gust_key] = wind_gust.toString()
            preferences[country_key] = country
            preferences[city_key] = city
        }
    }

    suspend fun storelocality(locality:String){
        mDataStore.edit { preferences->
            preferences[city_key] = locality
        }
    }

    // create flow for each types
    val mainflow: Flow<String> = mDataStore.data.map { it[main_key] ?: "" }
    val descriptionflow: Flow<String> = mDataStore.data.map { it[description_key] ?: "" }
    val iconflow: Flow<String> = mDataStore.data.map { it[icon_key] ?: "" }
    val tempflow: Flow<String> = mDataStore.data.map { it[temp_key] ?: "" }
    val feels_likeflow: Flow<String> = mDataStore.data.map { it[feels_like_key] ?: "" }
    val temp_minflow: Flow<String> = mDataStore.data.map { it[temp_min_key] ?: "" }
    val temp_maxflow: Flow<String> = mDataStore.data.map { it[temp_max_key] ?: "" }
    val pressureflow: Flow<String> = mDataStore.data.map { it[pressure_key] ?: "" }
    val humidityflow: Flow<String> = mDataStore.data.map { it[humidity_key] ?: "" }
    val sea_levelflow: Flow<String> = mDataStore.data.map { it[sea_level_key] ?: "" }
    val grnd_levelflow: Flow<String> = mDataStore.data.map { it[grnd_level_key] ?: "" }
    val visibilityflow: Flow<String> = mDataStore.data.map { it[visibilty_key] ?: "" }
    val wind_speedflow: Flow<String> = mDataStore.data.map { it[wind_speed_key] ?: "" }
    val wind_degflow: Flow<String> = mDataStore.data.map { it[wind_deg_key] ?: "" }
    val wind_gustflow: Flow<String> = mDataStore.data.map { it[wind_gust_key] ?: "" }
    val countryflow: Flow<String> = mDataStore.data.map { it[country_key] ?: "" }
    val cityflow: Flow<String> = mDataStore.data.map { it[city_key] ?: "" }

}


