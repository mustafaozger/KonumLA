package com.example.mevltbul.Classes

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class User (
    var user_id: String? = null,
    var user_name: String? = null,
    var user_shared_event_list_id:ArrayList<String>?=null,
    var user_saved_location_list_is:ArrayList<String>?= null,
    var user_chat_room_list_id:ArrayList<String>?= null
    ){
}