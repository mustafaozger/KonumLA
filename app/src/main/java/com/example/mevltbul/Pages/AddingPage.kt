package com.example.mevltbul.Pages

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.GnssAntennaInfo.Listener
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mevltbul.Constants.Constants
import com.example.mevltbul.R
import com.example.mevltbul.databinding.FragmentAddingPageBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale

class AddingPage : Fragment(),OnMapReadyCallback {
    private lateinit var binding:FragmentAddingPageBinding
    private var mMap:GoogleMap?=null
    private lateinit var locationListener: LocationListener
    private lateinit var locationManager: LocationManager
    private  var selectedPosition:LatLng?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding=FragmentAddingPageBinding.inflate(layoutInflater)
        val mMapFragment=childFragmentManager.findFragmentById(R.id.addingPageMapFragment) as SupportMapFragment
        mMapFragment.getMapAsync(this)

        binding.btnPublishEvent.setOnClickListener {
            publish()
        }

        return binding.root
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap=p0

        mMap?.setOnMapLongClickListener(listener)
        locationManager=requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationListener=LocationListener{ location->
                val currentLocation=LatLng(location.latitude,location.longitude)
                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15f))
            }
        if (ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION)
            !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,10f,locationListener)
        }

    }

    val listener=object:GoogleMap.OnMapLongClickListener{
        override fun onMapLongClick(p0: LatLng) {
            mMap?.clear()
            val geocoder=Geocoder(requireContext(), Locale.getDefault())
            if(p0!=null){
                var address=""
                try {
                    val addressList=geocoder.getFromLocation(p0.latitude,p0.longitude,1)
                    if (addressList != null) {
                        if(addressList.size>0){
                            if (addressList.get(0).thoroughfare!=null){
                                address+="${addressList[0].thoroughfare} "
                            }
                            if(addressList.get(0).subThoroughfare!=null){
                                address+= addressList[0].subThoroughfare
                            }
                        }
                    }

                }catch (e:Exception){
                    Log.e("hatam AddingPageListener = ", e.toString())
                }
                mMap?.addMarker(MarkerOptions().position(p0).title(address).visible(true).icon(Constants.getMarker(requireContext())))
                selectedPosition=p0
            }


        }

    }

    private fun publish(){
        if (selectedPosition!=null){

            // TODO detay sayfasına gönder

        }else{
            Toast.makeText(requireContext(),"Lütfen bir konum seçin",Toast.LENGTH_LONG).show()


        }
    }


}