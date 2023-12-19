package com.example.mevltbul.Pages

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.mevltbul.Classes.Marker
import com.example.mevltbul.R
import com.example.mevltbul.databinding.FragmentMainPageBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MainPage : Fragment() ,OnMapReadyCallback{
    var list=MutableLiveData<ArrayList<Marker>>()
    lateinit var binding:FragmentMainPageBinding
    lateinit var locationManager:LocationManager
    lateinit var locationListener:LocationListener
    var mMap:GoogleMap?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMainPageBinding.inflate(layoutInflater)

        val mapFragment=childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap=p0
        locationManager= requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener= LocationListener { location ->
            val currentLocation=LatLng(location.latitude,location.longitude)
            if(mMap!=null){
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,12f))
            }
        }
        if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
        }else{
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,10f,locationListener)
        }

        addMarkers()
    }

    fun addMarkers(){
        list.observe(viewLifecycleOwner){markList->
            for (marker in markList){
                val location= marker.marker_latitude?.let { marker.marker_longtitude?.let { it1 ->
                    LatLng(it.toDouble(),
                        it1.toDouble())
                } }
                location?.let { MarkerOptions().position(it) }?.let { mMap?.addMarker(it) }
            }
        }
    }


}