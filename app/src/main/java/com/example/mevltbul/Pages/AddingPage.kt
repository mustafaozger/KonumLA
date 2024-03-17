package com.example.mevltbul.Pages

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.example.mevltbul.Utils.Utils
import com.example.mevltbul.R
import com.example.mevltbul.databinding.FragmentAddingPageBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale

class AddingPage : Fragment(),OnMapReadyCallback {
    private lateinit var binding:FragmentAddingPageBinding
    private var mMap:GoogleMap?=null
    private  var selectedPosition:LatLng?=null
    private var address=""
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNavigationView=requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility=View.GONE
        val fabButton=requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fabButton.visibility=View.GONE
    }

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

        if (Utils.checkPermission(requireContext())){
            fusedLocationClient.lastLocation.addOnSuccessListener {location->
                val currentLocation=LatLng(location.latitude,location.longitude)
                if(mMap!=null){
                    selectedPosition=currentLocation
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,13f))
                    mMap?.addMarker(MarkerOptions().position(currentLocation).title(address).visible(true).icon(Utils.getMarker(requireContext())))
                    getAddress(currentLocation)
                }else{
                    Toast.makeText(requireContext(),"Hata",Toast.LENGTH_LONG).show()
                }
            }
        }else{
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
        }

        binding.materialToolbar6.setNavigationOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_addingPage_to_mainPage)
        }

    }

    private val listener=object:GoogleMap.OnMapLongClickListener{
        override fun onMapLongClick(p0: LatLng) {
            mMap?.clear()
            val geocoder=Geocoder(requireContext(), Locale.getDefault())
            if(p0!=null){
                try {
                    getAddress(p0)

                }catch (e:Exception){
                    Log.e("hatam AddingPageListener = ", e.toString())
                }
                mMap?.addMarker(MarkerOptions().position(p0).title(address).visible(true).icon(Utils.getMarker(requireContext())))
                selectedPosition=p0

            }


        }

    }


    private fun getAddress(position: LatLng) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addressList = geocoder.getFromLocation(position.latitude, position.longitude, 1)
            if (addressList != null && addressList.size > 0) {
                address = addressList[0].getAddressLine(0)
            }
        } catch (e: Exception) {
            Log.e("hatam getAddress = ", e.toString())
        }
    }

    private fun publish(){
        if (selectedPosition!=null){
            val transition=AddingPageDirections.actionAddingPageToAddDetailPage(latitude=selectedPosition!!.latitude.toString(),longitude= selectedPosition!!.longitude.toString(),address=this.address)
            Navigation.findNavController(requireView()).navigate(transition)

        }else{
            Toast.makeText(requireContext(),"Lütfen bir konum seçin",Toast.LENGTH_LONG).show()


        }
    }


}