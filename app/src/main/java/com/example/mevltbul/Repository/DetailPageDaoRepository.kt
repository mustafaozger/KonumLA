package com.example.mevltbul.Repository

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.example.mevltbul.Classes.Marker
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.LinkedList
import java.util.Queue

class DetailPageDaoRepository {

    private lateinit var db:FirebaseFirestore
    private lateinit var storageRef:StorageReference
    val urlList=ArrayList<String?>()


    init {
        db=Firebase.firestore
        storageRef=FirebaseStorage.getInstance().reference.child("Images")
    }

    fun publishDetail(contex :Context,marker_id:String?,
                       marker_latitude:String?=null,
                       marker_longtitude: String?=null,
                       marker_detail:String?=null,imageList: ArrayList<Uri?>){

        uploadImage(imageList)

        val marker=Marker(marker_id,marker_latitude,marker_longtitude,marker_detail
            ,urlList.get(0),urlList.get(1),urlList.get(2),urlList.get(3))

        marker.marker_id?.let {
            db.collection("images").document(it).set(marker).addOnSuccessListener {
                Toast.makeText(contex,"Paylaşıldı",Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(contex,"Hata ${it.localizedMessage}",Toast.LENGTH_LONG).show()
            }
        }

    }
    fun uploadImage(list:ArrayList<Uri?>){

        storageRef=storageRef.child(System.currentTimeMillis().toString())
         for (i in 0..<list.size){
            var temp=list.get(i)
            if (temp!=null){
                storageRef.putFile(temp).addOnCompleteListener{task->
                    if (task.isSuccessful){
                        storageRef.downloadUrl.addOnSuccessListener {uri->
                            this.urlList.add(uri.toString())
                        }
                    }

                }
            }
        }

        for (i in 0..3){
            if(urlList.size<=4){
                urlList.add(null)
            }
        }

    }


}