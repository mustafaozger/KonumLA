package com.example.mevltbul.Repository

import android.util.Log
import com.example.mevltbul.Classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.tasks.await

class UserDaoRepository {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    init {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }


    fun createUser(userName:String,email:String,password:String,isCreate:(Boolean) -> Unit){
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            val userCollection=db.collection("Users")
            val shared_event_list=ArrayList<String>()
            val saved_location_list=ArrayList<String>()
            val meessageRoomsId=ArrayList<String>()

            val user= hashMapOf(
                "user_id" to auth.uid,
                "user_name" to userName,
                "shared_event_list" to shared_event_list,
                "saved_location_list" to saved_location_list,
                "message_roooms_id" to meessageRoomsId,

            )
            auth.uid?.let { it1 -> userCollection.document(it1).set(user) }
            isCreate(true)
        }.addOnFailureListener {
            isCreate(false)
        }

    }

    fun loginUser(email:String,password:String, isLogin:(Boolean) -> Unit){
         auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            isLogin(true)
         }.addOnFailureListener {
             isLogin(false)
         }
    }

    fun logoutUser(){
        auth.signOut()
    }


    fun getUserData(): Flow<User> = channelFlow {
        val collection = auth.uid?.let { userId ->
            val snapshotListener = db.collection("Users").document(userId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        // Hata durumunu işle
                        close(error)
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        val data = snapshot.toObject<User>(User::class.java)
                        if (data != null) {
                            // Veriyi gönder
                            trySend(data).isSuccess
                        } else {
                            // Dönüşüm başarısız oldu
                            close(Exception("User data conversion failed"))
                        }
                    } else {
                        // Belge bulunamadı veya null
                        close(Exception("User data not found"))
                    }
                }
            // Akış sona erdiğinde snapshot dinleyicisini kapat
            awaitClose {
                snapshotListener.remove()
            }
        } ?: close(Exception("User ID is null"))
    }





}