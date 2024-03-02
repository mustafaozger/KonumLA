package com.example.mevltbul.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mevltbul.Classes.Marker
import com.example.mevltbul.R
import com.example.mevltbul.databinding.DesignEventMessagesListBinding
import com.squareup.picasso.Picasso

class EventMessageListPageAdapter(val context: Context,val chatRoomID:ArrayList<Marker>):RecyclerView.Adapter<EventMessageListPageAdapter.EventMessageListPageVH>() {
    inner class EventMessageListPageVH(val binding:DesignEventMessagesListBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventMessageListPageVH {
        val layoutInflater=LayoutInflater.from(context)
        val binding=DesignEventMessagesListBinding.inflate(layoutInflater,parent,false)
        return EventMessageListPageVH(binding)
    }

    override fun getItemCount(): Int {
        return chatRoomID.size
    }

    override fun onBindViewHolder(holder: EventMessageListPageVH, position: Int) {
        val binding=holder.binding
        if (chatRoomID[position].photo1!=null){
            Picasso.get().load(chatRoomID[position].photo1).centerCrop().into(binding.eventMessagesListPageImage)
        }
        binding.eventMessagesListPageEventName.text=chatRoomID[position].event_type
        binding.designEventListMessageLayout.setOnClickListener {
            val bundle=Bundle()
            bundle.putString("message_room_id",chatRoomID[position].marker_id)
            Navigation.findNavController(it).navigate(R.id.action_eventMessagesListPage_to_messagesPage,bundle)
        }


    }
}