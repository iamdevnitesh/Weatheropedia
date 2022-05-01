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
import com.imcodernitesh.weatheropedia.api.airquality.AirPollutionInterface
import com.imcodernitesh.weatheropedia.api.airquality.AirPollutionUtilities
import com.imcodernitesh.weatheropedia.databinding.FragmentAirPollutionBinding
import com.imcodernitesh.weatheropedia.datastore.AirPollutionDataStoreManager
import com.imcodernitesh.weatheropedia.repository.airquality.AirPollutionRepository
import com.imcodernitesh.weatheropedia.viewmodel.airpollution.AirPollutionViewModel
import com.imcodernitesh.weatheropedia.viewmodel.airpollution.AirPollutionViewModelFactory
import kotlinx.coroutines.launch

class AirPollutionFragment : Fragment() {
    private lateinit var binding: FragmentAirPollutionBinding
    private var cancellationTokenSource = CancellationTokenSource()
    private lateinit var airpollutionDataManager: AirPollutionDataStoreManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var viewmodel: AirPollutionViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAirPollutionBinding.inflate(inflater, container, false)
        observeData()

        airpollutionDataManager = AirPollutionDataStoreManager(requireContext())
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        binding.airPollutionSwipeRefereshLayout.setOnRefreshListener {
            requestCurrentLocation()
        }


        return binding.root
    }


    @SuppressLint("SetTextI18n")
    fun observeData() {
        airpollutionDataManager = AirPollutionDataStoreManager(requireContext())

        // airquality
        airpollutionDataManager.aquiflow.asLiveData().observe(viewLifecycleOwner) { aqi ->
            aqi.let {
                binding.airqualitytext.text = "AirQualityIndex : $it"
            }
        }

        // co
        airpollutionDataManager.coflow.asLiveData().observe(viewLifecycleOwner) { co ->
            co.let {
                binding.coText.text = "$it μg/m3"
            }
        }

        //  no
        airpollutionDataManager.noflow.asLiveData().observe(viewLifecycleOwner) { no ->
            no.let {
                binding.noText.text = "$it μg/m3"
            }
        }

        // no2
        airpollutionDataManager.no2flow.asLiveData().observe(viewLifecycleOwner) { no2 ->
            no2.let {
                binding.no2Text.text = "$it μg/m3"
            }
        }

        // o3
        airpollutionDataManager.o3flow.asLiveData().observe(viewLifecycleOwner) { o3 ->
            o3.let {
                binding.o3Text.text = "$it μg/m3"
            }
        }

        // so2
        airpollutionDataManager.so2flow.asLiveData().observe(viewLifecycleOwner) { so2 ->
            so2.let {
                binding.so2Text.text = "$it μg/m3"
            }
        }

        // pm2.5
        airpollutionDataManager.pm25flow.asLiveData().observe(viewLifecycleOwner) { pm25 ->
            pm25.let {
                binding.pm25Text.text = "$it μg/m3"
            }
        }

        // pm10
        airpollutionDataManager.pm10flow.asLiveData().observe(viewLifecycleOwner) { pm10 ->
            pm10.let {
                binding.pm10Text.text = "$it μg/m3"
            }
        }

        // nh3
        airpollutionDataManager.nh3flow.asLiveData().observe(viewLifecycleOwner) { nh3 ->
            nh3.let {
                binding.nh3Text.text = "$it μg/m3"
            }
        }
    }

    // Get latitute and longitude
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
                    val airpollutionInterface = AirPollutionUtilities.getInstance().create(AirPollutionInterface::class.java)
                    val airpollution = AirPollutionRepository(airpollutionInterface)
                    viewmodel = ViewModelProvider(
                        this,
                        AirPollutionViewModelFactory(
                            airpollution,
                            result.latitude,
                            result.longitude
                        )
                    )[AirPollutionViewModel::class.java]
                    viewmodel.airPollution.observe(requireActivity()) {
                        lifecycleScope.launch {
                            airpollutionDataManager.storeAirPollution(
                                it.list[0].main.aqi.toString(),
                                it.list[0].components.co.toString(),
                                it.list[0].components.no.toString(),
                                it.list[0].components.no2.toString(),
                                it.list[0].components.o3.toString(),
                                it.list[0].components.so2.toString(),
                                it.list[0].components.pm2_5.toString(),
                                it.list[0].components.pm10.toString(),
                                it.list[0].components.nh3.toString()
                            )
                        }
                        binding.airPollutionSwipeRefereshLayout.isRefreshing = false
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