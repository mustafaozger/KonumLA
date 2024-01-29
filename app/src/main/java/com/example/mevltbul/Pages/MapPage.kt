package com.example.mevltbul.Pages

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.mevltbul.Classes.Marker
import com.example.mevltbul.Constants.Constants
import com.example.mevltbul.R
import com.example.mevltbul.ViewModel.DetailVM
import com.example.mevltbul.databinding.FragmentMapPageBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MapPage : Fragment(),OnMapReadyCallback {

    lateinit var locationManager: LocationManager
    lateinit var locationListener: LocationListener
    var mMap:GoogleMap?=null
    lateinit var binding: FragmentMapPageBinding
    lateinit var detailVM:DetailVM
    private var markerList= MutableLiveData<ArrayList<Marker>>()
    private val markList = HashMap<com.google.android.gms.maps.model.Marker, Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val temp:DetailVM by viewModels()
        detailVM=temp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentMapPageBinding.inflate(layoutInflater)
        val mapFragment=childFragmentManager.findFragmentById(R.id.mapFragment2) as SupportMapFragment
        markerList=detailVM.getEventLists()
        mapFragment.getMapAsync(this)
        return binding.root
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap=p0
        locationManager=requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            Log.d("hatamMapPageOnMapPermission","permission denied")
            return
        }
        locationListener= LocationListener { location ->
            val currentLocation=LatLng(location.latitude,location.longitude)
            if(mMap!=null){
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,10f))
            }

        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,10f,locationListener)
        showMarker()
    }


    private fun showMarker(){
        val geocoder= Geocoder(requireContext(), Locale.getDefault())
        try {
            markerList.observe(viewLifecycleOwner){list->

                for(marker in list){
                    val address= marker.marker_latitude?.let { marker.marker_longtitude?.let { it1 ->
                        geocoder.getFromLocation(it.toDouble(),
                            it1.toDouble(),1)
                    } }
                    val latLang= marker.marker_latitude?.let { marker.marker_longtitude?.let { it1 ->
                        LatLng(it.toDouble(),
                            it1.toDouble())
                    } }

                    val markerOptions = latLang?.let {
                        MarkerOptions()
                            .position(it)
//                            .title("Marker Başlığı")
//                            .snippet("Marker Açıklaması")
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

            mMap?.setOnMarkerClickListener { clickedMarker->

            //TODO
                val event=markList.get(clickedMarker)
                if(event!=null){
                    showAllert(event)
                }




                true
            }



        }catch (e:Exception){
            Log.e("hatamMapPageShowMarker",e.toString())
        }


    }


    private fun showAllert(marker:Marker){
        val dialog=BottomSheetDialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.detail_event_bottom_sheet)
        val imageRycler: RecyclerView? =dialog.findViewById(R.id.detailBottomsheetRecyclerView)
        val txt_eventName:TextView? =dialog.findViewById(R.id.detailBottomsheetEventName)
        val txt_eventDate: TextView? =dialog.findViewById(R.id.detailBottomsheetEventDate)
        val txt_eventDescription: TextView? =dialog.findViewById(R.id.detailBottomsheetEventDescription)
        val btn_direction:Chip?=dialog.findViewById(R.id.btn_direction)

        txt_eventDescription?.text=marker.marker_detail
        Toast.makeText(requireContext(),marker.marker_detail,Toast.LENGTH_LONG).show()


        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setWindowAnimations(R.style.DialogAnimation)
        dialog.window?.setGravity(Gravity.BOTTOM)

    }

}