package com.imcodernitesh.weatheropedia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.imcodernitesh.weatheropedia.R
import com.imcodernitesh.weatheropedia.databinding.FragmentCheckPermissionBinding
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class CheckPermissionFragment : Fragment(),EasyPermissions.PermissionCallbacks {

    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
    }

    private lateinit var binding: FragmentCheckPermissionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCheckPermissionBinding.inflate(inflater, container, false)

        if(hasLocationPermission()){
            findNavController().navigate(R.id.action_checkPermissionFragment_to_splashFragment)
        } else {
            requestLocationPermission()
        }


        return binding.root
    }

    private fun hasLocationPermission() = EasyPermissions.hasPermissions(
        requireContext(),
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            "Location permission is needed to get the current weather",
            PERMISSION_LOCATION_REQUEST_CODE,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    // if user denies
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            SettingsDialog.Builder(requireActivity()).build().show()
        }
        else {
            SettingsDialog.Builder(requireActivity()).build().show()
            Toast.makeText(requireContext(), "Give the Permssion", Toast.LENGTH_SHORT).show()
        }
    }

    // if user grants
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(requireContext(),"Permission Granted",Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_checkPermissionFragment_to_splashFragment)
    }

    override fun onResume() {
        super.onResume()
        if(hasLocationPermission()){
            findNavController().navigate(R.id.action_checkPermissionFragment_to_splashFragment)
        }
        /*
        else if(!hasLocationPermission()){
            onPermissionsDenied(PERMISSION_LOCATION_REQUEST_CODE, listOf(android.Manifest.permission.ACCESS_FINE_LOCATION))
        }
         */
    }


}