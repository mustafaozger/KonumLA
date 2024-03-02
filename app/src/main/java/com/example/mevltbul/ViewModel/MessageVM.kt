package com.example.mevltbul.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mevltbul.Classes.MessageModel
import com.example.mevltbul.Repository.MessagePageDaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessageVM @Inject constructor(var provideMessagePageDaoRepo:MessagePageDaoRepository) : ViewModel() {
    private fun _getMessage(messageRoomId: String) =provideMessagePageDaoRepo.getMessage(messageRoomId).asLiveData()

    fun getMessage(messageRoomId: String) =_getMessage(messageRoomId)
    fun sendMessage(messageRoomId:String, message:String, senderID:String , callback:(Boolean) -> Unit){
        provideMessagePageDaoRepo.sendMessage(messageRoomId, message, senderID, callback)
    }


    fun addMassageRoomToUserDatabase(uid:String,messageRoomId:String){
        provideMessagePageDaoRepo.addMassageRoomToUserDatabase(uid,messageRoomId)
    }


    private val _messagesLiveData = MutableLiveData<ArrayList<MessageModel>>()
    val messagesLiveData: LiveData<ArrayList<MessageModel>> = _messagesLiveData

    fun listenForMessages(messageRoomId: String) {
        provideMessagePageDaoRepo.listenForMessages(messageRoomId) { messages ->
            _messagesLiveData.postValue(messages)
        }
    }


}