package com.imcodernitesh.weatheropedia.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.pm.PackageManager
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
import com.imcodernitesh.weatheropedia.api.forecast.ForecastInterface
import com.imcodernitesh.weatheropedia.api.forecast.ForecastUtilities
import com.imcodernitesh.weatheropedia.databinding.FragmentForecastBinding
import com.imcodernitesh.weatheropedia.datastore.ForecastDataStoreManager
import com.imcodernitesh.weatheropedia.datastore.WeatherDataStoreManager
import com.imcodernitesh.weatheropedia.repository.forecast.ForecastRepository
import com.imcodernitesh.weatheropedia.viewmodel.forecast.ForecastViewModel
import com.imcodernitesh.weatheropedia.viewmodel.forecast.ForecastViewModelFactory
import com.imcodernitesh.weatheropedia.viewmodel.weather.WeatherViewModel
import kotlinx.coroutines.launch

class ForecastFragment : Fragment() {
    private lateinit var binding: FragmentForecastBinding
    private var cancellationTokenSource = CancellationTokenSource()
    private lateinit var forecastDataManager: ForecastDataStoreManager
    private lateinit var weatherDataManager: WeatherDataStoreManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var viewmodel: ForecastViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForecastBinding.inflate(inflater, container, false)
        observeData()

        forecastDataManager = ForecastDataStoreManager(requireContext())
        weatherDataManager = WeatherDataStoreManager(requireContext())
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        binding.forecastSwipeRefreshLayout.setOnRefreshListener {
            requestCurrentLocation()
        }

        return binding.root
    }

    // checks live data changes and reflects them on the UI
    @SuppressLint("SetTextI18n")
    fun observeData() {
        forecastDataManager = ForecastDataStoreManager(requireContext())
        weatherDataManager = WeatherDataStoreManager(requireContext())
        // city name
        weatherDataManager.cityflow.asLiveData().observe(viewLifecycleOwner) { city ->
            city.let {
                binding.forecastCity.text = it
            }
        }
        // forecast temperature
        weatherDataManager.tempflow.asLiveData().observe(viewLifecycleOwner) { temp ->
            temp.let {
                binding.forecastTemperatureTextView.text = it
            }
        }
        // max temperature
        weatherDataManager.temp_maxflow.asLiveData().observe(viewLifecycleOwner) { maxTemp ->
            maxTemp.let {
                binding.maxminMaxTextView.text = it
            }
        }
        // min temperature
        weatherDataManager.temp_minflow.asLiveData().observe(viewLifecycleOwner) { minTemp ->
            minTemp.let {
                binding.maxminMinTextView.text = it
            }
        }
        // feels like
        weatherDataManager.feels_likeflow.asLiveData().observe(viewLifecycleOwner) { feelsLike ->
            feelsLike.let {
                binding.feelsLikeTextView.text = it
            }
        }
        // date1
        forecastDataManager.date1Flow.asLiveData().observe(viewLifecycleOwner) { date1 ->
            date1.let {
                binding.forecastDate1.text = it
            }
        }
        // date2
        forecastDataManager.date2Flow.asLiveData().observe(viewLifecycleOwner) { date2 ->
            date2.let {
                binding.forecastDate2.text = it
            }
        }
        // date3
        forecastDataManager.date3Flow.asLiveData().observe(viewLifecycleOwner) { date3 ->
            date3.let {
                binding.forecastDate3.text = it
            }
        }
        // date4
        forecastDataManager.date4Flow.asLiveData().observe(viewLifecycleOwner) { date4 ->
            date4.let {
                binding.forecastDate4.text = it
            }
        }
        // date5
        forecastDataManager.date5Flow.asLiveData().observe(viewLifecycleOwner) { date5 ->
            date5.let {
                binding.forecastDate5.text = it
            }
        }
        // image1
        forecastDataManager.img1Flow.asLiveData().observe(viewLifecycleOwner) { image1 ->
            image1.let {
                if (it == "01d") {
                    binding.forecastImage1.setImageResource(R.drawable.weather_sunny)
                } else if (it == "01n") {
                    binding.forecastImage1.setImageResource(R.drawable.weather_night)
                } else if (it == "02d") {
                    binding.forecastImage1.setImageResource(R.drawable.weather_partly_cloudy)
                } else if (it == "02n") {
                    binding.forecastImage1.setImageResource(R.drawable.weather_mostly_cloudy_night)
                } else if (it == "03d" || it == "03n") {
                    binding.forecastImage1.setImageResource(R.drawable.weather_mostly_cloudy)
                } else if (it == "04d" || it == "04n") {
                    binding.forecastImage1.setImageResource(R.drawable.weather_cloudy)
                } else if (it == "09d" || it == "09n") {
                    binding.forecastImage1.setImageResource(R.drawable.weather_drizzle)
                } else if (it == "10d") {
                    binding.forecastImage1.setImageResource(R.drawable.weather_drizzle_sunny)
                } else if (it == "10n") {
                    binding.forecastImage1.setImageResource(R.drawable.weather_drizzle_night)
                } else if (it == "11d" || it == "11n") {
                    binding.forecastImage1.setImageResource(R.drawable.weather_thunderstroms)
                } else if (it == "13d" || it == "13n") {
                    binding.forecastImage1.setImageResource(R.drawable.weather_snow)
                } else if (it == "50d" || it == "50n") {
                    binding.forecastImage1.setImageResource(R.drawable.weather_fog)
                }
            }
        }

        //image2
        forecastDataManager.img2Flow.asLiveData().observe(viewLifecycleOwner) { image2 ->
            image2.let {
                if (it == "01d") {
                    binding.forecastImage2.setImageResource(R.drawable.weather_sunny)
                } else if (it == "01n") {
                    binding.forecastImage2.setImageResource(R.drawable.weather_night)
                } else if (it == "02d") {
                    binding.forecastImage2.setImageResource(R.drawable.weather_partly_cloudy)
                } else if (it == "02n") {
                    binding.forecastImage2.setImageResource(R.drawable.weather_mostly_cloudy_night)
                } else if (it == "03d" || it == "03n") {
                    binding.forecastImage2.setImageResource(R.drawable.weather_mostly_cloudy)
                } else if (it == "04d" || it == "04n") {
                    binding.forecastImage2.setImageResource(R.drawable.weather_cloudy)
                } else if (it == "09d" || it == "09n") {
                    binding.forecastImage2.setImageResource(R.drawable.weather_drizzle)
                } else if (it == "10d") {
                    binding.forecastImage2.setImageResource(R.drawable.weather_drizzle_sunny)
                } else if (it == "10n") {
                    binding.forecastImage2.setImageResource(R.drawable.weather_drizzle_night)
                } else if (it == "11d" || it == "11n") {
                    binding.forecastImage2.setImageResource(R.drawable.weather_thunderstroms)
                } else if (it == "13d" || it == "13n") {
                    binding.forecastImage2.setImageResource(R.drawable.weather_snow)
                } else if (it == "50d" || it == "50n") {
                    binding.forecastImage2.setImageResource(R.drawable.weather_fog)
                }
            }
        }

        // image 3
        forecastDataManager.img3Flow.asLiveData().observe(viewLifecycleOwner) { image3 ->
            image3.let {
                if (it == "01d") {
                    binding.forecastImage3.setImageResource(R.drawable.weather_sunny)
                } else if (it == "01n") {
                    binding.forecastImage3.setImageResource(R.drawable.weather_night)
                } else if (it == "02d") {
                    binding.forecastImage3.setImageResource(R.drawable.weather_partly_cloudy)
                } else if (it == "02n") {
                    binding.forecastImage3.setImageResource(R.drawable.weather_mostly_cloudy_night)
                } else if (it == "03d" || it == "03n") {
                    binding.forecastImage3.setImageResource(R.drawable.weather_mostly_cloudy)
                } else if (it == "04d" || it == "04n") {
                    binding.forecastImage3.setImageResource(R.drawable.weather_cloudy)
                } else if (it == "09d" || it == "09n") {
                    binding.forecastImage3.setImageResource(R.drawable.weather_drizzle)
                } else if (it == "10d") {
                    binding.forecastImage3.setImageResource(R.drawable.weather_drizzle_sunny)
                } else if (it == "10n") {
                    binding.forecastImage3.setImageResource(R.drawable.weather_drizzle_night)
                } else if (it == "11d" || it == "11n") {
                    binding.forecastImage3.setImageResource(R.drawable.weather_thunderstroms)
                } else if (it == "13d" || it == "13n") {
                    binding.forecastImage3.setImageResource(R.drawable.weather_snow)
                } else if (it == "50d" || it == "50n") {
                    binding.forecastImage3.setImageResource(R.drawable.weather_fog)
                }
            }
        }

        // image 4
        forecastDataManager.img4Flow.asLiveData().observe(viewLifecycleOwner) { image4 ->
            image4.let {
                if (it == "01d") {
                    binding.forecastImage4.setImageResource(R.drawable.weather_sunny)
                } else if (it == "01n") {
                    binding.forecastImage4.setImageResource(R.drawable.weather_night)
                } else if (it == "02d") {
                    binding.forecastImage4.setImageResource(R.drawable.weather_partly_cloudy)
                } else if (it == "02n") {
                    binding.forecastImage4.setImageResource(R.drawable.weather_mostly_cloudy_night)
                } else if (it == "03d" || it == "03n") {
                    binding.forecastImage4.setImageResource(R.drawable.weather_mostly_cloudy)
                } else if (it == "04d" || it == "04n") {
                    binding.forecastImage4.setImageResource(R.drawable.weather_cloudy)
                } else if (it == "09d" || it == "09n") {
                    binding.forecastImage4.setImageResource(R.drawable.weather_drizzle)
                } else if (it == "10d") {
                    binding.forecastImage4.setImageResource(R.drawable.weather_drizzle_sunny)
                } else if (it == "10n") {
                    binding.forecastImage4.setImageResource(R.drawable.weather_drizzle_night)
                } else if (it == "11d" || it == "11n") {
                    binding.forecastImage4.setImageResource(R.drawable.weather_thunderstroms)
                } else if (it == "13d" || it == "13n") {
                    binding.forecastImage4.setImageResource(R.drawable.weather_snow)
                } else if (it == "50d" || it == "50n") {
                    binding.forecastImage4.setImageResource(R.drawable.weather_fog)
                }
            }
        }

        // image 5
        forecastDataManager.img5Flow.asLiveData().observe(viewLifecycleOwner) { image5 ->
            image5.let {
                if (it == "01d") {
                    binding.forecastImage5.setImageResource(R.drawable.weather_sunny)
                } else if (it == "01n") {
                    binding.forecastImage5.setImageResource(R.drawable.weather_night)
                } else if (it == "02d") {
                    binding.forecastImage5.setImageResource(R.drawable.weather_partly_cloudy)
                } else if (it == "02n") {
                    binding.forecastImage5.setImageResource(R.drawable.weather_mostly_cloudy_night)
                } else if (it == "03d" || it == "03n") {
                    binding.forecastImage5.setImageResource(R.drawable.weather_mostly_cloudy)
                } else if (it == "04d" || it == "04n") {
                    binding.forecastImage5.setImageResource(R.drawable.weather_cloudy)
                } else if (it == "09d" || it == "09n") {
                    binding.forecastImage5.setImageResource(R.drawable.weather_drizzle)
                } else if (it == "10d") {
                    binding.forecastImage5.setImageResource(R.drawable.weather_drizzle_sunny)
                } else if (it == "10n") {
                    binding.forecastImage5.setImageResource(R.drawable.weather_drizzle_night)
                } else if (it == "11d" || it == "11n") {
                    binding.forecastImage5.setImageResource(R.drawable.weather_thunderstroms)
                } else if (it == "13d" || it == "13n") {
                    binding.forecastImage5.setImageResource(R.drawable.weather_snow)
                } else if (it == "50d" || it == "50n") {
                    binding.forecastImage5.setImageResource(R.drawable.weather_fog)
                }
            }
        }

        // temp_max1
        forecastDataManager.temp_max1Flow.asLiveData().observe(viewLifecycleOwner) { tempMax1 ->
            tempMax1.let {
                binding.forecastMax1.text = "Max:$it"
            }
        }
        // temp_min1
        forecastDataManager.temp_min1Flow.asLiveData().observe(viewLifecycleOwner) { tempMin1 ->
            tempMin1.let {
                binding.forecastMin1.text = "Min:$it"
            }
        }
        // temp_max2
        forecastDataManager.temp_max2Flow.asLiveData().observe(viewLifecycleOwner) { tempMax2 ->
            tempMax2.let {
                binding.forecastMax2.text = "Max:$it"
            }
        }
        // temp_min2
        forecastDataManager.temp_min2Flow.asLiveData().observe(viewLifecycleOwner) { tempMin2 ->
            tempMin2.let {
                binding.forecastMin2.text = "Min:$it"
            }
        }
        // temp_max3
        forecastDataManager.temp_max3Flow.asLiveData().observe(viewLifecycleOwner) { tempMax3 ->
            tempMax3.let {
                binding.forecastMax3.text = "Max:$it"
            }
        }
        // temp_min3
        forecastDataManager.temp_min3Flow.asLiveData().observe(viewLifecycleOwner) { tempMin3 ->
            tempMin3.let {
                binding.forecastMin3.text = "Min:$it"
            }
        }
        // temp_max4
        forecastDataManager.temp_max4Flow.asLiveData().observe(viewLifecycleOwner) { tempMax4 ->
            tempMax4.let {
                binding.forecastMax4.text = "Max:$it"
            }
        }
        // temp_min4
        forecastDataManager.temp_min4Flow.asLiveData().observe(viewLifecycleOwner) { tempMin4 ->
            tempMin4.let {
                binding.forecastMin4.text = "Min:$it"
            }
        }
        // temp_max5
        forecastDataManager.temp_max5Flow.asLiveData().observe(viewLifecycleOwner) { tempMax5 ->
            tempMax5.let {
                binding.forecastMax5.text = "Max:$it"
            }
        }
        // temp_min5
        forecastDataManager.temp_min5Flow.asLiveData().observe(viewLifecycleOwner) { tempMin5 ->
            tempMin5.let {
                binding.forecastMin5.text = "Min:$it"
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
                    val forecastInterface =
                        ForecastUtilities.getInstance().create(ForecastInterface::class.java)
                    val forecastRepository = ForecastRepository(forecastInterface)
                    viewmodel = ViewModelProvider(
                        this,
                        ForecastViewModelFactory(
                            forecastRepository,
                            result.latitude,
                            result.longitude
                        )
                    )[ForecastViewModel::class.java]
                    viewmodel.forecast.observe(requireActivity()) {
                        lifecycleScope.launch {
                            forecastDataManager.storeForecastData(
                                it.list[0].dt_txt,
                                it.list[8].dt_txt,
                                it.list[16].dt_txt,
                                it.list[24].dt_txt,
                                it.list[32].dt_txt,
                                it.list[0].main.temp_max.toString(),
                                it.list[0].main.temp_min.toString(),
                                it.list[8].main.temp_max.toString(),
                                it.list[8].main.temp_min.toString(),
                                it.list[16].main.temp_max.toString(),
                                it.list[16].main.temp_min.toString(),
                                it.list[24].main.temp_max.toString(),
                                it.list[24].main.temp_min.toString(),
                                it.list[32].main.temp_max.toString(),
                                it.list[32].main.temp_min.toString(),
                                it.list[0].weather[0].icon.toString(),
                                it.list[8].weather[0].icon.toString(),
                                it.list[16].weather[0].icon.toString(),
                                it.list[24].weather[0].icon.toString(),
                                it.list[32].weather[0].icon.toString()
                            )
                        }
                        binding.forecastSwipeRefreshLayout.isRefreshing = false
                    }
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
}