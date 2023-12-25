package com.example. mevltbul.Repository

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.mevltbul.Classes.Marker
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.delay
import java.util.LinkedList
import java.util.Queue

class DetailPageDaoRepo{

    private  var db:FirebaseFirestore=Firebase.firestore
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    private val urlQueue:Queue<String> = LinkedList()

    fun publishDetail(
        marker_id: String?,
        marker_latitude: String? = null,
        marker_longitude: String? = null,
        marker_detail: String? = null,
        imageList: ArrayList<Uri?>,
        callback: (Boolean) -> Unit
    ) {
        val totalImages = imageList.size
        var uploadedImages = 0


        uploadImages(imageList, "images") { imageUrl ->
            uploadedImages++
            if (uploadedImages == totalImages) {
                for ( i in 0..3){
                    if (urlQueue.size<4){
                        urlQueue.add(null)
                    }
                }
                val marker = Marker(
                    marker_id,
                    marker_latitude,
                    marker_longitude,
                    marker_detail,
                    urlQueue.poll(),
                    urlQueue.poll(),
                    urlQueue.poll(),
                    urlQueue.poll()
                )

                marker.marker_id?.let {
                    db.collection("images").document(it).set(marker)
                        .addOnSuccessListener {
                            callback(true)
                        }
                        .addOnFailureListener {
                            callback(false)
                        }
                }
            }
        }
    }

    private fun uploadImages(imageUris: ArrayList<Uri?>, folderName: String, callback: (String) ->  Unit) {
        val imageNamePrefix = "image_${System.currentTimeMillis()}"
        var uploadedCount = 0

        for (imageUri in imageUris) {
            val imageName = "$imageNamePrefix${uploadedCount++}"
            val imageRef: StorageReference = storageReference.child("$folderName/$imageName")

            if (imageUri != null) {
                imageRef.putFile(imageUri)
                    .addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                            urlQueue.add(uri.toString())
                            callback(uri.toString())
                        }
                    }
                    .addOnFailureListener { exception ->
                        exception.printStackTrace()
                    }
            }
        }
    }


}