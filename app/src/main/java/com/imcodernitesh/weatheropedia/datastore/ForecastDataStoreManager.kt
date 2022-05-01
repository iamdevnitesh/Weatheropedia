package com.imcodernitesh.weatheropedia.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "forecast-sharedprefences")
class ForecastDataStoreManager(context: Context) {

    // data store creation
    private val mDataStore: DataStore<Preferences> = context.datastore

    companion object{
        val date_1 = stringPreferencesKey("date1")
        val date_2 = stringPreferencesKey("date2")
        val date_3 = stringPreferencesKey("date3")
        val date_4 = stringPreferencesKey("date4")
        val date_5 = stringPreferencesKey("date5")
        val tempmax1 = stringPreferencesKey("tempmax1")
        val tempmin1 = stringPreferencesKey("tempmin1")
        val tempmax2 = stringPreferencesKey("tempmax2")
        val tempmin2 = stringPreferencesKey("tempmin2")
        val tempmax3 = stringPreferencesKey("tempmax3")
        val tempmin3 = stringPreferencesKey("tempmin3")
        val tempmax4 = stringPreferencesKey("tempmax4")
        val tempmin4 = stringPreferencesKey("tempmin4")
        val tempmax5 = stringPreferencesKey("tempmax5")
        val tempmin5 = stringPreferencesKey("tempmin5")
        val img1 = stringPreferencesKey("img1")
        val img2 = stringPreferencesKey("img2")
        val img3 = stringPreferencesKey("img3")
        val img4 = stringPreferencesKey("img4")
        val img5 = stringPreferencesKey("img5")
    }

    suspend fun storeForecastData(date1:String, date2:String, date3:String, date4:String, date5:String, temp_max1:String, temp_min1:String, temp_max2:String, temp_min2:String, temp_max3:String, temp_min3:String, temp_max4:String, temp_min4:String, temp_max5:String, temp_min5:String, image1:String, image2:String, image3:String, image4:String, image5:String){
        mDataStore.edit { preferences ->

            preferences[date_1] = date1
            preferences[date_2] = date2
            preferences[date_3] = date3
            preferences[date_4] = date4
            preferences[date_5] = date5
            preferences[tempmax1] = temp_max1
            preferences[tempmin1] = temp_min1
            preferences[tempmax2] = temp_max2
            preferences[tempmin2] = temp_min2
            preferences[tempmax3] = temp_max3
            preferences[tempmin3] = temp_min3
            preferences[tempmax4] = temp_max4
            preferences[tempmin4] = temp_min4
            preferences[tempmax5] = temp_max5
            preferences[tempmin5] = temp_min5
            preferences[img1] = image1
            preferences[img2] = image2
            preferences[img3] = image3
            preferences[img4] = image4
            preferences[img5] = image5
        }
    }

    // create flow for each type
    val date1Flow: Flow<String> = mDataStore.data.map { it[date_1] ?: "" }
    val date2Flow: Flow<String> = mDataStore.data.map { it[date_2] ?: "" }
    val date3Flow: Flow<String> = mDataStore.data.map { it[date_3] ?: "" }
    val date4Flow: Flow<String> = mDataStore.data.map { it[date_4] ?: "" }
    val date5Flow: Flow<String> = mDataStore.data.map { it[date_5] ?: "" }
    val temp_max1Flow: Flow<String> = mDataStore.data.map { it[tempmax1] ?: "" }
    val temp_min1Flow: Flow<String> = mDataStore.data.map { it[tempmin1] ?: "" }
    val temp_max2Flow: Flow<String> = mDataStore.data.map { it[tempmax2] ?: "" }
    val temp_min2Flow: Flow<String> = mDataStore.data.map { it[tempmin2] ?: "" }
    val temp_max3Flow: Flow<String> = mDataStore.data.map { it[tempmax3] ?: "" }
    val temp_min3Flow: Flow<String> = mDataStore.data.map { it[tempmin3] ?: "" }
    val temp_max4Flow: Flow<String> = mDataStore.data.map { it[tempmax4] ?: "" }
    val temp_min4Flow: Flow<String> = mDataStore.data.map { it[tempmin4] ?: "" }
    val temp_max5Flow: Flow<String> = mDataStore.data.map { it[tempmax5] ?: "" }
    val temp_min5Flow: Flow<String> = mDataStore.data.map { it[tempmin5] ?: "" }
    val img1Flow: Flow<String> = mDataStore.data.map { it[img1] ?: "" }
    val img2Flow: Flow<String> = mDataStore.data.map { it[img2] ?: "" }
    val img3Flow: Flow<String> = mDataStore.data.map { it[img3] ?: "" }
    val img4Flow: Flow<String> = mDataStore.data.map { it[img4] ?: "" }
    val img5Flow: Flow<String> = mDataStore.data.map { it[img5] ?: "" }
}