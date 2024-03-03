package com.example.mevltbul.Repository

import android.util.Log
import com.example.mevltbul.Classes.MessageModel
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date
import kotlin.collections.ArrayList

class MessagePageDaoRepository {
    private val db= FirebaseFirestore.getInstance()


    fun sendMessage(messageRoomId:String, message:String, senderID:String , callback:(Boolean) -> Unit){
        val messageModel=MessageModel(messageRoomId, message, senderID, Date().time)
        val randomKey=System.currentTimeMillis().toString()

        db.collection( "chats").document(messageRoomId).collection("message").document(randomKey).set(messageModel).addOnSuccessListener {
            db.collection("chats").document(senderID).collection("message").document(randomKey).set(messageModel).addOnSuccessListener {
                callback(true)
            }.addOnFailureListener {
                Log.d("hatamMessageDao", " error:  messageRoom"+it.toString())
                callback(false)
            }
        }.addOnFailureListener{
            Log.d("hatamMessageDao", " error:  senderId"+it.toString())

            callback(false)
        }
    }

    fun addMassageRoomToUserDatabase(uid:String,newMessageRoomId:String){
        val user=db.collection("Users").document(uid)
        user.get().addOnSuccessListener {roomList->
            val retList=roomList.get("message_roooms_id") as ArrayList<String>
            retList.add(newMessageRoomId)
            user.update("message_roooms_id",retList)
        }



    }


    fun getMessage(messageRoomId: String, callback: (ArrayList<MessageModel>) -> Unit) {
        try {
            db.collection("chats").document(messageRoomId)
                .collection("message")
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.w("hatamMessageDao", "Error: Listen failed", e)
                        return@addSnapshotListener
                    }

                    if (snapshots != null) {
                        val list = ArrayList<MessageModel>()
                        for (doc in snapshots) {
                            val data = doc.toObject(MessageModel::class.java)
                            list.add(data)
                        }
                        callback(list)
                    } else {
                        Log.d("hatamMessageDao", "Current data: null")
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }





    fun getMessageRooms(uid:String,callback: (ArrayList<String>) -> Unit){
        val user=db.collection("Users").document(uid).get().addOnSuccessListener {
            if (it!=null){
                val list=it.get("message_roooms_id") as ArrayList<String>
                callback(list)
            }
        }

    }

}