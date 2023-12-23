package com.example. mevltbul.Repository

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.mevltbul.Classes.Marker
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.LinkedList
import java.util.Queue

class DetailPageDaoRepo{

    private lateinit var db:FirebaseFirestore
    private lateinit var storageRef:StorageReference
    lateinit var urlLiveData: MutableLiveData<ArrayList<String>?>

    init {
        db=Firebase.firestore
        storageRef=FirebaseStorage.getInstance().reference.child("Images")
        urlLiveData=MutableLiveData<ArrayList<String>?>()

    }

    suspend fun publishDetail(contex :Context, marker_id:String?,
                              marker_latitude:String?=null,
                              marker_longtitude: String?=null,
                              marker_detail:String?=null, imageList: ArrayList<Uri?>){
        withContext(Dispatchers.IO) {
            uploadImage(imageList)
        }
        withContext(Dispatchers.IO){
            uploadImage(imageList)
        }

        val urlList= urlLiveData.value
        Log.d("hatamDetailDaoUrlList","url list: "+urlList.toString())
        Log.d("hatamDetailDaoUrlList","url liveData: "+urlLiveData.value.toString())

        val marker=Marker(marker_id,marker_latitude,marker_longtitude,marker_detail
            , urlList?.get(0), urlList?.get(1), urlList?.get(2), urlList?.get(3)
        )
        Log.d("hatamDetailDaoPublish2",marker.photo1.toString()+"\n"+marker.photo2.toString())
        marker.marker_id?.let {
            db.collection("images").document(it).set(marker).addOnSuccessListener {
                Toast.makeText(contex,"Paylaşıldı",Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(contex,"Hata ${it.localizedMessage}",Toast.LENGTH_LONG).show()
            }
        }

    }
    suspend fun uploadImage(list:ArrayList<Uri?>){
        val urlList=ArrayList<String?>()
        Log.d("hatamDetailDaoUplodImg"," list = "+list.toString())
        storageRef=storageRef.child(System.currentTimeMillis().toString())
        for (i in 0..<list.size){
            val temp=list.get(i)
            if (temp!=null){
                storageRef.putFile(temp).addOnCompleteListener{task->
                    if (task.isSuccessful){
                        storageRef.downloadUrl.addOnSuccessListener {uri->
                            Log.d("hatamDetailDaoUplodImg"," sa"+uri.toString())
                            // Mevcut değeri al
                            val currentList = urlLiveData.value

                            // Yeni URI'yi ekle
                            currentList?.add(uri.toString())

                            // Güncellenmiş listeyi LiveData'ya ata
                            urlLiveData.value = currentList
                        }
                    }else{
                        Log.d("hatamDetailDaoUplodImg"," put if ")

                    }

                }.addOnFailureListener{
                    Log.e("hatamDetailDaoUplodImg"," putFİle "+it.message)

                }
            }
        }
        Log.d("hatamDetailDaoPublish","before ret "+ urlLiveData.value.toString())



    }

}