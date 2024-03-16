package com.example.mevltbul.Classes

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User (
    var user_id: String? = null,
    var user_name: String? = null,
    var user_password: String? = null,
    var user_mail:String? = null,
    var shared_event_list:ArrayList<String>?=null,
    var saved_location_list:ArrayList<String>?= null,
    var message_roooms_id:ArrayList<String>?= null
    ){
}