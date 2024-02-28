package com.example.mevltbul.Pages

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.mevltbul.Classes.Marker
import com.example.mevltbul.Utils.Utils
import com.example.mevltbul.R
import com.example.mevltbul.ViewModel.DetailVM
import com.example.mevltbul.databinding.FragmentMapPageBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MapPage : Fragment(),OnMapReadyCallback {

    var mMap:GoogleMap?=null
    lateinit var binding: FragmentMapPageBinding
    lateinit var detailVM:DetailVM
    private val markList = HashMap<com.google.android.gms.maps.model.Marker, Marker>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val temp:DetailVM by viewModels()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        detailVM=temp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentMapPageBinding.inflate(layoutInflater)
        val mapFragment=childFragmentManager.findFragmentById(R.id.mapFragment2) as SupportMapFragment
        mapFragment.getMapAsync(this)



        return binding.root
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap=p0
        if (Utils.checkPermission(requireContext())){
            fusedLocationClient.lastLocation.addOnSuccessListener {location->
                detailVM.getMarkers(location.latitude,location.longitude)
                val currentLocation=LatLng(location.latitude,location.longitude)
                if(mMap!=null){
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,10f))
                }else{
                    Log.d("hatamMainOnMapReady","2. mMap is null")
                }
            }
        }else{
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
        }


        if(Utils.checkPermission(requireContext())){
            showMarker()
        }else{
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
        }
    }


    private fun showMarker(){
        val geocoder= Geocoder(requireContext(), Locale.getDefault())
        try {

            viewLifecycleOwner.lifecycleScope.launch {
                detailVM.markerList.collect(){list->
                    Log.d("hatamMapPageShowMarker","list size ${list.size}")
                    for(marker in list){
                        val address= marker.marker_latitude?.let { marker.marker_longtitude?.let { it1 ->
                            geocoder.getFromLocation(it.toDouble(),
                                it1.toDouble(),1)
                        } }
                        val latLang= marker.marker_latitude?.let { marker.marker_longtitude?.let { it2 ->
                            LatLng(it.toDouble(),
                                it2.toDouble())
                        } }

                        val markerOptions = latLang?.let {
                            MarkerOptions()
                                .position(it)
                                .icon(Utils.getMarker(requireContext()))
                        }
                        val mar=markerOptions?.let { mMap?.addMarker(it) }
                        if (mar != null) {
                            markList.put(mar,marker)
                        }

                    }

                    mMap?.setOnMarkerClickListener { clickedMarker->
                        val event =markList.get(clickedMarker)
                        if(event!=null){
                            Utils.showAllert(requireContext(),event)
                        }
                        true
                    }
                }

            }

        }catch (e:Exception){
            Log.e("hatamMapPageShowMarker",e.toString())
        }
    }


}