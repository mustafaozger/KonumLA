package com.example.mevltbul.Utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.mevltbul.Classes.Marker
import com.example.mevltbul.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip

class Utils  {
    companion object{
         fun checkPermission(context: Context):Boolean{
            return ContextCompat.checkSelfPermission(context,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }

        fun getMarker(contex:Context):BitmapDescriptor?{
            var vectorDrawable=ContextCompat.getDrawable(contex,R.drawable.placeholder)
            vectorDrawable?.setBounds(0,0,vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight)
            val bitmap= vectorDrawable?.let { Bitmap.createBitmap(it.intrinsicWidth,vectorDrawable.intrinsicHeight,Bitmap.Config.ARGB_8888) }
            val canvas= bitmap?.let { Canvas(it) }
            if (canvas != null) {
                if (vectorDrawable != null) {
                    vectorDrawable.draw(canvas)
                }
            }
            return bitmap?.let { BitmapDescriptorFactory.fromBitmap(it) }
        }
         fun showAllert(context:Context,marker: Marker){
            val dialog= BottomSheetDialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.detail_event_bottom_sheet)
            val imageSlider: ImageSlider? =dialog.findViewById(R.id.image_slider)
            val txt_eventType: TextView? =dialog.findViewById(R.id.detailBottomsheetEventType)
            val txt_eventDate: TextView? =dialog.findViewById(R.id.detailBottomsheetEventDate)
            val txt_eventDescription: TextView? =dialog.findViewById(R.id.detailBottomsheetEventDescription)
            val btn_direction: Chip?=dialog.findViewById(R.id.btn_direction)

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

            imageSlider?.setImageList(imageList, ScaleTypes.CENTER_INSIDE)

            btn_direction?.setOnClickListener {
                marker.marker_latitude?.toDouble()
                    ?.let { it1 -> marker.marker_longtitude?.let { it2 ->
                       direction(context,it1,
                            it2.toDouble())
                    } }
            }


            dialog.show()
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setWindowAnimations(R.style.DialogAnimation)
            dialog.window?.setGravity(Gravity.BOTTOM)

        }
        private fun direction(context: Context,latitude:Double,longitude:Double){
            // Yol tarifi almak için Google Haritalar URL'i oluştur
            val directionUri = Uri.parse("google.navigation:q=${latitude},${longitude}")

            // Implicit Intent oluştur ve harita uygulamasını başlat
            val mapIntent = Intent(Intent.ACTION_VIEW, directionUri)
            mapIntent.setPackage("com.google.android.apps.maps")  // Google Haritalar uygulamasını kullan

            if (mapIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(mapIntent)

            } else {
                // Eğer Google Haritalar uygulaması yüklü değilse, kullanıcıya bir mesaj göster
                Toast.makeText(context , "Google Haritalar uygulaması bulunamadı.", Toast.LENGTH_SHORT).show()
                }
            }

//        TODO("Silinecek")
        val UID="2334"


    }


}