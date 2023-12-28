package com.example.mevltbul.Pages

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
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
        private val markList = HashMap<com.google.android.gms.maps.model.Marker, Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val temp:DetailVM by viewModels()
        detailVM=temp
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentMainPageBinding.inflate(layoutInflater)
        val mapFragment=childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        markerList= detailVM.getEventLists()



        return binding.root
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap=p0
        locationManager=requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener=LocationListener{location ->
//            val currentLocation=LatLng(location.latitude,location.longitude)
            val currentLocation=LatLng(37.3076287,-122.013545)

            if(mMap!=null){

                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15f))
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

                    if (addressList != null) {
                        if(addressList.size>0){
                            //normalde cadde ve sokak ismini buradan alıyorum ama
                        }

                    }
                    val latLng= marker.marker_latitude?.let { marker.marker_longtitude?.let { it1 ->
                        LatLng(it.toDouble(),
                            it1.toDouble())
                    } }
                    val markerOptions = latLng?.let {
                        MarkerOptions()
                            .position(it)
                            .title("Marker Başlığı")
                            .snippet("Marker Açıklaması")
                            .icon(Constants.getMarker(requireContext()))
                    }
                   val mar=markerOptions?.let { mMap?.addMarker(it) }
                    if (mar != null) {
                        markList.put(mar,marker)
                    }


                }
                mMap?.setOnMarkerClickListener { clickedMarker->
                    val event =markList.get(clickedMarker)
                    if(event!=null){
                        Log.d("hatamMainPageMarkerClick"," mar ${event.marker_id}")
                    }

                    true
                }

            }


        }catch (e:Exception){
            e.printStackTrace()
        }
    }




}