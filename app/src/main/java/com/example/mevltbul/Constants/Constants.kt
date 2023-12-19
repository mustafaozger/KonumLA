package com.example.mevltbul.Constants

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.example.mevltbul.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

class Constants  {
    companion object{
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
    }
}