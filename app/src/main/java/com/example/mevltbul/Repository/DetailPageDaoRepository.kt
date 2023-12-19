package com.example.mevltbul.Repository

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.example.mevltbul.Classes.Marker
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class DetailPageDaoRepository {

    private lateinit var db:FirebaseFirestore

    init {
        db=Firebase.firestore
    }

    fun uploadImage(contex :Context,marker: Marker){

        marker.marker_id?.let {
            db.collection("images").document(it).set(marker).addOnSuccessListener {
                Toast.makeText(contex,"Paylaşıldı",Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(contex,"Hata ${it.localizedMessage}",Toast.LENGTH_LONG).show()
            }
        }

    }
}