package com.imcodernitesh.weatheropedia.fragments

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.datastore.dataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.imcodernitesh.weatheropedia.R
import com.imcodernitesh.weatheropedia.api.airquality.AirPollutionInterface
import com.imcodernitesh.weatheropedia.api.airquality.AirPollutionUtilities
import com.imcodernitesh.weatheropedia.api.forecast.ForecastInterface
import com.imcodernitesh.weatheropedia.api.forecast.ForecastUtilities
import com.imcodernitesh.weatheropedia.databinding.FragmentSplashBinding
import com.imcodernitesh.weatheropedia.datastore.AirPollutionDataStoreManager
import com.imcodernitesh.weatheropedia.datastore.ForecastDataStoreManager
import com.imcodernitesh.weatheropedia.datastore.WeatherDataStoreManager
import com.imcodernitesh.weatheropedia.repository.airquality.AirPollutionRepository
import com.imcodernitesh.weatheropedia.repository.forecast.ForecastRepository
import com.imcodernitesh.weatheropedia.viewmodel.airpollution.AirPollutionViewModel
import com.imcodernitesh.weatheropedia.viewmodel.airpollution.AirPollutionViewModelFactory
import com.imcodernitesh.weatheropedia.viewmodel.forecast.ForecastViewModel
import com.imcodernitesh.weatheropedia.viewmodel.forecast.ForecastViewModelFactory
import kotlinx.coroutines.*
import java.util.*

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var weatherdataManager: WeatherDataStoreManager
    private var cancellationTokenSource = CancellationTokenSource()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            FragmentSplashBinding.bind(inflater.inflate(R.layout.fragment_splash, container, false))
        weatherdataManager = WeatherDataStoreManager(requireContext())
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        CoroutineScope(Dispatchers.IO).launch {
            requestCurrentLocation()
        }
        return binding.root
    }

    // Get location and city name
    private fun requestCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {

            val currentLocationTask: Task<Location> =
                fusedLocationProviderClient.getCurrentLocation(
                    PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                )

            currentLocationTask.addOnCompleteListener { task: Task<Location> ->
                val result = if (task.isSuccessful) {
                    val result: Location = task.result

                    getCity(result.latitude, result.longitude)
                    "Location (success): ${result.latitude}, ${result.longitude}"
                } else {
                    val exception = task.exception
                    "Location (failure): $exception"
                }

                Log.d(TAG, "getCurrentLocation() result: $result")
            }
        } else {
            // Request fine location permission (full code below).

        }
    }

    override fun onStop() {
        super.onStop()
        cancellationTokenSource.cancel()
    }

    private fun getCity(lat: Double, lng: Double) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(lat, lng, 1)
        lifecycleScope.launch(Dispatchers.IO) {
            weatherdataManager.storelocality(addresses[0].locality)
        }
        Log.d(
            TAG,
            "City Name fetched Complete. City = ${addresses[0].locality}"
        ) //addresses.get(0).getLocality() can also be used
        findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
    }
}