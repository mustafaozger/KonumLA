package com.example.mevltbul.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mevltbul.Classes.MessageRoomModel
import com.example.mevltbul.R
import com.example.mevltbul.databinding.DesignEventMessagesListBinding
import com.squareup.picasso.Picasso

class MessageRoomAdapter(val context: Context,val chatRoomIdList:ArrayList<MessageRoomModel>):RecyclerView.Adapter<MessageRoomAdapter.EventMessageListPageVH>() {
    inner class EventMessageListPageVH(val binding:DesignEventMessagesListBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventMessageListPageVH {
        val layoutInflater=LayoutInflater.from(context)
        val binding=DesignEventMessagesListBinding.inflate(layoutInflater,parent,false)
        return EventMessageListPageVH(binding)
    }

    override fun getItemCount(): Int {
        return chatRoomIdList.size
    }

    override fun onBindViewHolder(holder: EventMessageListPageVH, position: Int) {
        val binding=holder.binding
        if (chatRoomIdList[position].message_image!=null){
            Picasso.get().load(chatRoomIdList[position].message_image)
                .centerCrop()
                .fit().placeholder(R.drawable.loading_placeholder)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.eventMessagesListPageImage)
        }
        binding.eventMessagesListPageEventName.text= chatRoomIdList[position].message_room_name

        binding.designEventListMessageLayout.setOnClickListener {
            val bundle=Bundle()
            bundle.putString("message_room_id", chatRoomIdList[position].message_room_id)
            Navigation.findNavController(it).navigate(R.id.action_eventMessagesListPage_to_messagesPage,bundle)
        }


    }
}