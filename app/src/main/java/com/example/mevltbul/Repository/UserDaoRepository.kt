package com.example.mevltbul.Repository

import com.example.mevltbul.Classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.security.auth.callback.Callback

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
            val user= hashMapOf(
                "user_id" to auth.uid,
                "user_name" to "User",
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

    fun getUserData():Flow<User> =flow{
        val collection= auth.uid?.let {
            val snapshot=  db.collection("Users").document(it).get().await()
            val data =snapshot.toObject(User::class.java)
            if(data!=null){
                emit(data)
            }else{
                emit(User())
            }
        }
    }



}