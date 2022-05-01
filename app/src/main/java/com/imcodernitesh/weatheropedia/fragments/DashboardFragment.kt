package com.imcodernitesh.weatheropedia.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.imcodernitesh.weatheropedia.R
import com.imcodernitesh.weatheropedia.api.weather.WeatherInterface
import com.imcodernitesh.weatheropedia.api.weather.WeatherUtilities
import com.imcodernitesh.weatheropedia.databinding.FragmentDashboardBinding
import com.imcodernitesh.weatheropedia.datastore.WeatherDataStoreManager
import com.imcodernitesh.weatheropedia.repository.weather.WeatherRepository
import com.imcodernitesh.weatheropedia.viewmodel.weather.WeatherViewModel
import com.imcodernitesh.weatheropedia.viewmodel.weather.WeatherViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private var cancellationTokenSource = CancellationTokenSource()
    private lateinit var weatherDataManager: WeatherDataStoreManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var viewmodel: WeatherViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        observeData()

        weatherDataManager = WeatherDataStoreManager(requireContext())
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.dashboardSwipeRefreshLayout.setOnRefreshListener {
            requestCurrentLocation()
        }
        return binding.root
    }

    // checks live data changes and reflects them on the UI
    @SuppressLint("SetTextI18n")
    fun observeData() {
        weatherDataManager = WeatherDataStoreManager(requireContext())
        // city name
        weatherDataManager.cityflow.asLiveData().observe(viewLifecycleOwner) { locality ->
            locality.let {
                binding.dashboardCityName.text = it
            }
        }
        // temperature
        weatherDataManager.tempflow.asLiveData().observe(viewLifecycleOwner) { temperature ->
            temperature.let {
                binding.weatherTemperatureTextView.text = it
            }
        }
        // weather description
        weatherDataManager.descriptionflow.asLiveData().observe(viewLifecycleOwner) { description ->
            description.let {
                binding.weatherDesc.text = it
            }
        }
        // temp max
        weatherDataManager.temp_maxflow.asLiveData().observe(viewLifecycleOwner) { temp_max ->
            temp_max.let {
                binding.maxminMaxTextView.text = it
            }
        }
        // temp min
        weatherDataManager.temp_minflow.asLiveData().observe(viewLifecycleOwner) { temp_min ->
            temp_min.let {
                binding.maxminMinTextView.text = it
            }
        }
        // feels like
        weatherDataManager.feels_likeflow.asLiveData().observe(viewLifecycleOwner) { feels_like ->
            feels_like.let {
                binding.feelsLikeTextView.text = "Feels like :$it°C"
            }
        }
        // humidity
        weatherDataManager.humidityflow.asLiveData().observe(viewLifecycleOwner) { humidity ->
            humidity.let {
                binding.weatherHumidity.text = "Humidity :$it%"
            }
        }
        // wind
        weatherDataManager.wind_speedflow.asLiveData().observe(viewLifecycleOwner) { wind ->
            wind.let {
                binding.weatherWind.text = "Wind :$it m/s"
            }
        }
        // pressure
        weatherDataManager.pressureflow.asLiveData().observe(viewLifecycleOwner) { pressure ->
            pressure.let {
                binding.weatherPressure.text = "Pressure :$it hPa"
            }
        }
        // visibility
        weatherDataManager.visibilityflow.asLiveData().observe(viewLifecycleOwner) { visibility ->
            visibility.let {
                binding.weatherVisibility.text = "Visibility :$it m"
            }
        }
        // sealevel
        weatherDataManager.sea_levelflow.asLiveData().observe(viewLifecycleOwner) { sea_level ->
            sea_level.let {
                binding.weatherSealevel.text = "Sea Level :$it hPa"
            }
        }
        // ground level
        weatherDataManager.grnd_levelflow.asLiveData().observe(viewLifecycleOwner) { ground_level ->
            ground_level.let {
                binding.weatherGrndlevel.text = "Ground Level :$it hPa"
            }
        }
        // icon
        weatherDataManager.iconflow.asLiveData().observe(viewLifecycleOwner) { image ->
            image.let {
                if (it == "01d") {
                    binding.basicWeatherIcon.setImageResource(R.drawable.weather_sunny)
                } else if (it == "01n") {
                    binding.basicWeatherIcon.setImageResource(R.drawable.weather_night)
                } else if (it == "02d") {
                    binding.basicWeatherIcon.setImageResource(R.drawable.weather_partly_cloudy)
                } else if (it == "02n") {
                    binding.basicWeatherIcon.setImageResource(R.drawable.weather_mostly_cloudy_night)
                } else if (it == "03d" || it == "03n") {
                    binding.basicWeatherIcon.setImageResource(R.drawable.weather_mostly_cloudy)
                } else if (it == "04d" || it == "04n") {
                    binding.basicWeatherIcon.setImageResource(R.drawable.weather_cloudy)
                } else if (it == "09d" || it == "09n") {
                    binding.basicWeatherIcon.setImageResource(R.drawable.weather_drizzle)
                } else if (it == "10d") {
                    binding.basicWeatherIcon.setImageResource(R.drawable.weather_drizzle_sunny)
                } else if (it == "10n") {
                    binding.basicWeatherIcon.setImageResource(R.drawable.weather_drizzle_night)
                } else if (it == "11d" || it == "11n") {
                    binding.basicWeatherIcon.setImageResource(R.drawable.weather_thunderstroms)
                } else if (it == "13d" || it == "13n") {
                    binding.basicWeatherIcon.setImageResource(R.drawable.weather_snow)
                } else if (it == "50d" || it == "50n") {
                    binding.basicWeatherIcon.setImageResource(R.drawable.weather_fog)
                }
            }
        }
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
                    LocationRequest.PRIORITY_HIGH_ACCURACY,
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

                Log.d(ContentValues.TAG, "getCurrentLocation() result: $result")
            }
        } else {
            // will not be needing this part since we already handled with permission management
        }
    }

    override fun onStop() {
        super.onStop()
        cancellationTokenSource.cancel()
    }

    private fun getCity(lat: Double, lng: Double) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(lat, lng, 1)
        val weatherInterface =
            WeatherUtilities.getInstance().create(WeatherInterface::class.java)
        val weatherRepository = WeatherRepository(weatherInterface)
        viewmodel = ViewModelProvider(
            this,
            WeatherViewModelFactory(weatherRepository, addresses[0].locality)
        )[WeatherViewModel::class.java]
        viewmodel.weather.observe(requireActivity()) {
            lifecycleScope.launch {
                weatherDataManager.storeWeatherData(
                    it.weather[0].main,
                    it.weather[0].description,
                    it.weather[0].icon,
                    it.main.temp,
                    it.main.feels_like,
                    it.main.temp_min,
                    it.main.temp_max,
                    it.main.pressure,
                    it.main.humidity,
                    it.main.sea_level,
                    it.main.grnd_level,
                    it.visibility,
                    it.wind.speed,
                    it.wind.deg,
                    it.wind.gust,
                    it.sys.country,
                    it.name
                )
            }
            binding.dashboardSwipeRefreshLayout.isRefreshing = false
        }
        lifecycleScope.launch(Dispatchers.IO) {
            weatherDataManager.storelocality(addresses[0].locality)
        }
        binding.dashboardSwipeRefreshLayout.isRefreshing = false
    }

}