package com.example.mevltbul.Pages

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
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
        val imageSlider: ImageSlider? =dialog.findViewById(R.id.image_slider)
        val txt_eventType:TextView? =dialog.findViewById(R.id.detailBottomsheetEventType)
        val txt_eventDate: TextView? =dialog.findViewById(R.id.detailBottomsheetEventDate)
        val txt_eventDescription: TextView? =dialog.findViewById(R.id.detailBottomsheetEventDescription)
        val btn_direction:Chip?=dialog.findViewById(R.id.btn_direction)

        if (marker.event_date!=null){
            txt_eventDate?.text="${marker.event_date}"
        }
        if (marker.event_type!=null){
            txt_eventType?.text="Etkinlik Türü : ${marker.event_type}"
        }


        txt_eventDescription?.text=marker.marker_detail

        val imageList=ArrayList<SlideModel>()
        marker.photo1.let {
            imageList.add(SlideModel(it))
        }
        if(marker.photo2!=null)
            imageList.add(SlideModel(marker.photo2))
        if (marker.photo3!=null)
            imageList.add(SlideModel(marker.photo3))
        if (marker.photo4!=null)
            imageList.add(SlideModel(marker.photo4))

        imageSlider?.setImageList(imageList,ScaleTypes.CENTER_INSIDE)

        btn_direction?.setOnClickListener {
            marker.marker_latitude?.toDouble()
                ?.let { it1 -> marker.marker_longtitude?.let { it2 ->
                    direction(it1,
                        it2.toDouble())
                } }
        }


        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setWindowAnimations(R.style.DialogAnimation)
        dialog.window?.setGravity(Gravity.BOTTOM)

    }

    private fun direction(latitude:Double,longitude:Double){
        // Yol tarifi almak için Google Haritalar URL'i oluştur
        val directionUri = Uri.parse("google.navigation:q=${latitude},${longitude}")

// Implicit Intent oluştur ve harita uygulamasını başlat
        val mapIntent = Intent(Intent.ACTION_VIEW, directionUri)
        mapIntent.setPackage("com.google.android.apps.maps")  // Google Haritalar uygulamasını kullan

        if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(mapIntent)
        } else {
            // Eğer Google Haritalar uygulaması yüklü değilse, kullanıcıya bir mesaj göster
            Toast.makeText(requireContext() , "Google Haritalar uygulaması bulunamadı.", Toast.LENGTH_SHORT).show()
        }
    }

}