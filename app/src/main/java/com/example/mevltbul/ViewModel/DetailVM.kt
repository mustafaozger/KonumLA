package com.example.mevltbul.ViewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mevltbul.Repository.DetailPageDaoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class DetailVM @Inject constructor(var detailPageDaoRepo: DetailPageDaoRepo): ViewModel() {
    fun publishDetail(contex : Context, marker_id:String?,
                      marker_latitude:String?=null,
                      marker_longtitude: String?=null,
                      marker_detail:String?=null, imageList: ArrayList<Uri?>){

        viewModelScope.launch {

            detailPageDaoRepo.publishDetail(contex, marker_id, marker_latitude, marker_longtitude, marker_detail, imageList)
        }


    }

}