package com.example.mevltbul.Pages

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mevltbul.Adapter.MainPageExploreRcylerAdapter
import com.example.mevltbul.Utils.Utils
import com.example.mevltbul.R
import com.example.mevltbul.ViewModel.DetailVM
import com.example.mevltbul.databinding.FragmentMainPageBinding
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
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale


@AndroidEntryPoint
class MainPage: Fragment() ,OnMapReadyCallback{
        lateinit var binding: FragmentMainPageBinding
        lateinit var detailVM:DetailVM
        var mMap:GoogleMap?=null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val temp:DetailVM by viewModels()
        detailVM=temp
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())




    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentMainPageBinding.inflate(layoutInflater)
        val mapFragment=childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.layoutMainPage.visibility=View.GONE




        binding.chip.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mainPage_to_mapPage)
        }




        return binding.root
    }


    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser!=null){
            binding.layoutMainPage.visibility=View.VISIBLE
            binding.layoutMainPageProgress.visibility=View.GONE
        }else{
            Navigation.findNavController(requireView()).navigate(R.id.action_mainPage_to_signInPage)
        }

        val bottomNavigationView=requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility=View.VISIBLE
        val fabButton=requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fabButton.visibility=View.VISIBLE


        if (Utils.checkPermission(requireContext())){
            try {

            fusedLocationClient.lastLocation.addOnSuccessListener {location->
                if(location!=null){
                    detailVM.getMarkers(location.latitude,location.longitude)
                    val currentLocation=LatLng(location.latitude,location.longitude)
                    if(mMap!=null){
                        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,8f))
                    }else{
                        Log.d("","2. mMap is null")
                    }
                }


            }
            }catch (e:Exception){
                Log.e("hatamMainOnMapReady",e.toString())
            }

        }else{
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
        }


        viewLifecycleOwner.lifecycleScope.launch {
            detailVM.markerList.collect{list->
                if(list.size>0){
                    binding.imgErrorMessage.visibility=View.GONE
                    val adapter= MainPageExploreRcylerAdapter(requireContext(),list,detailVM)
                    binding.mainPageExploreRycler.adapter=adapter
                    binding.mainPageExploreRycler.layoutManager=
                        StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
                    Log.d("hatamOnWiewCreatedMainMarkerList","list is not empty")
                    binding.progressBarEventList.visibility=View.GONE

                }else{
                    Log.e("hatamMainOnWiewCreatedMarkerList","list is empty")

                    binding.imgErrorMessage.visibility=View.VISIBLE
                }

            }
        }


    }

    override fun onMapReady(p0: GoogleMap) {
        mMap=p0

        if (!Utils.checkPermission(requireContext())) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
        }else{
            showMarker()
        }

    }

    private fun showMarker(){

        val geocoder= Geocoder(requireContext(), Locale.getDefault())
        try {

            viewLifecycleOwner.lifecycleScope.launch {
                detailVM.markerList.collect{list->
                 for ( marker in list){
                        marker.marker_latitude?.let { marker.marker_longtitude?.let { it1 ->
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
                            .icon(Utils.getMarker(requireContext()))
                    }
                     markerOptions?.let { mMap?.addMarker(it) }
                 }
                 binding.progressBarMap.visibility=View.GONE

             }
        }



        }catch (e:Exception){
            e.printStackTrace()
            Log.e("hatamMainShowMarker",e.toString())
        }
    }






}