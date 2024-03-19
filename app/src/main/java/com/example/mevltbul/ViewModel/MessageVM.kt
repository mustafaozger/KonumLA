package com.example.mevltbul.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mevltbul.Classes.MessageModel
import com.example.mevltbul.Repository.MessagePageDaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessageVM @Inject constructor(var provideMessagePageDaoRepo:MessagePageDaoRepository) : ViewModel() {

    fun sendMessage(messageRoomId:String, message:String, senderID:String , callback:(Boolean) -> Unit){
        provideMessagePageDaoRepo.sendMessage(messageRoomId, message, senderID, callback)
    }


    fun addMassageRoomToUserDatabase(uid:String,messageRoomId:String){
        provideMessagePageDaoRepo.addMassageRoomToUserDatabase(uid,messageRoomId)
    }


    private val _messagesLiveData = MutableLiveData<ArrayList<MessageModel>>()
    val messagesLiveData: LiveData<ArrayList<MessageModel>> = _messagesLiveData

    fun getMessage(messageRoomId: String) {
        provideMessagePageDaoRepo.getMessage(messageRoomId) { messages ->
            _messagesLiveData.postValue(messages)
        }
    }

    private val _messageRoomLiveData = MutableLiveData<ArrayList<String>>()
    val messageRoomLiveData: LiveData<ArrayList<String>> = _messageRoomLiveData

    fun getMessageRooms(uid:String){

        provideMessagePageDaoRepo.getMessageRooms(uid) { messageRooms ->
            _messageRoomLiveData.postValue(messageRooms)
        }
    }


}