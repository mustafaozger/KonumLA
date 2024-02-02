package com.example.mevltbul.Repository

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
    private val markerList=MutableLiveData<ArrayList<Marker>>()

    fun getEventLists():MutableLiveData<ArrayList<Marker>>{
        return markerList
    }


    fun getEventLists(latitude: Double, longitude: Double):MutableLiveData<ArrayList<Marker>>{
        return getEventListsFromDatabas(latitude,longitude)

    }

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


        uploadImages(imageList, "markers") { imageUrl ->
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




   private fun getEventListsFromDatabas(latitude:Double,longitude:Double):MutableLiveData<ArrayList<Marker>>{
       val collection= db.collection("images")
       collection.whereGreaterThan("marker_latitude",(latitude-0.1))
       collection.whereLessThanOrEqualTo("marker_latitude",(latitude+0.1).toString())
       collection.whereGreaterThanOrEqualTo("marker_longtitude",(longitude-0.15).toString())
       collection.whereLessThanOrEqualTo("marker_longtitude",(longitude+0.15).toString())
       collection.get().addOnSuccessListener {documents->
            if(!documents.isEmpty){
                val list=ArrayList<Marker>()
                for (document in documents){
                    val data = document.data
                    val marker=Marker(
                        data.get("marker_id") as String? ,
                        data.get("marker_latitude") as String?,
                        data.get("marker_longtitude") as String?,
                        data.get("marker_detail") as String?,
                        data.get("photo1") as String?,
                        data.get("photo2") as String?,
                        data.get("photo3") as String?,
                        data.get("photo4") as String?
                        )
                    list.add(marker)
                }
                markerList.value=list

            }

        }
        return markerList
    }


    fun getEventDetail(){

    }


}