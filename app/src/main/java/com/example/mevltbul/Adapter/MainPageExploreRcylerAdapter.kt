package com.example.mevltbul.Adapter

import android.annotation.SuppressLint
import   android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.mevltbul.Classes.Marker
import com.example.mevltbul.Classes.MessageRoomModel
import com.example.mevltbul.Pages.MainPageDirections
import com.example.mevltbul.Pages.MessageRoomsPageDirections
import com.example.mevltbul.Pages.SavedEnevtsPage
import com.example.mevltbul.Pages.SavedEnevtsPageDirections
import com.example.mevltbul.Utils.Utils
import com.example.mevltbul.R
import com.example.mevltbul.ViewModel.DetailVM
import com.example.mevltbul.databinding.MainPageNeareventRcylerBinding

class MainPageExploreRcylerAdapter(val context: Context,val markerList:List<Marker>,val detailVM: DetailVM,val fragment: Fragment,val callingFragmentTag:String):RecyclerView.Adapter<MainPageExploreRcylerAdapter.ViewHolder>() {
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

        if (markerList[position].marker_name!=null){
            if (markerList[position].marker_name!!.length>18){
                binding.txtMainpageExploreRyclerEventName.text=markerList[position].marker_name!!.substring(0,15)+"..."

            }else{
                binding.txtMainpageExploreRyclerEventName.text=markerList[position].marker_name
            }
        }else{
            binding.txtMainpageExploreRyclerEventName.text=""
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

        binding.imgMainpageExploreRyclerImage.setOnClickListener {view->
            Utils.showAllert(context,marker,detailVM,fragment){
                if (it){
                    val messageRoom=MessageRoomModel(marker.marker_id,marker.marker_name,marker.photo1)
                    Log.d("hatamMessageRoom",messageRoom.toString())
                    val bundle =MainPageDirections.actionMainPageToMessagesPage(messageRoom)
                    Navigation.findNavController(view).navigate(bundle)
                }

            }
        }

        binding.imgMainpageExploreRyclerImage.setItemClickListener(object :ItemClickListener{
            override fun doubleClick(position: Int) {
            }

            override fun onItemSelected(position: Int) {
                Utils.showAllert(context,marker,detailVM,fragment){
                    if (it){
                        val messageRoom=MessageRoomModel(marker.marker_id,marker.marker_name,marker.photo1)
                        Log.d("hatamMessageRoom",messageRoom.toString())
                        val bundle =MainPageDirections.actionMainPageToMessagesPage(messageRoom)
                    Navigation.findNavController(fragment.requireView()).navigate(bundle)
                    }

                }
            }

        })

        binding.rcylerNearEventLayout.setOnClickListener {view->
            Utils.showAllert(context,marker,detailVM,fragment){
                if (it){
                    goMessage(marker,view)
                }

            }

        }

        binding.btnReclerMessage.setOnClickListener {
            goMessage(marker,it)
        }


        if(marker.marker_detail!=null){
            if(marker.marker_detail!!.length>50){
                binding.txtMainpageExploreRyclerEventDetail.text=marker.marker_detail!!.substring(0,47)+"..."
            }else{
                binding.txtMainpageExploreRyclerEventDetail.text=marker.marker_detail
            }
        }else{
            binding.txtMainpageExploreRyclerEventDetail.text=""
        }



    }
    private fun goMessage(marker:Marker,view: View){
        val messageRoom=MessageRoomModel(marker.marker_id,marker.marker_name,marker.photo1)
        Log.d("hatamMessageRoom",messageRoom.toString())

        if(callingFragmentTag=="MainPage"){
            val bundle =MainPageDirections.actionMainPageToMessagesPage(messageRoom)
            Navigation.findNavController(view).navigate(bundle)
        }else if (callingFragmentTag=="SavedEnevtsPage"){

            val bundle =SavedEnevtsPageDirections.actionSavedEnevtsPageToMessagesPage(messageRoom)
            Navigation.findNavController(view).navigate(bundle)
        }

    }



}