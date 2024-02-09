package com.example.mevltbul.ViewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mevltbul.Classes.Marker
import com.example.mevltbul.Repository.DetailPageDaoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class DetailVM @Inject constructor(var detailPageDaoRepo: DetailPageDaoRepo): ViewModel() {

    fun publishDetail( marker_id:String?,
                         marker_latitude:String?=null,
                         marker_longtitude: String?=null,
                         marker_detail:String?=null,
                         imageList: ArrayList<Uri?>,
                         event_type:String?=null,
                         event_date:String?=null,
                         callback:(Boolean) ->Unit)
    {

        viewModelScope.launch {
            detailPageDaoRepo.publishDetail( marker_id, marker_latitude, marker_longtitude, marker_detail, imageList,event_type,event_date,callback)

        }
    }

    fun getEventLists(latitude:Double,longitude:Double): MutableLiveData<ArrayList<Marker>> {
       return detailPageDaoRepo.getEventLists(latitude,longitude)
    }
    fun getEventLists():MutableLiveData<ArrayList<Marker>>{
        return detailPageDaoRepo.getEventLists()
    }

}