package com.example.mevltbul.Pages

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mevltbul.Adapter.MessagePageAdapter
import com.example.mevltbul.R
import com.example.mevltbul.ViewModel.MessageVM
import com.example.mevltbul.ViewModel.UserVM
import com.example.mevltbul.databinding.FragmentMessagesPageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.launch

@AndroidEntryPoint
class MessagesPage : Fragment() {

    lateinit var bindng: FragmentMessagesPageBinding
    lateinit var messagePageVM:MessageVM
    lateinit var userVM: UserVM
    lateinit var userId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempMessagePageVM:MessageVM by viewModels()
        val tempUserVM:UserVM by viewModels()
        userVM=tempUserVM
        messagePageVM=tempMessagePageVM

    }



    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val bottomNavigationView=requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility=View.GONE
        val fabButton=requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fabButton.visibility=View.GONE

        try {
            bindng=FragmentMessagesPageBinding.inflate(inflater,container,false)

            val messageRoomId= arguments?.getString("message_room_id")
            userId= FirebaseAuth.getInstance().uid.toString()
            userVM.getUserData()
            messagePageVM.getMessage(messageRoomId!!)

            viewLifecycleOwner.lifecycleScope.launch {
                userVM.userData.collect{user->
                    if (user.message_roooms_id?.find{it==messageRoomId} !=null){
                        bindng.layoutMessagePageMakeJoinChat.visibility=View.GONE
                    }else{
                        bindng.layoutMessagePageMakeJoinChat.visibility=View.VISIBLE
                    }
                }
            }


            messagePageVM.messagesLiveData.observe(viewLifecycleOwner){
                val adapter=MessagePageAdapter(requireContext(),it,userId,messageRoomId)
                bindng.messagePageRecycler.adapter=adapter
                adapter.notifyDataSetChanged()
                bindng.messagePageRecycler.layoutManager=LinearLayoutManager(requireContext())
                bindng.messagePageRecycler.scrollToPosition(adapter.itemCount - 1)

            }

            bindng.btnSendMessage.setOnClickListener{
                val message=bindng.txtSendMessage.text.toString()
                if (message.isNotEmpty()){
                    messagePageVM.sendMessage(messageRoomId!!,message,userId){
                        if (it){
                            messagePageVM.getMessage(messageRoomId!!)
                            bindng.txtSendMessage.text.clear()
                        }else{
                            Toast.makeText(requireContext(),"Hata : Mesajınızı Gönderilemedi",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            bindng.btnMessagePageJoinChat.setOnClickListener{
                messagePageVM.addMassageRoomToUserDatabase(userId,messageRoomId!!)
                bindng.layoutMessagePageMakeJoinChat.visibility=View.GONE

            }

        }catch (e:Exception){
            Log.e("hatamMessagePage", "exceptionnum : ",e)
        }

        return bindng.root
    }

}