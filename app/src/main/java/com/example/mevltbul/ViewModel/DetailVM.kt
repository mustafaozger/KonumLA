package com.example.mevltbul.ViewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.mevltbul.Classes.Marker
import com.example.mevltbul.Classes.MessageRoomModel
import com.example.mevltbul.Repository.DetailPageDaoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class DetailVM @Inject constructor(var detailPageDaoRepo: DetailPageDaoRepo): ViewModel() {


    private val _markerList = MutableStateFlow<List<Marker>>(emptyList())
    val markerList: StateFlow<List<Marker>> = _markerList

    private val _messageRoomLiveData = MutableLiveData<ArrayList<MessageRoomModel>>()
    val messageRoomLiveData: LiveData<ArrayList<MessageRoomModel>> = _messageRoomLiveData

    private var savedMarkersLiveData = MutableLiveData<List<Marker>>()
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


    fun getMessageRooms3(idList: ArrayList<String>){
        detailPageDaoRepo.getChatListWithID(idList){
            _messageRoomLiveData.postValue(it)
        }
    }


    fun getSavedList(): LiveData<List<Marker>> {
        viewModelScope.launch {
            detailPageDaoRepo.getSavedEvents {markers->
                if (markers.size==0 || markers.isEmpty()) {
                    savedMarkersLiveData.postValue(arrayListOf())
                }else{
                    savedMarkersLiveData.postValue(markers)
                }

            }
        }
        return savedMarkersLiveData
    }


    fun addSavedEvent(markerId:String){
        detailPageDaoRepo.addSavedEvent(markerId)
    }

    fun deleteSavedEvent(markerId:String) {
        detailPageDaoRepo.deleteSavedEvent(markerId)
    }
    fun getMessageRooms(): LiveData<ArrayList<MessageRoomModel>> {
        detailPageDaoRepo.getChatListWithID2(){
            _messageRoomLiveData.postValue(it)
        }
        return _messageRoomLiveData
    }


}