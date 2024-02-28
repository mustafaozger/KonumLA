package com.example.mevltbul.Classes

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties

data class MessageModel (
    var messageId:String?=null,
    var message:String?=null,
    var senderId:String?=null,
    var date:Long?=null
){

}