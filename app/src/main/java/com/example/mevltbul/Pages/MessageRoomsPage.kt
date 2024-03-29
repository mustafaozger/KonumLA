package com.example.mevltbul.Pages

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mevltbul.Adapter.MessageRoomAdapter
import com.example.mevltbul.Classes.Marker
import com.example.mevltbul.R
import com.example.mevltbul.ViewModel.DetailVM
import com.example.mevltbul.ViewModel.MessageVM
import com.example.mevltbul.databinding.FragmentEventMessagesListPageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint

class MessageRoomsPage : Fragment() {

    lateinit var bindig:FragmentEventMessagesListPageBinding
    lateinit var messageVM:MessageVM
    lateinit var detailVM:DetailVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempMessageVM:MessageVM by viewModels()
        messageVM=tempMessageVM
        val tempDetailVM:DetailVM by viewModels()
        detailVM=tempDetailVM
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindig=FragmentEventMessagesListPageBinding.inflate(inflater,container,false)

        val bottomNavigationView=requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility=View.VISIBLE
        val fabButton=requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fabButton.visibility=View.VISIBLE

        val userID=FirebaseAuth.getInstance().uid
        messageVM.getMessageRooms(userID!!)
        Log.d("hatamRoomListID","work")

        detailVM.getMessageRooms().observe(viewLifecycleOwner){
            Log.d("hatamRoomListID","event lisy  $it ")
            val adapter=MessageRoomAdapter(requireContext(),it)
            bindig.eventMessageRoomRcyler.adapter=adapter
            bindig.eventMessageRoomRcyler.layoutManager=LinearLayoutManager(requireContext())
        }

//        messageVM.messageRoomLiveData.observe(viewLifecycleOwner){roomListIdList->
//            Log.d("hatamRoomListID","id list  $roomListIdList")
//            if (roomListIdList != null) {
//
//
//
//            }
//        }

        return bindig.root
    }



}