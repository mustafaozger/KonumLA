package com.example.mevltbul.Repository

import com.example.mevltbul.Classes.MessageModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Date

class MessagePageDaoRepository {
    private val db= FirebaseFirestore.getInstance()


    fun sendMessage(messageRoomId:String, message:String, senderID:String , callback:(Boolean) -> Unit){
        val messageModel=MessageModel(messageRoomId, message, senderID, Date().time)
        val randomKey=System.currentTimeMillis().toString()

        db.collection( "chats").document(senderID).collection("message").document(randomKey).set(messageModel).addOnSuccessListener {
            db.collection("chats").document(messageRoomId).collection("message").document(randomKey).set(messageModel).addOnSuccessListener {
                callback(true)
            }.addOnFailureListener {
                callback(false)
            }
        }.addOnFailureListener{
            callback(false)
        }
    }


    fun getMessage(senderID: String): Flow<ArrayList<MessageModel>> = flow {
        try {
            val messages = db.collection("chats").document(senderID).collection("message").get().await()
            val list = ArrayList<MessageModel>()
            for (message in messages) {
                val data = message.toObject(MessageModel::class.java)
                list.add(data)
            }
            emit(list)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}