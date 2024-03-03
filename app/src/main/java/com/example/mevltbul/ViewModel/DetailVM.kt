package com.example.mevltbul.ViewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mevltbul.Classes.Marker
import com.example.mevltbul.Classes.MessageRoomModel
import com.example.mevltbul.Repository.DetailPageDaoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class DetailVM @Inject constructor(var detailPageDaoRepo: DetailPageDaoRepo): ViewModel() {


    // StateFlow to hold the list of markers
    private val _markerList = MutableStateFlow<List<Marker>>(emptyList())
    val markerList: StateFlow<List<Marker>> = _markerList

    fun getMarkers(latitude: Double, longitude: Double) {
        // Launch a coroutine in the viewModelScope
        viewModelScope.launch {
            // Call the function from the repository to get the Flow of markers
            detailPageDaoRepo.getEventListsFromDatabaseWitfFlow(latitude, longitude).collect { markers ->
                // Update the StateFlow with the new list of markers
                _markerList.value = markers
            }
        }
    }


    fun publishDetail( marker_id:String?,
                       marker_name:String?=null,
                         marker_latitude:String?=null,
                         marker_longtitude: String?=null,
                         marker_detail:String?=null,
                         imageList: ArrayList<Uri?>,
                         event_type:String?=null,
                         event_date:String?=null,
                         callback:(Boolean) ->Unit)
    {

        viewModelScope.launch {

            detailPageDaoRepo.publishDetail( marker_id,marker_name, marker_latitude, marker_longtitude, marker_detail, imageList,event_type,event_date,callback)

        }
    }


    fun getEventLists():MutableLiveData<ArrayList<Marker>>{
        Log.d("hatamDetailVM","2. getEventLists")
        return detailPageDaoRepo.getEventLists()
    }



    private val _messageRoomLiveData = MutableLiveData<ArrayList<MessageRoomModel>>()
    val messageRoomLiveData: LiveData<ArrayList<MessageRoomModel>> = _messageRoomLiveData

    fun getMessageRooms(idList: ArrayList<String>){
        detailPageDaoRepo.getEventListWithID(idList){
            _messageRoomLiveData.postValue(it)
        }
    }



}