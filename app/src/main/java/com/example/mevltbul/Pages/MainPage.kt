package com.example.mevltbul.Pages

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.mevltbul.Classes.Marker
import com.example.mevltbul.Constants.Constants
import com.example.mevltbul.R
import com.example.mevltbul.ViewModel.DetailVM
import com.example.mevltbul.databinding.FragmentMainPageBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainPage: Fragment() ,OnMapReadyCallback{
        lateinit var binding: FragmentMainPageBinding
        lateinit var detailVM:DetailVM
        lateinit var locationManager: LocationManager
        lateinit var locationListener: LocationListener
        var mMap:GoogleMap?=null
        private var markerList=MutableLiveData<ArrayList<Marker>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val temp:DetailVM by viewModels()
        detailVM=temp
        locationManager=requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val lastLocation:Location?=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if(lastLocation!=null){
            markerList=detailVM.getEventLists(lastLocation.latitude,lastLocation.longitude)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentMainPageBinding.inflate(layoutInflater)
        val mapFragment=childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)


        binding.chip.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mainPage_to_mapPage)
        }

        return binding.root
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onMapReady(p0: GoogleMap) {
        mMap=p0



        locationListener=LocationListener{location ->
            val currentLocation=LatLng(location.latitude,location.longitude)
            if(mMap!=null){
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,8f))
            }
        }
        if (ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION)
            !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)

        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,10f,locationListener)
        }
        showMarker()

    }

    private fun showMarker(){
        val geocoder= Geocoder(requireContext(), Locale.getDefault())
        try {
             markerList.observe(viewLifecycleOwner){list->
                 for ( marker in list){
                      val addressList= marker.marker_latitude?.let { marker.marker_longtitude?.let { it1 ->
                        geocoder.getFromLocation(it.toDouble(),
                            it1.toDouble(),1)
                    } }
                    val latLng= marker.marker_latitude?.let { marker.marker_longtitude?.let { it1 ->
                        LatLng(it.toDouble(),
                            it1.toDouble())
                    } }
                    val markerOptions = latLng?.let {
                        MarkerOptions()
                            .position(it)
                            .icon(Constants.getMarker(requireContext()))
                    }
                     markerOptions?.let { mMap?.addMarker(it) }




                 }

            }


        }catch (e:Exception){
            e.printStackTrace()
        }
    }





}