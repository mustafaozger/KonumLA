package com.example.mevltbul.Classes

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class MessageRoomModel (
    var message_room_id:String?=null,
    var message_room_name:String?=null,
    var message_image:String?=null
){
}