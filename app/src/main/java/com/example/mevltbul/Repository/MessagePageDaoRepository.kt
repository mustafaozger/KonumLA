package com.example.mevltbul.Repository

import android.util.Log
import java.util.ArrayList
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


    fun getMessage(messageRoomId: String): Flow<ArrayList<MessageModel>> = flow {
        try {
            Log.d("hatamMessageDao", " message dao work + messageRoomId: "+messageRoomId)
            val list = ArrayList<MessageModel>()
            if (messageRoomId!=null|| messageRoomId!="null"){

                val messages = db.collection("chats").document(messageRoomId).collection("message").get().await()
                Log.d("hatamMessageDao", " messages size: "+messages.size())

                for (message in messages) {
                    Log.d("hatamMessageDao", " message: "+message.toString())
                    val data = message.toObject(MessageModel::class.java)
                    list.add(data)
                }

            }else{
                Log.d("hatamMessageDao", " message room id null $messageRoomId")

            }

            Log.d("hatamMessageDao", " message dao work list : "+list)

            emit(list)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun addMassageRoomToUserDatabase(uid:String,messageRoomId:String){
        val user=db.collection("Users").document(uid)
        user.collection("message_roooms_id").get().addOnSuccessListener { dataList->
            val list=dataList.toObjects(String::class.java)
            list.add(messageRoomId)
            user.update("message_roooms_id",list)
        }
    }


    fun listenForMessages(messageRoomId: String, callback: (ArrayList<MessageModel>) -> Unit) {
        try {
            db.collection("chats").document(messageRoomId)
                .collection("message")
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.w("hatamMessageDao", "Listen failed", e)
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








}