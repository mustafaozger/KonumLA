package com.example.mevltbul.Utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.mevltbul.Classes.Marker
import com.example.mevltbul.R
import com.example.mevltbul.ViewModel.DetailVM
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso

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
         fun showAllert(context:Context,marker: Marker,detailVM: DetailVM,fragment: Fragment,isMessage : (Boolean) -> Unit){
            val dialog= BottomSheetDialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.detail_event_bottom_sheet)
            val imageSlider: ImageSlider? =dialog.findViewById(R.id.img_detail_image_slider)
            val txt_eventType: TextView? =dialog.findViewById(R.id.txt_detail_event_type)
            val txt_eventDate: TextView? =dialog.findViewById(R.id.txt_detail_event_date)
            val txt_eventDescription: TextView? =dialog.findViewById(R.id.txt_detail_event_detail)
            val btn_direction: LinearLayout?=dialog.findViewById(R.id.layout_detail_event_adress_direction)
             val btn_message_room:LinearLayout?=dialog.findViewById(R.id.layout_detail_message_room)
             val btn_saveLocation:ImageButton?=dialog.findViewById(R.id.btn_detail_save_event)
             val txt_detail_event_name:TextView?=dialog.findViewById(R.id.txt_detail_event_name)
             val savedveventList=ArrayList<Marker>()
            if (marker.event_date!=null){
                txt_eventDate?.text="${marker.event_date}"
            }
            if (marker.event_type!=null){
                txt_eventType?.text="${marker.event_type}"
            }

             detailVM.getSavedList().observe(fragment.viewLifecycleOwner){
                 savedveventList.clear()
                 savedveventList.addAll(it)
                 Log.d("hatamUtilSave","it room")
                 if (it.contains(marker)){
                     btn_saveLocation?.setImageResource(R.drawable.saved_button_dark)
                     Log.d("hatamUtilSave","it room contains")
                 }else{
                     btn_saveLocation?.setImageResource(R.drawable.saved_button_default)
                     Log.d("hatamUtilSave","it room not contains")
                 }


             }
             btn_saveLocation?.setOnClickListener {
                 if (savedveventList.contains(marker)){
                     Log.d("hatamUtilSave","it  click contains")
                     btn_saveLocation?.setImageResource(R.drawable.saved_button_default)
                     detailVM.deleteSavedEvent(marker.marker_id!!)
                     if (savedveventList.size==1){
                         savedveventList.clear()
                     }
                 }else{
                     btn_saveLocation?.setImageResource(R.drawable.saved_button_dark)
                     Log.d("hatamUtilSave","it click not contains")
                     detailVM.addSavedEvent(marker.marker_id!!)
                 }
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

             btn_message_room?.setOnClickListener{
                 isMessage(true)
                 dialog.cancel()
             }

             if (txt_detail_event_name != null) {
                 txt_detail_event_name.text=marker.marker_name
             }else{
                 txt_detail_event_name?.text=""
             }
             imageSlider?.setItemClickListener(object :ItemClickListener{
                 override fun doubleClick(position: Int) {
                 }

                 @SuppressLint("SuspiciousIndentation")
                 override fun onItemSelected(position: Int) {
                   val image=imageList.get(position).imageUrl
                     Log.d("hatamUtils","img :  $image!!")
                     zoomPhoto(context,image!!)
                 }

             })



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


        private fun zoomPhoto(context: Context,photo_link:String){
            val dialog= Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.design_zoom_image_slider)
            val btn_close_image:ImageView=dialog.findViewById(R.id.btn_image_close)
            val image_zoomed_photo:ImageView=dialog.findViewById(R.id.img_zoom)

            Picasso.get().load(photo_link)
                .centerCrop().resize(900,1300)
                .into(image_zoomed_photo)

            btn_close_image.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setWindowAnimations(R.style.DialogAnimation)
            dialog.window?.setGravity(Gravity.CENTER)
        }
    }


}