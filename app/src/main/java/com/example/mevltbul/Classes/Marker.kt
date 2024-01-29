package com.example.mevltbul.Classes

import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Marker(var marker_id:String?=null,
                  var marker_latitude:String?=null,
                  var marker_longtitude: String?=null,
                  var marker_detail:String?=null,
                  var photo1:String?=null,
                  var photo2: String?=null,
                  var photo3:String?=null,
                  var photo4:String?=null

    ):Serializable{
}