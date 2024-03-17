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
                "password" to password,
                "user_mail" to email,
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

    fun changePassword(password:String,isChange:(Boolean) -> Unit){
        auth.currentUser?.updatePassword(password)?.addOnSuccessListener {
            isChange(true)
        }?.addOnFailureListener {
            isChange(false)
        }
    }
    fun changeEmail(email:String,isChange:(Boolean) -> Unit){
        auth.currentUser?.updateEmail(email)?.addOnSuccessListener {
            val userCollection=db.collection("Users")
            auth.uid?.let { it1 -> userCollection.document(it1).update("user_mail",email) }
            isChange(true)
        }?.addOnFailureListener {
            isChange(false)
        }
    }


    fun getUserData(): Flow<User> = channelFlow {
        val collection = auth.uid?.let { userId ->
            val snapshotListener = db.collection("Users").document(userId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        val data = snapshot.toObject<User>(User::class.java)
                        if (data != null) {
                            trySend(data).isSuccess
                        } else {
                            close(Exception("User data conversion failed"))
                        }
                    } else {
                        close(Exception("User data not found"))
                    }
                }
            awaitClose {
                snapshotListener.remove()
            }
        } ?: close(Exception("User ID is null"))
    }

    fun checkUserPassword(password:String,isCheck:(Boolean) -> Unit){
        auth.currentUser?.email?.let { it1 -> auth.signInWithEmailAndPassword(it1, password).addOnSuccessListener {
            isCheck(true)
        }?.addOnFailureListener {
            isCheck(false)
        } }
    }





    fun changeUserName(userName: String,isChange:(Boolean) -> Unit){
        val userCollection=db.collection("Users")
        auth.uid?.let { it1 ->
            userCollection.document(it1).update("user_mail",userName).addOnSuccessListener {
                isChange(true)
            }.addOnFailureListener{
                isChange(false)
            }
        }?.addOnFailureListener{
            isChange(false)
        }
    }



}