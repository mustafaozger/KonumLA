package com.example.mevltbul.Adapter

import android.annotation.SuppressLint
import   android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.mevltbul.Classes.Marker
import com.example.mevltbul.Utils.Utils
import com.example.mevltbul.R
import com.example.mevltbul.databinding.MainPageNeareventRcylerBinding

class MainPageExploreRcylerAdapter(val context: Context,val markerList:List<Marker>):RecyclerView.Adapter<MainPageExploreRcylerAdapter.ViewHolder>() {
     class ViewHolder(val binding:MainPageNeareventRcylerBinding):RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater=LayoutInflater.from(context)
        val binding=MainPageNeareventRcylerBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return markerList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding=holder.binding
        binding.txtMainpageExploreRyclerDate.text=markerList[position].event_date
        binding.txtMainpageExploreRyclerEventType.text=markerList[position].event_type

        if (markerList[position].marker_detail!=null){
            if (markerList[position].marker_detail!!.length>56){
                binding.txtMainpageExploreRyclerEventDetail.text=markerList[position].marker_detail!!.substring(0,56)+"..."

            }else{
                binding.txtMainpageExploreRyclerEventDetail.text=markerList[position].marker_detail
            }
        }

       val marker=markerList[position]
        val imageList=ArrayList<SlideModel>()
        if (marker.photo1!=null){
            imageList.add(SlideModel(marker.photo1))
        }
        if(marker.photo2!=null){
            imageList.add(SlideModel(marker.photo2))
        }
        if (marker.photo3!=null){
            imageList.add(SlideModel(marker.photo3))
        }
        if (marker.photo4!=null){
            imageList.add(SlideModel(marker.photo4))
        }
        if(imageList.size>0) {
            binding.imgMainpageExploreRyclerImage.setImageList(imageList, ScaleTypes.FIT)
        }else{
            binding.imgMainpageExploreRyclerImage.setImageList(List<SlideModel>(1){SlideModel(R.drawable.loading_placeholder)}, ScaleTypes.CENTER_INSIDE)
        }

        binding.rcylerNearEventLayout.setOnClickListener {
            Utils.showAllert(context,marker)

        }
        binding.imgMainpageExploreRyclerImage.setOnClickListener(){
            Utils.showAllert(context,marker)
        }



    }



}